package net.hwyz.iov.cloud.tsp.rsms.api.contract;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 管理后台国标可充电储能子系统温度数据信息
 *
 * @author hwyz_leo
 */
@Data
@Slf4j
@NoArgsConstructor
public class GbBatteryTemperatureMpt {

    /**
     * 可充电储能子系统号
     * 有效值范围:1~250
     */
    private Integer sn;
    /**
     * 可充电储能温度探针个数
     * N个温度探针，有效值范围：1~65531，“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private Integer probeCount;
    /**
     * 可充电储能子系统各温度探针检测到的温度值
     * 有效值范围：0 ~ 250（数值偏移量40℃，表示-40℃ ~ +210℃)，最小计量单元：1℃，“0xFE”表示异常，“0xFF”表示无效
     */
    private List<Integer> temperatures;

}
