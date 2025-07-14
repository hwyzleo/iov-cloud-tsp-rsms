package net.hwyz.iov.cloud.tsp.rsms.api.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 车辆上报状态枚举类
 *
 * @author hwyz_leo
 */
@Getter
@AllArgsConstructor
public enum VehicleReportState {

    /** 正常上报 **/
    NORMAL(0, "正常上报"),
    /** 维修保养 **/
    MAINTAIN(1, "维修保养");

    private final int code;
    private final String name;

    public static VehicleReportState valOf(int val) {
        return Arrays.stream(VehicleReportState.values())
                .filter(vehicleReportState -> vehicleReportState.code == val)
                .findFirst()
                .orElse(null);
    }

}
