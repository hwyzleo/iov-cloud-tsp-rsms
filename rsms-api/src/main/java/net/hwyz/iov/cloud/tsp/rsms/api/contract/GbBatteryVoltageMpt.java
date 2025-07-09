package net.hwyz.iov.cloud.tsp.rsms.api.contract;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;

/**
 * 管理后台国标可充电储能子系统电压数据信息
 *
 * @author hwyz_leo
 */
@Data
@Slf4j
@NoArgsConstructor
public class GbBatteryVoltageMpt {

    /**
     * 可充电储能子系统号
     * 有效值范围:1~250
     */
    private Integer sn;
    /**
     * 可充电储能装置电压
     * 有效值范围：0~10000（表示0V~1000V),最小计量单元：0.1V,“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private BigDecimal voltage;
    /**
     * 可充电储能装置电流
     * 有效值范围：0 ~ 20000（数值偏移量1000A,表示-1000A ~ +1000A),最小计量单元：0.1A,
     * “0xFF,0xFE”表示异常，“0xFF,0xFF#表示无效
     */
    private BigDecimal current;
    /**
     * 单体电池总数
     * N个电池单体，有效值范圃：1 ~ 65531，“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private Integer cellCount;
    /**
     * 本帧起始电池序号
     * 当本帧单体个数超过200时，应标分成多帧数据进行传输，有效值范围：1~65531
     */
    private Integer frameStartCellSn;
    /**
     * 本帧单体电池总数
     * 本帧单体总数m;有效值范围，1~200
     */
    private Integer frameCellCount;
    /**
     * 单体电池电压列表
     * 有效值范围：0~60000（表示0V~60.000V),最小计量单元：0.001V,单体电池电压个数等于本帧单体电池总数m,
     * “0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private List<BigDecimal> cellVoltageList;

}
