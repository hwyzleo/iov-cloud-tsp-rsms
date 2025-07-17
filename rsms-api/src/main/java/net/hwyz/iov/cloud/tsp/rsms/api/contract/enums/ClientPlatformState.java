package net.hwyz.iov.cloud.tsp.rsms.api.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 客户端平台状态枚举类
 *
 * @author hwyz_leo
 */
@Getter
@AllArgsConstructor
public enum ClientPlatformState {

    /** 平台连接成功 **/
    CONNECT_SUCCESS,
    /** 平台连接失败 **/
    CONNECT_FAILURE,
    /** 平台登入成功 **/
    LOGIN_SUCCESS,
    /** 平台登入失败 **/
    LOGIN_FAILURE,
    /** 平台登出成功 **/
    LOGOUT_SUCCESS;

    public static ClientPlatformState valOf(String val) {
        return Arrays.stream(ClientPlatformState.values())
                .filter(clientPlatformState -> clientPlatformState.name().equals(val))
                .findFirst()
                .orElse(null);
    }

}
