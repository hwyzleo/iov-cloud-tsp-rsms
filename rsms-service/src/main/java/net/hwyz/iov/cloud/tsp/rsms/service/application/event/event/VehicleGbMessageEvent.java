package net.hwyz.iov.cloud.tsp.rsms.service.application.event.event;

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
     * 车架号
     */
    private final String vin;
    /**
     * 国标数据
     */
    private final GbMessage gbMessage;

    public VehicleGbMessageEvent(String vin, GbMessage gbMessage) {
        super();
        this.vin = vin;
        this.gbMessage = gbMessage;
    }

}
