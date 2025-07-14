package net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.msg;

import cn.hutool.core.collection.ConcurrentHashSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.util.GbUtil;
import net.hwyz.iov.cloud.tsp.rsms.client.application.event.event.NettyClientConnectEvent;
import net.hwyz.iov.cloud.tsp.rsms.client.application.event.publish.GbMessagePublish;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.model.ClientPlatformDo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;
import java.util.Set;

/**
 * 国标数据消费者
 *
 * @author hwyz_leo
 */
@Slf4j
@Component
@RefreshScope
@RequiredArgsConstructor
public class GbDataConsumer {

    private final KafkaProperties properties;

    @Value("${spring.kafka.consumer.reactive-concurrency:5}")
    private Integer concurrency;

    private static final Set<String> CONSUMER = new ConcurrentHashSet<>();

    /**
     * 国标数据主题
     */
    private final String TOPIC_GB_DATA = "vagw-rsms-data";

    private final GbMessagePublish gbMessagePublish;

    /**
     * 消费国标数据消息
     */
    private void consume(ClientPlatformDo clientPlatform) {
        String uniqueKey = clientPlatform.getUniqueKey();
        if (!CONSUMER.contains(uniqueKey)) {
            properties.getConsumer().setGroupId(properties.getConsumer().getGroupId() + "-" + uniqueKey);
            ReceiverOptions<byte[], byte[]> options = ReceiverOptions.create(properties.buildConsumerProperties());
            options = options.subscription(Collections.singleton(TOPIC_GB_DATA));
            logger.info("客户端平台[{}]开始监听国标数据消息", uniqueKey);
            new ReactiveKafkaConsumerTemplate<>(options)
                    .receiveAutoAck()
                    .flatMap(record -> {
                        String vin = null;
                        try {
                            vin = new String(record.key());
                            GbUtil.parseMessage(record.value(), vin, false).ifPresent(gbMessage -> {
                                gbMessagePublish.sendVehicleData(clientPlatform.getId(), gbMessage.getVin(), gbMessage);
                            });
                        } catch (Exception e) {
                            logger.error("客户端平台[{}]消费车辆[{}]国标数据消息异常", uniqueKey, vin, e);
                        }
                        return Mono.empty();
                    }, concurrency)
                    .doOnError(throwable -> {
                        logger.error("客户端平台[{}]消费车辆国标数据消息异常", uniqueKey, throwable);
                    })
                    .subscribe();
            CONSUMER.add(uniqueKey);
        }
    }

    /**
     * 订阅Netty客户端连接事件
     *
     * @param event 客户端平台登录事件
     */
    @EventListener
    public void onNettyClientConnectEvent(NettyClientConnectEvent event) {
        ClientPlatformDo clientPlatform = event.getClientPlatform();
        if (event.getConnectResult()) {
            consume(clientPlatform);
        }
    }

}
