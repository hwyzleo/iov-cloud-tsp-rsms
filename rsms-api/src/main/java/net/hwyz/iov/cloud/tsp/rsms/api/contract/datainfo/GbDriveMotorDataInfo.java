package net.hwyz.iov.cloud.tsp.rsms.api.contract.datainfo;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@AllArgsConstructor
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

    public GbDriveMotorDataInfo(byte driveMotorCount) {
        this.driveMotorCount = driveMotorCount;
    }

    @Override
    public int getLength() {
        return this.driveMotorCount * 12;
    }

    @Override
    public void parse(byte[] dataInfoBytes) {
        if (dataInfoBytes == null || dataInfoBytes.length != getLength()) {
            logger.warn("国标驱动电机数据信息[{}]异常", Arrays.toString(dataInfoBytes));
            return;
        }
        this.dataInfoType = GbDataInfoType.DRIVE_MOTOR;
        this.driveMotorList = new LinkedList<>();
        int startPos = 1;
        while (startPos < dataInfoBytes.length) {
            GbSingleDriveMotorDataInfo singleDriveMotor = new GbSingleDriveMotorDataInfo();
            singleDriveMotor.parse(Arrays.copyOfRange(dataInfoBytes, startPos, startPos + singleDriveMotor.getLength()));
            driveMotorList.add(singleDriveMotor);
            startPos += singleDriveMotor.getLength();
        }
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
