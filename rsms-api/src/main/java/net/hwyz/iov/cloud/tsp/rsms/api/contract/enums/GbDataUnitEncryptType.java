package net.hwyz.iov.cloud.tsp.rsms.api.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 国标数据单元加密方式枚举类
 *
 * @author hwyz_leo
 */
@Getter
@AllArgsConstructor
public enum GbDataUnitEncryptType {

    /** 不加密 **/
    NONE((byte) 0x01),
    /** RSA加密 **/
    RSA((byte) 0x02),
    /** AES128加密 **/
    AES128((byte) 0x03),
    /** 异常 **/
    EXCEPTION((byte) 0xFE),
    /** 无效 **/
    INVALID((byte) 0xFF);

    private final byte code;

    public static GbDataUnitEncryptType valOf(byte val) {
        return Arrays.stream(GbDataUnitEncryptType.values())
                .filter(gbDataUnitEncryptType -> gbDataUnitEncryptType.code == val)
                .findFirst()
                .orElse(null);
    }

}
