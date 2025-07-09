package net.hwyz.iov.cloud.tsp.rsms.api.contract;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * 管理后台国标极值数据数据信息
 *
 * @author hwyz_leo
 */
@Data
@Slf4j
@NoArgsConstructor
public class GbExtremumMpt {

    /**
     * 最高电压电池子系统号
     * 有效值范围：1 ~ 250，“0xFE”表示异常，“0xFF”表示无效
     */
    private Integer maxVoltageBatteryDeviceNo;
    /**
     * 最高电压电池单体代号
     * 有效值范围：1 ~ 250，“0xFE”表示异常，“0xFF”表示无效
     */
    private Integer maxVoltageCellNo;
    /**
     * 电池单体电压最高值
     * 有效值范围：0 ~ 15000（表示0V ~ 15V),最小计量单元：0.001V,“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private BigDecimal cellMaxVoltage;
    /**
     * 最低电压电池子系统号
     * 有效值范围：1 ~ 250，“0xFE”表示异常，“0xFF”表示无效
     */
    private Integer minVoltageBatteryDeviceNo;
    /**
     * 最低电压电池单体代号
     * 有效值范围：1 ~ 250，“0xFE”表示异常，“0xFF”表示无效
     */
    private Integer minVoltageCellNo;
    /**
     * 电池单体电压最低值
     * 有效值范围：0 ~ 15000（表示0V ~ 15V),最小计量单元：0.001V,“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private BigDecimal cellMinVoltage;
    /**
     * 最高温度子系统号
     * 有效值范围：1 ~ 250，“0xFE”表示异常，“0xFF”表示无效
     */
    private Integer maxTemperatureDeviceNo;
    /**
     * 最高温度探针序号
     * 有效值范圃：1 ~ 250，“0xFE”表示异常，“0xFF”表示无效
     */
    private Integer maxTemperatureProbeNo;
    /**
     * 最高温度值
     * 有效值范围：0 ~ 250（数值偏移量40℃，表示-40℃ ~ +210℃)，最小计量单元：1℃，“0xFE”表示异常，“0xFF”表示无效
     */
    private Integer maxTemperature;
    /**
     * 最低温度子系统号
     * 有效值范围：1 ~ 250，“0xFE”表示异常，“0xFF”表示无效
     */
    private Integer minTemperatureDeviceNo;
    /**
     * 最低温度探针序号
     * 有效值范圃：1 ~ 250，“0xFE”表示异常，“0xFF”表示无效
     */
    private Integer minTemperatureProbeNo;
    /**
     * 最低温度值
     * 有效值范围：0 ~ 250（数值偏移量40℃，表示-40℃ ~ +210℃)，最小计量单元：1℃，“0xFE”表示异常，“0xFF”表示无效
     */
    private Integer minTemperature;

}
