package net.hwyz.iov.cloud.tsp.rsms.api.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 国标发动机状态枚举类
 *
 * @author hwyz_leo
 */
@Getter
@AllArgsConstructor
public enum GbEngineState {

    /** 启动 **/
    START((byte) 0x01, "启动"),
    /** 关闭 **/
    STOP((byte) 0x02, "关闭"),
    /** 异常 **/
    ABNORMAL((byte) 0xFE, "异常"),
    /** 无效 **/
    INVALID((byte) 0xFF, "无效");

    private final byte code;
    private final String name;

    public static GbEngineState valOf(byte val) {
        return Arrays.stream(GbEngineState.values())
                .filter(gbEngineState -> gbEngineState.code == val)
                .findFirst()
                .orElse(null);
    }

}
