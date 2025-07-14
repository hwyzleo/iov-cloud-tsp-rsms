package net.hwyz.iov.cloud.tsp.rsms.api.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 客户端平台指令枚举类
 *
 * @author hwyz_leo
 */
@Getter
@AllArgsConstructor
public enum ClientPlatformCmd {

    /** 平台登入 **/
    LOGIN,
    /** 平台登出 **/
    LOGOUT,
    /** 同步车辆 **/
    SYNC_VEHICLE;

    public static ClientPlatformCmd valOf(String val) {
        return Arrays.stream(ClientPlatformCmd.values())
                .filter(clientPlatformCmd -> clientPlatformCmd.name().equals(val))
                .findFirst()
                .orElse(null);
    }

}
