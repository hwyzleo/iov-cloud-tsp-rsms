package net.hwyz.iov.cloud.tsp.rsms.api.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 国标数据信息类型枚举类
 *
 * @author hwyz_leo
 */
@Getter
@AllArgsConstructor
public enum GbDataInfoType {

    /** 整车数据 **/
    VEHICLE((byte) 0x01),
    /** 驱动电机数据 **/
    DRIVE_MOTOR((byte) 0x02),
    /** 燃料电池数据 **/
    FUEL_CELL((byte) 0x03),
    /** 发动机数据 **/
    ENGINE((byte) 0x04),
    /** 车辆位置数据 **/
    POSITION((byte) 0x05),
    /** 极值数据 **/
    EXTREMUM((byte) 0x06),
    /** 报警数据 **/
    ALARM((byte) 0x07),
    /** 可充电储能装置电压数据 **/
    BATTERY_VOLTAGE((byte) 0x08),
    /** 可充电储能装置温度数据 **/
    BATTERY_TEMPERATURE((byte) 0x09);

    private final byte code;

    public static GbDataInfoType valOf(byte val) {
        return Arrays.stream(GbDataInfoType.values())
                .filter(gbDataInfoType -> gbDataInfoType.code == val)
                .findFirst()
                .orElse(null);
    }

}
