package net.hwyz.iov.cloud.tsp.rsms.api.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 国标运行模式枚举类
 *
 * @author hwyz_leo
 */
@Getter
@AllArgsConstructor
public enum GbRunningMode {

    /** 纯电 **/
    ELECTRIC((byte) 0x01, "纯电"),
    /** 混动 **/
    HYBRID((byte) 0x02, "混动"),
    /** 燃油 **/
    FUEL((byte) 0x03, "燃油"),
    /** 异常 **/
    ABNORMAL((byte) 0xFE, "异常"),
    /** 无效 **/
    INVALID((byte) 0xFF, "无效");

    private final byte code;
    private final String name;

    public static GbRunningMode valOf(byte val) {
        return Arrays.stream(GbRunningMode.values())
                .filter(gbRunningMode -> gbRunningMode.code == val)
                .findFirst()
                .orElse(null);
    }

}
