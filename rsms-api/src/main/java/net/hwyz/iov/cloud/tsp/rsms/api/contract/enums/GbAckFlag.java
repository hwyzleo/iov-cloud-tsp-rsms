package net.hwyz.iov.cloud.tsp.rsms.api.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 国标应答标志枚举类
 *
 * @author hwyz_leo
 */
@Getter
@AllArgsConstructor
public enum GbAckFlag {

    /** 成功 **/
    SUCCESS((byte) 0x01),
    /** 错误 **/
    FAILURE((byte) 0x02),
    /** VIN重复 **/
    VIN_DUPLICATE((byte) 0x03),
    /** 命令 **/
    COMMAND((byte) 0xFE);

    private final byte code;

    public static GbAckFlag valOf(byte val) {
        return Arrays.stream(GbAckFlag.values())
                .filter(gbAckFlag -> gbAckFlag.code == val)
                .findFirst()
                .orElse(null);
    }

}
