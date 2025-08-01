package net.hwyz.iov.cloud.tsp.rsms.api.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 国标检测报告场景枚举类
 *
 * @author hwyz_leo
 */
@Getter
@AllArgsConstructor
public enum GbInspectionReportScene {

    NONE(0, "不指定"),
    DRIVING(1, "车辆行驶"),
    CHARGING(2, "车辆充电"),
    ALARM(3, "车辆报警"),
    VEHICLE_REISSUE(4, "车辆补发"),
    PLATFORM_REISSUE(5, "平台补发");

    private final int code;
    private final String name;

    public static GbInspectionReportScene valOf(int val) {
        return Arrays.stream(GbInspectionReportScene.values())
                .filter(gbInspectionReportScene -> gbInspectionReportScene.code == val)
                .findFirst()
                .orElse(null);
    }

}
