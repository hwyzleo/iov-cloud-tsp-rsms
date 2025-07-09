package net.hwyz.iov.cloud.tsp.rsms.api.contract.datainfo;

import cn.hutool.core.util.ArrayUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessageDataInfo;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.*;
import net.hwyz.iov.cloud.tsp.rsms.api.util.GbUtil;

import java.util.Arrays;

/**
 * 国标整车数据数据信息
 *
 * @author hwyz_leo
 */
@Data
@Slf4j
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GbVehicleDataDataInfo extends GbMessageDataInfo {

    /**
     * 车辆状态
     */
    private GbVehicleState vehicleState;
    /**
     * 充电状态
     */
    private GbChargingState chargingState;
    /**
     * 运行模式
     */
    private GbRunningMode runningMode;
    /**
     * 车速
     * 有效值范围：0 ~ 2200（表示0km/h ~ 220km/h),最小计量单元：0.1km/h,“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private int speed;
    /**
     * 累计里程
     * 有效值范围：0 ~ 9999999（表示0km ~ 999999.9km),最小计量单元：0.1km。
     * “0xFF,0xFF,0xFF,0xFE”表示异常，“0xFF,0xFF,0xFF,0xFF”表示无效
     */
    private int totalOdometer;
    /**
     * 总电压
     * 有效值范围：0 ~ 10000（表示0V ~ 1000V),最小计量单元：0.1V,“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private int totalVoltage;
    /**
     * 总电流
     * 有效值范围：0 ~ 20000（偏移量1000A,表示-1000A ~ +1000A),最小计量单元：0.1A,“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private int totalCurrent;
    /**
     * SOC
     * 有效值范圃：0 ~ 100（表示0% ~ 100%），最小计量单元：1%，“0xFE”表示异常，“OxFF”表示无效
     */
    private byte soc;
    /**
     * DC/DC状态
     */
    private GbDcDcState dcdcState;
    /**
     * 有驱动力
     */
    private Boolean driving;
    /**
     * 有制动力
     */
    private Boolean braking;
    /**
     * 挡位
     */
    private GbGear gear;
    /**
     * 绝缘电阻
     * 有效范围 0 ~ 60000（表示 0 kΩ ~ 60000 kΩ),最小计量单元：1 kΩ
     */
    private int insulationResistance;
    /**
     * 加速踏板行程值
     * 有效值范围：0 ~ 100（表示0% ~ 100%），最小计量单元：1%，“0xFE”表示异常，“0xFF”表示无效
     */
    private byte acceleratorPedalPosition;
    /**
     * 制动踏板状态
     * 有效值范围：0 ~ 100（表示0% ~ 100%），最小计量单元：1%，“0”表示制动关的状态，
     * 在无具体行程值情祝下，用“0x65”即“101”表示制动有效状态，“0xFE”表示异常，“0xFF”表示无效
     */
    private byte brakePedalPosition;

    @Override
    public int parse(byte[] dataInfoBytes) {
        if (dataInfoBytes == null || dataInfoBytes.length == 0) {
            return 0;
        }
        this.dataInfoType = GbDataInfoType.VEHICLE;
        this.vehicleState = GbVehicleState.valOf(dataInfoBytes[0]);
        this.chargingState = GbChargingState.valOf(dataInfoBytes[1]);
        this.runningMode = GbRunningMode.valOf(dataInfoBytes[2]);
        this.speed = GbUtil.bytesToWord(Arrays.copyOfRange(dataInfoBytes, 3, 5));
        this.totalOdometer = GbUtil.bytesToDword(Arrays.copyOfRange(dataInfoBytes, 5, 9));
        this.totalVoltage = GbUtil.bytesToWord(Arrays.copyOfRange(dataInfoBytes, 9, 11));
        this.totalCurrent = GbUtil.bytesToWord(Arrays.copyOfRange(dataInfoBytes, 11, 13));
        this.soc = dataInfoBytes[13];
        this.dcdcState = GbDcDcState.valOf(dataInfoBytes[14]);
        this.driving = GbUtil.isDriving(dataInfoBytes[15]);
        this.braking = GbUtil.isBraking(dataInfoBytes[15]);
        this.gear = GbUtil.getGear(dataInfoBytes[15]);
        this.insulationResistance = GbUtil.bytesToWord(Arrays.copyOfRange(dataInfoBytes, 16, 18));
        this.acceleratorPedalPosition = dataInfoBytes[18];
        this.brakePedalPosition = dataInfoBytes[19];
        return 20;
    }

    @Override
    public byte[] toByteArray() {
        return ArrayUtil.addAll(new byte[]{this.dataInfoType.getCode()}, new byte[]{this.vehicleState.getCode()},
                new byte[]{this.chargingState.getCode()}, new byte[]{this.runningMode.getCode()}, GbUtil.wordToBytes(this.speed),
                GbUtil.dwordToBytes(this.totalOdometer), GbUtil.wordToBytes(this.totalVoltage),
                GbUtil.wordToBytes(this.totalCurrent), new byte[]{this.soc}, new byte[]{this.dcdcState.getCode()},
                new byte[]{GbUtil.combineGearByte(this.driving, this.braking, this.gear)}, GbUtil.wordToBytes(this.insulationResistance),
                new byte[]{this.acceleratorPedalPosition}, new byte[]{this.brakePedalPosition});
    }
}
