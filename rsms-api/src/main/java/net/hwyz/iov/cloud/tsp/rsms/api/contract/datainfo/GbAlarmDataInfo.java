package net.hwyz.iov.cloud.tsp.rsms.api.contract.datainfo;

import cn.hutool.core.util.ArrayUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessageDataInfo;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbAlarmLevel;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbDataInfoType;
import net.hwyz.iov.cloud.tsp.rsms.api.util.GbUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 国标报警数据数据信息
 *
 * @author hwyz_leo
 */
@Data
@Slf4j
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GbAlarmDataInfo extends GbMessageDataInfo {

    /**
     * 最高报警等级
     */
    private GbAlarmLevel maxAlarmLevel;

    /**
     * 通用报警标志
     */
    private Map<Integer, Boolean> alarmFlagMap;
    /**
     * 可充电储能装置故障总数N1
     * N1个可充电储能装置故障，有效值范围：0~252，“0xFE”表示异常，“0xFF”表示无效
     */
    private byte batteryFaultCount;
    /**
     * 可充电储能装置故障代码列表
     */
    private List<Integer> batteryFaultList;
    /**
     * 驱动电机故障总数N2
     * N2个驱动电机故障，有效值范围：0~252，“0xFE”表示异常，“0xFF”表示无效
     */
    private byte driveMotorFaultCount;
    /**
     * 驱动电机故障代码列表
     */
    private List<Integer> driveMotorFaultList;
    /**
     * 发动机故障总数N3
     * N3个发动机故障，有效值范围：0~252，“0xFE”表示异常，“0xFF”表示无效
     */
    private byte engineFaultCount;
    /**
     * 发动机故障代码列表
     */
    private List<Integer> engineFaultList;
    /**
     * 其他故障总数N4
     * N4个其他故障，有效值范围：0~252，“0xFE”表示异常，“0xFF”表示无效
     */
    private byte otherFaultCount;
    /**
     * 其他故障代码列表
     */
    private List<Integer> otherFaultList;

    @Override
    public int parse(byte[] dataInfoBytes) {
        if (dataInfoBytes == null || dataInfoBytes.length == 0) {
            return 0;
        }
        this.dataInfoType = GbDataInfoType.ALARM;
        this.maxAlarmLevel = GbAlarmLevel.valOf(dataInfoBytes[0]);
        this.alarmFlagMap = GbUtil.parseAlarmFlag(Arrays.copyOfRange(dataInfoBytes, 1, 5));
        this.batteryFaultCount = dataInfoBytes[5];
        this.batteryFaultList = new ArrayList<>();
        int startPos = 6;
        for (int i = 0; i < batteryFaultCount; i++) {
            this.batteryFaultList.add(GbUtil.bytesToDword(Arrays.copyOfRange(dataInfoBytes, startPos, startPos + 4)));
            startPos += 4;
        }
        this.driveMotorFaultCount = dataInfoBytes[startPos];
        startPos++;
        this.driveMotorFaultList = new ArrayList<>();
        for (int i = 0; i < driveMotorFaultCount; i++) {
            this.driveMotorFaultList.add(GbUtil.bytesToDword(Arrays.copyOfRange(dataInfoBytes, startPos, startPos + 4)));
            startPos += 4;
        }
        this.engineFaultCount = dataInfoBytes[startPos];
        startPos++;
        this.engineFaultList = new ArrayList<>();
        for (int i = 0; i < engineFaultCount; i++) {
            this.engineFaultList.add(GbUtil.bytesToDword(Arrays.copyOfRange(dataInfoBytes, startPos, startPos + 4)));
            startPos += 4;
        }
        this.otherFaultCount = dataInfoBytes[startPos];
        startPos++;
        this.otherFaultList = new ArrayList<>();
        for (int i = 0; i < otherFaultCount; i++) {
            this.otherFaultList.add(GbUtil.bytesToDword(Arrays.copyOfRange(dataInfoBytes, startPos, startPos + 4)));
            startPos += 4;
        }
        return startPos;
    }

    @Override
    public byte[] toByteArray() {
        byte[] bytes = ArrayUtil.addAll(new byte[]{this.dataInfoType.getCode()}, new byte[]{this.maxAlarmLevel.getCode()},
                GbUtil.packageAlarmFlag(this.alarmFlagMap), new byte[]{this.batteryFaultCount});
        for (Integer batteryFault : batteryFaultList) {
            bytes = ArrayUtil.addAll(bytes, GbUtil.dwordToBytes(batteryFault));
        }
        bytes = ArrayUtil.addAll(bytes, new byte[]{this.driveMotorFaultCount});
        for (Integer driveMotorFault : driveMotorFaultList) {
            bytes = ArrayUtil.addAll(bytes, GbUtil.dwordToBytes(driveMotorFault));
        }
        bytes = ArrayUtil.addAll(bytes, new byte[]{this.engineFaultCount});
        for (Integer engineFault : engineFaultList) {
            bytes = ArrayUtil.addAll(bytes, GbUtil.dwordToBytes(engineFault));
        }
        bytes = ArrayUtil.addAll(bytes, new byte[]{this.otherFaultCount});
        for (Integer otherFault : otherFaultList) {
            bytes = ArrayUtil.addAll(bytes, GbUtil.dwordToBytes(otherFault));
        }
        return bytes;
    }
}
