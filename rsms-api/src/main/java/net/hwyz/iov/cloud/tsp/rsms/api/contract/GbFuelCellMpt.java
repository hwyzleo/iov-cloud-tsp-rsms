package net.hwyz.iov.cloud.tsp.rsms.api.contract;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;

/**
 * 管理后台国标燃料电池数据信息
 *
 * @author hwyz_leo
 */
@Data
@Slf4j
@NoArgsConstructor
public class GbFuelCellMpt {

    /**
     * 燃料电池电压
     * 有效值范围：0 ~ 20000（表示0V ~ 2000V),最小计量单元：0.1V,“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private BigDecimal voltage;
    /**
     * 燃料电池电流
     * 有效值范围：0 ~ 20000（表示0A ~ +2000A),最小计量单元：0.1A,“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private BigDecimal current;
    /**
     * 燃料消耗率
     * 有效值范围：0 ~ 60000（表示0kg/100km ~ 600kg/100km),最小计量单元：0.01kg/100km,“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private BigDecimal consumptionRate;
    /**
     * 燃料电池温度探针总数
     * N个燃料电池温度探针，有效值范围：0 ~ 65531，“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private Integer temperatureProbeCount;
    /**
     * 探针温度值
     * 有效值范圃：0 ~ 240（数值偏移量40℃，表示-40℃ ~ +200℃)，最小计量单元：1℃
     */
    private List<Integer> probeTemperature;
    /**
     * 氢系统中最高温度
     * 有效值范圃：0 ~ 2400（偏移量40℃，表示-40℃ ~ 200℃），最小计量单元：0.1℃，“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private BigDecimal hydrogenSystemMaxTemperature;
    /**
     * 氢系统中最高温度探针代号
     * 有效值范围：1 ~ 252，“0xFE”表示异常，“0xFF”表示无效
     */
    private Integer hydrogenSystemMaxTemperatureProbe;
    /**
     * 氢气最高浓度
     * 有效值范围：0 ~ 60000（表示0mg/kg ~ 50000mg/kg),最小计量单元：1mg/kg,“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private Integer hydrogenMaxConcentration;
    /**
     * 氢气最高浓度传感器代号
     * 有效值范围：1 ~ 252，“0xFE”表示异常，“0xFF”表示无效
     */
    private Integer hydrogenMaxConcentrationSensor;
    /**
     * 氢气最高压力
     * 有效值范围：0 ~ 1000（表示0MPa ~ 100MPa),最小计量单元：0.1MPa
     */
    private BigDecimal hydrogenMaxPressure;
    /**
     * 氢气最高压力传感器代号
     * 有效值范围：1 ~ 252，“0xFE”表示异常，“0xFF”表示无效
     */
    private Integer hydrogenMaxPressureSensor;
    /**
     * 高压DCDC状态
     */
    private String highPressureDcdcState;

}
