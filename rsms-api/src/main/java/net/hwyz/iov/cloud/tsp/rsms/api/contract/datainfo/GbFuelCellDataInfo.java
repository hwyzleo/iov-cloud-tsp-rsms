package net.hwyz.iov.cloud.tsp.rsms.api.contract.datainfo;

import cn.hutool.core.util.ArrayUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessageDataInfo;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbDataInfoType;
import net.hwyz.iov.cloud.tsp.rsms.api.util.GbUtil;

import java.util.Arrays;

/**
 * 国标燃料电池数据信息
 *
 * @author hwyz_leo
 */
@Data
@Slf4j
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GbFuelCellDataInfo extends GbMessageDataInfo {

    /**
     * 数据信息类型
     */
    private GbDataInfoType dataInfoType;
    /**
     * 燃料电池电压
     * 有效值范围：0~20000（表示0V~2000V),最小计量单元：0.1V,“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private int voltage;
    /**
     * 燃料电池电流
     * 有效值范围：0~20000（表示0A~+2000A),最小计量单元：0.1A,“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private int current;
    /**
     * 燃料消耗率
     * 有效值范围：0~60000（表示0kg/100km~600kg/100km),最小计量单元：0.01kg/100km,“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private int consumptionRate;
    /**
     * 燃料电池温度探针总数
     * N个燃料电池温度探针，有效值范围：0~65531，“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private int temperatureProbeCount;
    /**
     * 探针温度值
     * 有效值范圃：0~240（数值偏移量40℃，表示-40℃ ~ +200℃)，最小计量单元：1℃
     */
    private byte[] probeTemperature;
    /**
     * 氢系统中最高温度
     * 有效值范圃：0~2400（偏移量40℃，表示一40℃~200℃），最小计量单元：0.1℃，“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private int hydrogenSystemMaxTemperature;
    /**
     * 氢系统中最高温度探针代号
     * 有效值范围：1~252，“0xFE”表示异常，“0xFF”表示无效
     */
    private byte hydrogenSystemMaxTemperatureProbe;
    /**
     * 氢气最高浓度
     * 有效值范围：0~60000（表示0mg/kg~50000mg/kg),最小计量单元：1mg/kg,“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private int hydrogenMaxConcentration;
    /**
     * 氢气最高浓度传感器代号
     * 有效值范围：1~252，“0xFE”表示异常，“0xFF”表示无效
     */
    private byte hydrogenMaxConcentrationSensor;
    /**
     * 氢气最高压力
     */
    private int hydrogenMaxPressure;
    /**
     * 氢气最高压力传感器代号
     * 有效值范围：1~252，“0xFE”表示异常，“0xFF”表示无效
     */
    private byte hydrogenMaxPressureSensor;
    /**
     * 高压DCDC状态
     * 0x01：工作；0x02：断开；“0xFE”表示异常，“0xFF”表示无效
     */
    private byte highPressureDcdcState;

    @Override
    public int parse(byte[] dataInfoBytes) {
        if (dataInfoBytes == null || dataInfoBytes.length == 0) {
            return 0;
        }
        this.dataInfoType = GbDataInfoType.FUEL_CELL;
        this.voltage = GbUtil.bytesToWord(Arrays.copyOfRange(dataInfoBytes, 0, 2));
        this.current = GbUtil.bytesToWord(Arrays.copyOfRange(dataInfoBytes, 2, 4));
        this.consumptionRate = GbUtil.bytesToWord(Arrays.copyOfRange(dataInfoBytes, 4, 6));
        this.temperatureProbeCount = GbUtil.bytesToWord(Arrays.copyOfRange(dataInfoBytes, 6, 8));
        this.probeTemperature = Arrays.copyOfRange(dataInfoBytes, 8, 8 + temperatureProbeCount);
        this.hydrogenSystemMaxTemperature = GbUtil.bytesToWord(Arrays.copyOfRange(dataInfoBytes, 8 + temperatureProbeCount, 10 + temperatureProbeCount));
        this.hydrogenSystemMaxTemperatureProbe = dataInfoBytes[10 + temperatureProbeCount];
        this.hydrogenMaxConcentration = GbUtil.bytesToWord(Arrays.copyOfRange(dataInfoBytes, 11 + temperatureProbeCount, 13 + temperatureProbeCount));
        this.hydrogenMaxConcentrationSensor = dataInfoBytes[14 + temperatureProbeCount];
        this.hydrogenMaxPressure = GbUtil.bytesToWord(Arrays.copyOfRange(dataInfoBytes, 15 + temperatureProbeCount, 17 + temperatureProbeCount));
        this.hydrogenMaxPressureSensor = dataInfoBytes[18 + temperatureProbeCount];
        this.highPressureDcdcState = dataInfoBytes[19 + temperatureProbeCount];
        return 18 + this.temperatureProbeCount;
    }

    @Override
    public byte[] toByteArray() {
        return ArrayUtil.addAll(new byte[]{this.dataInfoType.getCode()}, GbUtil.wordToBytes(this.voltage),
                GbUtil.wordToBytes(this.current), GbUtil.wordToBytes(this.consumptionRate), GbUtil.wordToBytes(this.temperatureProbeCount),
                this.probeTemperature, GbUtil.wordToBytes(this.hydrogenSystemMaxTemperature),
                new byte[]{this.hydrogenSystemMaxTemperatureProbe}, GbUtil.wordToBytes(this.hydrogenMaxConcentration),
                new byte[]{this.hydrogenMaxConcentrationSensor}, GbUtil.wordToBytes(this.hydrogenMaxPressure),
                new byte[]{this.hydrogenMaxPressureSensor}, new byte[]{this.highPressureDcdcState});
    }
}
