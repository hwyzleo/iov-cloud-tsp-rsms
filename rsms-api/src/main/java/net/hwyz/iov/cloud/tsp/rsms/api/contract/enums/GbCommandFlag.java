package net.hwyz.iov.cloud.tsp.rsms.api.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 国标命令标识枚举类
 *
 * @author hwyz_leo
 */
@Getter
@AllArgsConstructor
public enum GbCommandFlag {

    /** 车辆登入 **/
    VEHICLE_LOGIN((byte) 0x01, CommandFlag.VEHICLE_LOGIN),
    /** 实时信息上报 **/
    REALTIME_REPORT((byte) 0x02, CommandFlag.REALTIME_REPORT),
    /** 补发信息上报 **/
    REISSUE_REPORT((byte) 0x03, CommandFlag.REISSUE_REPORT),
    /** 车辆登出 **/
    VEHICLE_LOGOUT((byte) 0x04, CommandFlag.VEHICLE_LOGOUT),
    /** 平台登入 **/
    PLATFORM_LOGIN((byte) 0x05, CommandFlag.PLATFORM_LOGIN),
    /** 平台登出 **/
    PLATFORM_LOGOUT((byte) 0x06, CommandFlag.PLATFORM_LOGOUT);

    private final byte code;

    private final CommandFlag commandFlag;

    public static GbCommandFlag valOf(byte val) {
        return Arrays.stream(GbCommandFlag.values())
                .filter(gbCommandFlag -> gbCommandFlag.code == val)
                .findFirst()
                .orElse(null);
    }

}
