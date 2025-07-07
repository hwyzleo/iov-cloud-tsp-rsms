package net.hwyz.iov.cloud.tsp.rsms.api.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 国标DC/DC状态枚举类
 *
 * @author hwyz_leo
 */
@Getter
@AllArgsConstructor
public enum GbDcDcState {

    /** 工作 **/
    ACTIVE((byte) 0x01, "工作"),
    /** 断开 **/
    INACTIVE((byte) 0x02, "断开"),
    /** 异常 **/
    ABNORMAL((byte) 0xFE, "异常"),
    /** 无效 **/
    INVALID((byte) 0xFF, "无效");

    private final byte code;
    private final String name;

    public static GbDcDcState valOf(byte val) {
        return Arrays.stream(GbDcDcState.values())
                .filter(gbRunningMode -> gbRunningMode.code == val)
                .findFirst()
                .orElse(null);
    }

}
