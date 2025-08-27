package net.hwyz.iov.cloud.tsp.rsms.api.contract.dataunit;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessageDataUnit;
import net.hwyz.iov.cloud.tsp.rsms.api.util.GbUtil;

import java.util.Arrays;

/**
 * 国标车辆登录数据单元
 *
 * @author hwyz_leo
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GbVehicleLoginDataUnit extends GbMessageDataUnit {

    /**
     * 登入流水号
     */
    private int loginSn;
    /**
     * ICCID
     */
    private String iccid;
    /**
     * 可充电储能子系统数
     */
    private byte deviceCount;
    /**
     * 可充电储能子系统编码长度
     */
    private byte deviceCodeLength;
    /**
     * 可充电储能系统编码
     */
    private String deviceCode;

    @Override
    public void parse(byte[] dataUnitBytes) {
        this.messageTime = GbUtil.dateTimeBytesToDate(Arrays.copyOfRange(dataUnitBytes, 0, 6));
        this.loginSn = GbUtil.bytesToWord(Arrays.copyOfRange(dataUnitBytes, 6, 8));
        this.iccid = GbUtil.bytesToString(Arrays.copyOfRange(dataUnitBytes, 8, 28));
        this.deviceCount = dataUnitBytes[28];
        this.deviceCodeLength = dataUnitBytes[29];
        this.deviceCode = GbUtil.bytesToString(Arrays.copyOfRange(dataUnitBytes, 30, 30 + this.deviceCount * this.deviceCodeLength));
    }

    @Override
    public byte[] toByteArray() {
        return ArrayUtil.addAll(GbUtil.getGbDateTimeBytes(this.messageTime.getTime()), GbUtil.wordToBytes(this.loginSn),
                GbUtil.stringToBytes(this.iccid, 20), new byte[]{this.deviceCount}, new byte[]{this.deviceCodeLength},
                GbUtil.stringToBytes(this.deviceCode, this.deviceCount * this.deviceCodeLength));
    }
}
