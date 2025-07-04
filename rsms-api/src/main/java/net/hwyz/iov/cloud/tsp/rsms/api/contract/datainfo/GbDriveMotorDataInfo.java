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
 * 国标驱动电机数据信息
 *
 * @author hwyz_leo
 */
@Data
@Slf4j
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GbDriveMotorDataInfo extends GbMessageDataInfo {

    /**
     * 数据信息类型
     */
    private GbDataInfoType dataInfoType;
    /**
     * 驱动电机个数
     */
    private byte driveMotorCount;
    /**
     * 驱动电机总成信息列表
     * 按驱动电机序号依次排列
     */
    private LinkedList<GbSingleDriveMotorDataInfo> driveMotorList;

    @Override
    public int parse(byte[] dataInfoBytes) {
        if (dataInfoBytes == null || dataInfoBytes.length == 0) {
            return 0;
        }
        this.dataInfoType = GbDataInfoType.DRIVE_MOTOR;
        this.driveMotorCount = dataInfoBytes[0];
        this.driveMotorList = new LinkedList<>();
        int startPos = 1;
        for (int i = 0; i < driveMotorCount; i++) {
            GbSingleDriveMotorDataInfo singleDriveMotor = new GbSingleDriveMotorDataInfo();
            int length = singleDriveMotor.parse(Arrays.copyOfRange(dataInfoBytes, startPos, dataInfoBytes.length));
            driveMotorList.add(singleDriveMotor);
            startPos += length;
        }
        return 1 + this.driveMotorCount * 12;
    }

    @Override
    public byte[] toByteArray() {
        byte[] bytes = ArrayUtil.addAll(new byte[]{this.dataInfoType.getCode()}, new byte[]{this.driveMotorCount});
        for (GbSingleDriveMotorDataInfo driveMotor : driveMotorList) {
            bytes = ArrayUtil.addAll(bytes, driveMotor.toByteArray());
        }
        return bytes;
    }
}
