package net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.msg;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.system.SystemUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.ClientPlatformCmd;
import net.hwyz.iov.cloud.tsp.rsms.client.application.event.publish.ClientPlatformCmdPublish;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.repository.ClientPlatformRepository;
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
 * 客户端平台指令消息消费者
 *
 * @author hwyz_leo
 */
@Slf4j
@Component
@RefreshScope
@RequiredArgsConstructor
public class ClientPlatformCmdConsumer {

    private final KafkaProperties properties;

    @Value("${spring.kafka.consumer.reactive-concurrency:1}")
    private Integer concurrency;

    /**
     * 客户端平台指令主题
     */
    private final String TOPIC_CLIENT_PLATFORM_CMD = "rsms-client-platform-cmd";

    private final ClientPlatformCmdPublish clientPlatformCmdPublish;
    private final ClientPlatformRepository clientPlatformRepository;

    /**
     * 消费客户端平台命令消息
     */
    @PostConstruct
    public void consume() {
        // 这里每次启动会产生随机消费组，会造成消费组不断累积，但该TOPIC量级不大，并且默认7天自动删除不活跃消费者，可忽略
        properties.getConsumer().setGroupId(properties.getConsumer().getGroupId() + "-" + SystemUtil.getHostInfo().getName());
        ReceiverOptions<byte[], byte[]> options = ReceiverOptions.create(properties.buildConsumerProperties());
        options = options.subscription(Collections.singleton(TOPIC_CLIENT_PLATFORM_CMD));
        logger.info("开始监听客户端平台命令消息");
        new ReactiveKafkaConsumerTemplate<>(options)
                .receiveAutoAck()
                .flatMap(record -> {
                    Long clientPlatformId = null;
                    try {
                        clientPlatformId = Long.valueOf(new String(record.key()));
                        clientPlatformRepository.getById(clientPlatformId).ifPresent(clientPlatform -> {
                            JSONObject json = JSONUtil.parseObj(new String(record.value()));
                            if (clientPlatform.matchHostname(json.getStr("hostname"))) {
                                clientPlatformCmdPublish.sendPlatformCmd(clientPlatform, ClientPlatformCmd.valOf(json.getStr("cmd")));
                            }
                        });
                    } catch (Exception e) {
                        logger.error("消费客户端平台[{}]命令消息异常", clientPlatformId, e);
                    }
                    return Mono.empty();
                }, concurrency)
                .doOnError(throwable -> {
                    logger.error("消费客户端平台命令消息异常", throwable);
                })
                .subscribe();
    }

}
