package net.hwyz.iov.cloud.tsp.rsms.api.contract.datainfo;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessageDataInfo;
import net.hwyz.iov.cloud.tsp.rsms.api.util.GbUtil;

import java.util.Arrays;

/**
 * 国标单个驱动电机数据信息
 *
 * @author hwyz_leo
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GbSingleDriveMotorDataInfo extends GbMessageDataInfo {

    /**
     * 驱动电机顺序号
     * 有效值范圃1~253
     */
    private byte sn;
    /**
     * 驱动电机状态
     * 0x01:耗电；0x02,发电；0x03:关闭状态，0x04:准备状态
     * “0xFE”表示异常，“0xFF”表示无效
     */
    private byte state;
    /**
     * 驱动电机控制器温度
     * 有效值范圃：0~250（数值偏移量40℃，表示-40℃ ~ +210℃)，最小计量单元：1℃，“0xFE”表示异常，“0xFF”表示无效
     */
    private byte controllerTemperature;
    /**
     * 驱动电机转速
     * 有效值范围：0~65531（数值偏移量20000表示-20000 r/min ~ 45531 r/min),最小计量单元：1r/min,
     * “0xFF，0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private int speed;
    /**
     * 驱动电机转矩
     * 有效值范围：0~65531（数值偏移量20000表示-2000 N·m ~ 45531 N·m),最小计量单元：0.1 N·m,
     * “0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private int torque;
    /**
     * 驱动电机温度
     * 有效值范围：0~250（数值偏移量40℃，表示-40 ℃ ~ +210 ℃)，最小计量单元：1℃，“0xFE”表示异常，“0xFF”表示无效
     */
    private byte temperature;
    /**
     * 电机控制器输入电压
     * 有效值范围：0~60000（表示0V6000V),最小计量单元：0.1V,“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private int controllerInputVoltage;
    /**
     * 电机控制器直流母线电流
     * 有效值范围：0~20000（数值偏移量1000A,表示-1000A ~ +1000A),最小计量单元：0.1A,“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private int controllerDcBusCurrent;

    @Override
    public int getLength() {
        return 12;
    }

    @Override
    public void parse(byte[] dataInfoBytes) {
        if (dataInfoBytes == null || dataInfoBytes.length != getLength()) {
            logger.warn("国标单个驱动电机数据信息[{}]异常", Arrays.toString(dataInfoBytes));
            return;
        }
        this.sn = dataInfoBytes[0];
        this.state = dataInfoBytes[1];
        this.controllerTemperature = dataInfoBytes[2];
        this.speed = GbUtil.bytesToWord(Arrays.copyOfRange(dataInfoBytes, 3, 5));
        this.torque = GbUtil.bytesToDword(Arrays.copyOfRange(dataInfoBytes, 5, 7));
        this.temperature = dataInfoBytes[7];
        this.controllerInputVoltage = GbUtil.bytesToWord(Arrays.copyOfRange(dataInfoBytes, 8, 10));
        this.controllerDcBusCurrent = GbUtil.bytesToWord(Arrays.copyOfRange(dataInfoBytes, 10, 12));
    }

    @Override
    public byte[] toByteArray() {
        return ArrayUtil.addAll(new byte[]{this.sn}, new byte[]{this.state}, new byte[]{this.controllerTemperature},
                GbUtil.wordToBytes(this.speed), GbUtil.wordToBytes(this.torque), new byte[]{this.temperature},
                GbUtil.wordToBytes(this.controllerInputVoltage), GbUtil.wordToBytes(this.controllerDcBusCurrent));
    }
}
