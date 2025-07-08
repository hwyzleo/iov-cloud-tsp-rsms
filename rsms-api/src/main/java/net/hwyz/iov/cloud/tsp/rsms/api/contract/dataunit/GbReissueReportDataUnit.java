package net.hwyz.iov.cloud.tsp.rsms.api.contract.dataunit;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessageDataInfo;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessageDataUnit;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.datainfo.*;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbDataInfoType;
import net.hwyz.iov.cloud.tsp.rsms.api.util.GbUtil;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * 国标车辆补发信息上报数据单元
 *
 * @author hwyz_leo
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GbReissueReportDataUnit extends GbMessageDataUnit {

    /**
     * 数据信息列表
     */
    private LinkedList<GbMessageDataInfo> dataInfoList;

    @Override
    public void parse(byte[] dataUnitBytes) {
        this.messageTime = GbUtil.dateTimeBytesToDate(Arrays.copyOfRange(dataUnitBytes, 0, 6));
        this.dataInfoList = new LinkedList<>();
        int startPos = 6;
        while (startPos < dataUnitBytes.length) {
            GbDataInfoType dataInfoType = GbDataInfoType.valOf(dataUnitBytes[startPos]);
            startPos++;
            GbMessageDataInfo dataInfo = null;
            switch (dataInfoType) {
                case VEHICLE -> dataInfo = new GbVehicleDataDataInfo();
                case DRIVE_MOTOR -> dataInfo = new GbDriveMotorDataInfo();
                case FUEL_CELL -> dataInfo = new GbFuelCellDataInfo();
                case ENGINE -> dataInfo = new GbEngineDataInfo();
                case POSITION -> dataInfo = new GbPositionDataInfo();
                case EXTREMUM -> dataInfo = new GbExtremumDataInfo();
                case ALARM -> dataInfo = new GbAlarmDataInfo();
                case BATTERY_VOLTAGE -> dataInfo = new GbBatteryVoltageDataInfo();
                case BATTERY_TEMPERATURE -> dataInfo = new GbBatteryTemperatureDataInfo();
            }
            if (ObjUtil.isNotNull(dataInfo)) {
                int length = dataInfo.parse(Arrays.copyOfRange(dataUnitBytes, startPos, dataUnitBytes.length));
                dataInfoList.add(dataInfo);
                startPos += length;
            }
        }
    }

    @Override
    public byte[] toByteArray() {
        byte[] bytes = GbUtil.getGbDateTimeBytes(this.messageTime.getTime());
        for (GbMessageDataInfo dataInfo : dataInfoList) {
            bytes = ArrayUtil.addAll(bytes, dataInfo.toByteArray());
        }
        return bytes;
    }
}
