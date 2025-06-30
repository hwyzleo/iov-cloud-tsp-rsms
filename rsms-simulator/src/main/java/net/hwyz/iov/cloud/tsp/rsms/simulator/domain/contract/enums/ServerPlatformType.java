package net.hwyz.iov.cloud.tsp.rsms.simulator.domain.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 服务端平台类型枚举类
 *
 * @author hwyz_leo
 */
@Getter
@AllArgsConstructor
public enum ServerPlatformType {

    /** 平台转发 **/
    PLATFORM_FORWARD(19006),
    /** 终端直连 **/
    VEHICLE_CONNECT(19007);

    private final int port;

    public static ServerPlatformType valOf(int port) {
        return Arrays.stream(ServerPlatformType.values())
                .filter(serverPlatformType -> serverPlatformType.port == port)
                .findFirst()
                .orElse(null);
    }

}
