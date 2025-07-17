package net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.msg;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessage;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 国标补发数据消息生产者
 *
 * @author hwyz_leo
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GbReissueDataProducer {

    private final ReactiveKafkaProducerTemplate<String, String> stringReactiveKafkaProducerTemplate;

    /**
     * 补发消息主题
     */
    private final String TOPIC_REISSUE_MESSAGE = "rsms-reissue-message";

    /**
     * 发送补发消息
     *
     * @param message  补发消息
     * @param hostname 主机名
     */
    public void send(String vin, GbMessage message, String hostname) {
        Map<String, Object> map = new HashMap<>();
        map.put("hostname", hostname);
        map.put("message", message.toByteArray());
        String jsonStr = JSONUtil.toJsonStr(map);
        stringReactiveKafkaProducerTemplate.send(TOPIC_REISSUE_MESSAGE, vin, jsonStr)
                .doOnError(throwable -> logger.error("发送车辆[{}]补发消息[{}]异常[{}]", vin, jsonStr, throwable.getMessage()))
                .subscribe();
    }

}
