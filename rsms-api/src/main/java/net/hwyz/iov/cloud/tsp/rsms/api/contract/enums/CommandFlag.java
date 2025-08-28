package net.hwyz.iov.cloud.tsp.rsms.api.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 命令标识枚举类
 *
 * @author hwyz_leo
 */
@Getter
@AllArgsConstructor
public enum CommandFlag {

    VEHICLE_LOGIN("车辆登入"),
    REALTIME_REPORT("实时信息上报"),
    REISSUE_REPORT("补发信息上报"),
    VEHICLE_LOGOUT("车辆登出"),
    PLATFORM_LOGIN("平台登入"),
    PLATFORM_LOGOUT("平台登出");

    private final String label;

    public static CommandFlag valOf(String val) {
        return Arrays.stream(CommandFlag.values())
                .filter(commandFlag -> commandFlag.name().equals(val))
                .findFirst()
                .orElse(null);
    }

}
