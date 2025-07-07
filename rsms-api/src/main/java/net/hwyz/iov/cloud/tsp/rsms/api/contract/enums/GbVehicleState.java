package net.hwyz.iov.cloud.tsp.rsms.api.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 国标车辆状态枚举类
 *
 * @author hwyz_leo
 */
@Getter
@AllArgsConstructor
public enum GbVehicleState {

    /** 车辆启动 **/
    POWER_ON((byte) 0x01, "车辆启动"),
    /** 车辆熄火 **/
    POWER_OFF((byte) 0x02, "车辆熄火"),
    /** 其他 **/
    OTHER((byte) 0x03, "其他"),
    /** 异常 **/
    ABNORMAL((byte) 0xFE, "异常"),
    /** 无效 **/
    INVALID((byte) 0xFF, "无效");

    private final byte code;
    private final String name;

    public static GbVehicleState valOf(byte val) {
        return Arrays.stream(GbVehicleState.values())
                .filter(gbVehicleState -> gbVehicleState.code == val)
                .findFirst()
                .orElse(null);
    }

}
