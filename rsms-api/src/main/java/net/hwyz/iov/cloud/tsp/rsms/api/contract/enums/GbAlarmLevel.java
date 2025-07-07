package net.hwyz.iov.cloud.tsp.rsms.api.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 国标报警等级枚举类
 *
 * @author hwyz_leo
 */
@Getter
@AllArgsConstructor
public enum GbAlarmLevel {

    /** 无故障 **/
    NO_FAULT((byte) 0x00, "无故障"),
    /**
     * 1级故障
     * 指代不影响车辆正常行驶的故障
     **/
    LEVEL1((byte) 0x01, "1级故障"),
    /**
     * 2级故障
     * 指代影响车辆性能，需驾驶员限制行驶的故障
     **/
    LEVEL2((byte) 0x02, "2级故障"),
    /**
     * 3级故障
     * 为最高级别故障，指代驾驶员应立即停车处理或请求救援的故障
     **/
    LEVEL3((byte) 0x03, "3级故障"),
    /** 异常 **/
    ABNORMAL((byte) 0xFE, "异常"),
    /** 无效 **/
    INVALID((byte) 0xFF, "无效");

    private final byte code;
    private final String name;

    public static GbAlarmLevel valOf(byte val) {
        return Arrays.stream(GbAlarmLevel.values())
                .filter(gbAlarmLevel -> gbAlarmLevel.code == val)
                .findFirst()
                .orElse(null);
    }

}
