package net.hwyz.iov.cloud.tsp.rsms.api.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 国标报警标志位枚举类
 *
 * @author hwyz_leo
 */
@Getter
@AllArgsConstructor
public enum GbAlarmFlag {

    ALARM_0(0, "温度差异报警"),
    ALARM_1(1, "电池高温报警"),
    ALARM_2(2, "车载储能装置类型过压报警"),
    ALARM_3(3, "车载储能装置类型欠压报警"),
    ALARM_4(4, "SOC低报警"),
    ALARM_5(5, "单体电池过压报警"),
    ALARM_6(6, "单体电池欠压报警"),
    ALARM_7(7, "SOC过高报警"),
    ALARM_8(8, "SOC跳变报警"),
    ALARM_9(9, "可充电储能系统不匹配报警"),
    ALARM_10(10, "电池单体一致性差报警"),
    ALARM_11(11, "绝缘报警"),
    ALARM_12(12, "DC-DC温度报警"),
    ALARM_13(13, "制动系统报警"),
    ALARM_14(14, "DC-DC状态报警"),
    ALARM_15(15, "驱动电机控制器温度报警"),
    ALARM_16(16, "高压互锁状态报警"),
    ALARM_17(17, "驱动电机温度报警"),
    ALARM_18(18, "车载储能装置类型过充报警");

    private final int code;
    private final String name;

    public static GbAlarmFlag valOf(int val) {
        return Arrays.stream(GbAlarmFlag.values())
                .filter(gbAlarmFlag -> gbAlarmFlag.code == val)
                .findFirst()
                .orElse(null);
    }

}
