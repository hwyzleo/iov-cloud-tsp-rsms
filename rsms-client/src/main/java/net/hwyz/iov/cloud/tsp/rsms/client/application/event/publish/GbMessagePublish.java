package net.hwyz.iov.cloud.tsp.rsms.client.application.event.publish;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessage;
import net.hwyz.iov.cloud.tsp.rsms.client.application.event.event.VehicleGbMessageEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * 国标消息发布类
 *
 * @author hwyz_leo
 */
@Slf4j
@Component
@RequiredArgsConstructor
@DependsOn("clientPlatformManager")
public class GbMessagePublish {

    private final ApplicationContext ctx;

    /**
     * 发送车辆国标消息
     *
     * @param clientPlatformId 客户端平台ID
     * @param vin              车架号
     * @param gbMessage        国标消息
     */
    public void sendVehicleData(Long clientPlatformId, String vin, GbMessage gbMessage) {
        ctx.publishEvent(new VehicleGbMessageEvent(clientPlatformId, vin, gbMessage));
    }

}
