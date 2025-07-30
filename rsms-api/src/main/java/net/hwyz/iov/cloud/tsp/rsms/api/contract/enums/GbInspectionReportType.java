package net.hwyz.iov.cloud.tsp.rsms.api.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 国标检测报告类型枚举类
 *
 * @author hwyz_leo
 */
@Getter
@AllArgsConstructor
public enum GbInspectionReportType {

    VEHICLE(1, "单车报告"),
    MODEL(2, "车型报告");

    private final int code;
    private final String name;

    public static GbInspectionReportType valOf(int val) {
        return Arrays.stream(GbInspectionReportType.values())
                .filter(gbInspectionReportType -> gbInspectionReportType.code == val)
                .findFirst()
                .orElse(null);
    }

}
