package net.hwyz.iov.cloud.tsp.rsms.api.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 协议类型枚举类
 *
 * @author hwyz_leo
 */
@Getter
@AllArgsConstructor
public enum ProtocolType {

    /** 国标 **/
    GB("gb");

    private final String code;

    public static ProtocolType valOf(String val) {
        return Arrays.stream(ProtocolType.values())
                .filter(protocolType -> protocolType.code.equals(val))
                .findFirst()
                .orElse(null);
    }

}
