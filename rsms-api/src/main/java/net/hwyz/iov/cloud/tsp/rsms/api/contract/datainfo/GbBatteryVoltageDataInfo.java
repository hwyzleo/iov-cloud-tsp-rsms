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
 * 国标可充电储能装置电压数据信息
 *
 * @author hwyz_leo
 */
@Data
@Slf4j
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GbBatteryVoltageDataInfo extends GbMessageDataInfo {

    /**
     * 可充电储能子系统个数
     * N个可充电储能子系统，有效值范围：1 ~ 250，“0xFE”表示异常，“0xFF”表示无效
     */
    private byte batteryCount;
    /**
     * 可充电储能子系统电压信息列表
     * 按可充电储能子系统序号依次排列
     */
    private LinkedList<GbSingleBatteryVoltageDataInfo> batteryVoltageList;

    @Override
    public int parse(byte[] dataInfoBytes) {
        if (dataInfoBytes == null || dataInfoBytes.length == 0) {
            return 0;
        }
        this.dataInfoType = GbDataInfoType.BATTERY_VOLTAGE;
        this.batteryCount = dataInfoBytes[0];
        this.batteryVoltageList = new LinkedList<>();
        int startPos = 1;
        for (int i = 0; i < batteryCount; i++) {
            GbSingleBatteryVoltageDataInfo singleBatteryVoltage = new GbSingleBatteryVoltageDataInfo();
            int length = singleBatteryVoltage.parse(Arrays.copyOfRange(dataInfoBytes, startPos, dataInfoBytes.length));
            batteryVoltageList.add(singleBatteryVoltage);
            startPos += length;
        }
        return startPos;
    }

    @Override
    public byte[] toByteArray() {
        byte[] bytes = ArrayUtil.addAll(new byte[]{this.dataInfoType.getCode()}, new byte[]{this.batteryCount});
        for (GbSingleBatteryVoltageDataInfo batteryVoltage : batteryVoltageList) {
            bytes = ArrayUtil.addAll(bytes, batteryVoltage.toByteArray());
        }
        return bytes;
    }
}
