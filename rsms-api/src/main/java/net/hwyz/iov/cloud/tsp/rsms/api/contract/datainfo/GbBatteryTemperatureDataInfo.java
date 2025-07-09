package net.hwyz.iov.cloud.tsp.rsms.api.contract.datainfo;

import cn.hutool.core.util.ArrayUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessageDataInfo;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbDataInfoType;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * 国标可充电储能装置温度数据信息
 *
 * @author hwyz_leo
 */
@Data
@Slf4j
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GbBatteryTemperatureDataInfo extends GbMessageDataInfo {

    /**
     * 可充电储能子系统个数
     * N个可充电储能子系统，有效值范围：1 ~ 250，“0xFE”表示异常，“0xFF”表示无效
     */
    private byte batteryCount;
    /**
     * 可充电储能子系统温度信息列表
     * 按可充电储能子系统序号依次排列
     */
    private LinkedList<GbSingleBatteryTemperatureDataInfo> batteryTemperatureList;

    @Override
    public int parse(byte[] dataInfoBytes) {
        if (dataInfoBytes == null || dataInfoBytes.length == 0) {
            return 0;
        }
        this.dataInfoType = GbDataInfoType.BATTERY_TEMPERATURE;
        this.batteryCount = dataInfoBytes[0];
        this.batteryTemperatureList = new LinkedList<>();
        int startPos = 1;
        for (int i = 0; i < batteryCount; i++) {
            GbSingleBatteryTemperatureDataInfo singleBatteryTemperature = new GbSingleBatteryTemperatureDataInfo();
            int length = singleBatteryTemperature.parse(Arrays.copyOfRange(dataInfoBytes, startPos, dataInfoBytes.length));
            batteryTemperatureList.add(singleBatteryTemperature);
            startPos += length;
        }
        return startPos;
    }

    @Override
    public byte[] toByteArray() {
        byte[] bytes = ArrayUtil.addAll(new byte[]{this.dataInfoType.getCode()}, new byte[]{this.batteryCount});
        for (GbSingleBatteryTemperatureDataInfo batteryTemperature : batteryTemperatureList) {
            bytes = ArrayUtil.addAll(bytes, batteryTemperature.toByteArray());
        }
        return bytes;
    }
}
