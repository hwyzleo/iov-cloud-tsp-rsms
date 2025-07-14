package net.hwyz.iov.cloud.tsp.rsms.client.application.event.event;

import lombok.Getter;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessage;

/**
 * 车辆国标消息事件
 *
 * @author hwyz_leo
 */
@Getter
public class VehicleGbMessageEvent extends BaseEvent {

    /**
     * 客户端平台ID
     */
    private final Long clientPlatformId;
    /**
     * 车架号
     */
    private final String vin;
    /**
     * 国标数据
     */
    private final GbMessage gbMessage;

    public VehicleGbMessageEvent(Long clientPlatformId, String vin, GbMessage gbMessage) {
        super();
        this.clientPlatformId = clientPlatformId;
        this.vin = vin;
        this.gbMessage = gbMessage;
    }

}
