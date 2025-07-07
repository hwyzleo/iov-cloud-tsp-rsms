package net.hwyz.iov.cloud.tsp.rsms.api.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 国标充电状态枚举类
 *
 * @author hwyz_leo
 */
@Getter
@AllArgsConstructor
public enum GbChargingState {

    /** 停车充电 **/
    PARKING_CHARGING((byte) 0x01, "停车充电"),
    /** 行驶充电 **/
    DRIVING_CHARGING((byte) 0x02, "行驶充电"),
    /** 未充电 **/
    UNCHARGED((byte) 0x03, "未充电"),
    /** 充电完成 **/
    CHARGING_COMPLETE((byte) 0x04, "充电完成"),
    /** 异常 **/
    ABNORMAL((byte) 0xFE, "异常"),
    /** 无效 **/
    INVALID((byte) 0xFF, "无效");

    private final byte code;
    private final String name;

    public static GbChargingState valOf(byte val) {
        return Arrays.stream(GbChargingState.values())
                .filter(gbVehicleChargingState -> gbVehicleChargingState.code == val)
                .findFirst()
                .orElse(null);
    }

}
