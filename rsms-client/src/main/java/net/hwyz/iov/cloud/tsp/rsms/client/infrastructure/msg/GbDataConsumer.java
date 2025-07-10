package net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.msg;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.util.GbUtil;
import net.hwyz.iov.cloud.tsp.rsms.client.application.event.publish.GbMessagePublish;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.ReceiverOptions;

import javax.annotation.PostConstruct;
import java.util.Collections;

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

    /**
     * 国标数据主题
     */
    private final String TOPIC_GB_DATA = "vagw-rsms-data";

    private final GbMessagePublish gbMessagePublish;

    /**
     * 消费国标数据消息
     */
    @PostConstruct
    public void consume() {
        ReceiverOptions<byte[], byte[]> options = ReceiverOptions.create(properties.buildConsumerProperties());
        options = options.subscription(Collections.singleton(TOPIC_GB_DATA));
        logger.info("开始监听国标数据消息");
        new ReactiveKafkaConsumerTemplate<>(options)
                .receiveAutoAck()
                .flatMap(record -> {
                    String vin = null;
                    try {
                        vin = new String(record.key());
                        GbUtil.parseMessage(record.value(), vin, false).ifPresent(gbMessage -> {
                            gbMessagePublish.sendVehicleData(gbMessage.getVin(), gbMessage);
                        });
                    } catch (Exception e) {
                        logger.error("消费车辆[{}]国标数据消息异常", vin, e);
                    }
                    return Mono.empty();
                }, concurrency)
                .doOnError(throwable -> {
                    logger.error("消费车辆国标数据消息异常", throwable);
                })
                .subscribe();
    }

}
