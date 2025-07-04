package net.hwyz.iov.cloud.tsp.rsms.api.contract.datainfo;

import cn.hutool.core.util.ArrayUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessageDataInfo;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbDataInfoType;
import net.hwyz.iov.cloud.tsp.rsms.api.util.GbUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
     * 数据信息类型
     */
    private GbDataInfoType dataInfoType;
    /**
     * 最高报警等级
     */
    private byte maxAlarmLevel;
    /**
     * 通用报警标志
     * 位0：1：温度差异报警；0：正常
     * 位1：1：电池高温报警；0：正常
     * 位2：1：车载储能装置类型过压报警；0：正常
     * 位3：1：车载储能装置类型欠压报警；0：正常
     * 位4：1：SOC低报警；0：正常
     * 位5：1：单体电池过压报警；0：正常
     * 位6：1：单体电池欠压报警；0：正常
     * 位7：1：SOC过高报警；0：正常
     * 位8：1：SOC跳变报警；0：正常
     * 位9：1：可充电储能系统不匹配报警；0：正常
     * 位10：1：电池单体一致性差报警；0：正常
     * 位11：1：绝缘报警；0：正常
     * 位12：1：DC-DC温度报警；0：正常
     * 位13：1：制动系统报警；0：正常
     * 位14：1：DC-DC状态报警；0：正常
     * 位15：1：驱动电机控制器温度报警；0：正常
     * 位16：1：高压互锁状态报警；0：正常
     * 位17：1：驱动电机温度报警；0：正常
     * 位18：1：车载储能装置类型过充；0：正常
     */
    private int alarmFlag;
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
        this.maxAlarmLevel = dataInfoBytes[0];
        this.alarmFlag = GbUtil.bytesToDword(Arrays.copyOfRange(dataInfoBytes, 1, 5));
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
        byte[] bytes = ArrayUtil.addAll(new byte[]{this.dataInfoType.getCode()}, new byte[]{this.maxAlarmLevel},
                GbUtil.dwordToBytes(this.alarmFlag), new byte[]{this.batteryFaultCount});
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
