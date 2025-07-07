package net.hwyz.iov.cloud.tsp.rsms.api.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 国标驱动电机状态枚举类
 *
 * @author hwyz_leo
 */
@Getter
@AllArgsConstructor
public enum GbDriveMotorState {

    /** 耗电 **/
    CONSUMPTION((byte) 0x01, "耗电"),
    /** 发电 **/
    GENERATION((byte) 0x02, "发电"),
    /** 关闭 **/
    CLOSED((byte) 0x03, "关闭"),
    /** 准备 **/
    READY((byte) 0x04, "准备"),
    /** 异常 **/
    ABNORMAL((byte) 0xFE, "异常"),
    /** 无效 **/
    INVALID((byte) 0xFF, "无效");

    private final byte code;
    private final String name;

    public static GbDriveMotorState valOf(byte val) {
        return Arrays.stream(GbDriveMotorState.values())
                .filter(gbDriveMotorState -> gbDriveMotorState.code == val)
                .findFirst()
                .orElse(null);
    }

}
