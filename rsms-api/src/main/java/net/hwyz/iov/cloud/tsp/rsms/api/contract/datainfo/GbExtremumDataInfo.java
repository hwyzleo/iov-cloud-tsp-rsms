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
 * 国标极值数据数据信息
 *
 * @author hwyz_leo
 */
@Data
@Slf4j
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GbExtremumDataInfo extends GbMessageDataInfo {

    /**
     * 数据信息类型
     */
    private GbDataInfoType dataInfoType;
    /**
     * 最高电压电池子系统号
     * 有效值范围：1~250，“0xFE”表示异常，“0xFF”表示无效
     */
    private byte maxVoltageBatteryDeviceNo;
    /**
     * 最高电压电池单体代号
     * 有效值范围：1~250，“0xFE”表示异常，“0xFF”表示无效
     */
    private byte maxVoltageCellNo;
    /**
     * 电池单体电压最高值
     * 有效值范围：0~15000（表示0V~15V),最小计量单元：0.001V,“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private int cellMaxVoltage;
    /**
     * 最低电压电池子系统号
     * 有效值范围：1~250，“0xFE”表示异常，“0xFF”表示无效
     */
    private byte minVoltageBatteryDeviceNo;
    /**
     * 最低电压电池单体代号
     * 有效值范围：1~250，“0xFE”表示异常，“0xFF”表示无效
     */
    private byte minVoltageCellNo;
    /**
     * 电池单体电压最低值
     * 有效值范围：0~15000（表示0V~15V),最小计量单元：0.001V,“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private int cellMinVoltage;
    /**
     * 最高温度子系统号
     * 有效值范围：1~250，“0xFE”表示异常，“0xFF”表示无效
     */
    private byte maxTemperatureDeviceNo;
    /**
     * 最高温度探针序号
     * 有效值范圃：1~250，“0xFE”表示异常，“0xFF”表示无效
     */
    private byte maxTemperatureProbeNo;
    /**
     * 最高温度值
     * 有效值范围：0一250（数值偏移量40℃，表示-40℃ ~ +210℃)，最小计量单元：1℃，“0xFE”表示异常，“0xFF”表示无效
     */
    private byte maxTemperature;
    /**
     * 最低温度子系统号
     * 有效值范围：1~250，“0xFE”表示异常，“0xFF”表示无效
     */
    private byte minTemperatureDeviceNo;
    /**
     * 最低温度探针序号
     * 有效值范圃：1~250，“0xFE”表示异常，“0xFF”表示无效
     */
    private byte minTemperatureProbeNo;
    /**
     * 最低温度值
     * 有效值范围：0一250（数值偏移量40℃，表示-40℃ ~ +210℃)，最小计量单元：1℃，“0xFE”表示异常，“0xFF”表示无效
     */
    private byte minTemperature;

    @Override
    public int parse(byte[] dataInfoBytes) {
        if (dataInfoBytes == null || dataInfoBytes.length == 0) {
            return 0;
        }
        this.dataInfoType = GbDataInfoType.EXTREMUM;
        this.maxVoltageBatteryDeviceNo = dataInfoBytes[0];
        this.maxVoltageCellNo = dataInfoBytes[1];
        this.cellMaxVoltage = GbUtil.bytesToWord(Arrays.copyOfRange(dataInfoBytes, 2, 4));
        this.minVoltageBatteryDeviceNo = dataInfoBytes[4];
        this.minVoltageCellNo = dataInfoBytes[5];
        this.cellMinVoltage = GbUtil.bytesToWord(Arrays.copyOfRange(dataInfoBytes, 6, 8));
        this.maxTemperatureDeviceNo = dataInfoBytes[8];
        this.maxTemperatureProbeNo = dataInfoBytes[9];
        this.maxTemperature = dataInfoBytes[10];
        this.minTemperatureDeviceNo = dataInfoBytes[11];
        this.minTemperatureProbeNo = dataInfoBytes[12];
        this.minTemperature = dataInfoBytes[13];
        return 14;
    }

    @Override
    public byte[] toByteArray() {
        return ArrayUtil.addAll(new byte[]{this.dataInfoType.getCode()}, new byte[]{this.maxVoltageBatteryDeviceNo},
                new byte[]{this.maxVoltageCellNo}, GbUtil.wordToBytes(this.cellMaxVoltage),
                new byte[]{this.minVoltageBatteryDeviceNo}, new byte[]{this.minVoltageCellNo},
                GbUtil.wordToBytes(this.cellMinVoltage), new byte[]{this.maxTemperatureDeviceNo},
                new byte[]{this.maxTemperatureProbeNo}, new byte[]{this.maxTemperature},
                new byte[]{this.minTemperatureDeviceNo}, new byte[]{this.minTemperatureProbeNo},
                new byte[]{this.minTemperature});
    }
}
