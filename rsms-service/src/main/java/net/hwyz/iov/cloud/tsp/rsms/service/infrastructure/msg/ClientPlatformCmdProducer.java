package net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.msg;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.ClientPlatformCmd;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 客户端平台指令消息生产者
 *
 * @author hwyz_leo
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ClientPlatformCmdProducer {

    private final ReactiveKafkaProducerTemplate<String, String> stringReactiveKafkaProducerTemplate;

    /**
     * 客户端平台指令主题
     */
    private final String TOPIC_CLIENT_PLATFORM_CMD = "rsms-client-platform-cmd";

    /**
     * 发送客户端平台指令
     *
     * @param clientPlatformId 客户端平台ID
     * @param cmd              客户端平台指令
     */
    public void send(Long clientPlatformId, ClientPlatformCmd cmd) {
        send(clientPlatformId, null, cmd);
    }

    /**
     * 发送客户端平台指令
     *
     * @param clientPlatformId 客户端平台ID
     * @param hostname         客户端平台主机名
     * @param cmd              客户端平台指令
     */
    public void send(Long clientPlatformId, String hostname, ClientPlatformCmd cmd) {
        Map<String, Object> map = new HashMap<>();
        map.put("clientPlatformId", clientPlatformId);
        map.put("hostname", hostname);
        map.put("cmd", cmd.name());
        String jsonStr = JSONUtil.toJsonStr(map);
        logger.debug("发送客户端平台[{}:{}]指令[{}]", clientPlatformId, hostname, cmd);
        stringReactiveKafkaProducerTemplate.send(TOPIC_CLIENT_PLATFORM_CMD, String.valueOf(clientPlatformId), jsonStr)
                .doOnError(throwable -> logger.error("发送客户端平台[{}:{}]指令[{}]异常[{}]", clientPlatformId, hostname, jsonStr, throwable.getMessage()))
                .subscribe();
    }

}
