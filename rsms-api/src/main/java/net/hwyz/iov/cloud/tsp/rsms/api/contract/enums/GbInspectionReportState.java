package net.hwyz.iov.cloud.tsp.rsms.api.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 国标检测报告状态枚举类
 *
 * @author hwyz_leo
 */
@Getter
@AllArgsConstructor
public enum GbInspectionReportState {

    PROCESSING(1, "处理中"),
    COMPLETED(2, "处理结束");

    private final int code;
    private final String name;

    public static GbInspectionReportState valOf(int val) {
        return Arrays.stream(GbInspectionReportState.values())
                .filter(gbInspectionReportState -> gbInspectionReportState.code == val)
                .findFirst()
                .orElse(null);
    }

}
