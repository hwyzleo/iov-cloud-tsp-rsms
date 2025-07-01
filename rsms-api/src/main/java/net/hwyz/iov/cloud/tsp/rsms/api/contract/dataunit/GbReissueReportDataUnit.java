package net.hwyz.iov.cloud.tsp.rsms.api.contract.dataunit;

import cn.hutool.core.util.ArrayUtil;
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
     * 数据采集时间
     */
    private byte[] collectTime;
    /**
     * 数据信息列表
     */
    private LinkedList<GbMessageDataInfo> dataInfoList;

    @Override
    public void parse(byte[] dataUnitBytes) {
        this.collectTime = Arrays.copyOfRange(dataUnitBytes, 0, 6);
        this.dataInfoList = new LinkedList<>();
        int startPos = 6;
        while (startPos < dataUnitBytes.length) {
            GbDataInfoType dataInfoType = GbDataInfoType.valOf(dataUnitBytes[startPos]);
            startPos++;
            switch (dataInfoType) {
                case VEHICLE -> {
                    GbVehicleDataDataInfo vehicleData = new GbVehicleDataDataInfo();
                    vehicleData.parse(Arrays.copyOfRange(dataUnitBytes, startPos, startPos + vehicleData.getLength()));
                    dataInfoList.add(vehicleData);
                    startPos += vehicleData.getLength();
                }
                case DRIVE_MOTOR -> {
                    byte driveMotorCount = dataUnitBytes[startPos];
                    startPos++;
                    GbDriveMotorDataInfo driveMotor = new GbDriveMotorDataInfo(driveMotorCount);
                    driveMotor.parse(Arrays.copyOfRange(dataUnitBytes, startPos, startPos + driveMotor.getLength()));
                    dataInfoList.add(driveMotor);
                    startPos += driveMotor.getLength();
                }
                case FUEL_CELL -> {
                    int temperatureProbeCount = GbUtil.bytesToWord(Arrays.copyOfRange(dataUnitBytes, startPos + 6, startPos + 8));
                    GbFuelCellDataInfo fuelCell = new GbFuelCellDataInfo(temperatureProbeCount);
                    fuelCell.parse(Arrays.copyOfRange(dataUnitBytes, startPos, startPos + fuelCell.getLength()));
                    dataInfoList.add(fuelCell);
                    startPos += fuelCell.getLength();
                }
                case ENGINE -> {
                    GbEngineDataInfo engine = new GbEngineDataInfo();
                    engine.parse(Arrays.copyOfRange(dataUnitBytes, startPos, startPos + engine.getLength()));
                    dataInfoList.add(engine);
                    startPos += engine.getLength();
                }
                case POSITION -> {
                    GbPositionDataInfo engine = new GbPositionDataInfo();
                    engine.parse(Arrays.copyOfRange(dataUnitBytes, startPos, startPos + engine.getLength()));
                    dataInfoList.add(engine);
                    startPos += engine.getLength();
                }
                case EXTREMUM -> {
                    GbExtremumDataInfo extremum = new GbExtremumDataInfo();
                    extremum.parse(Arrays.copyOfRange(dataUnitBytes, startPos, startPos + extremum.getLength()));
                    dataInfoList.add(extremum);
                    startPos += extremum.getLength();
                }
                case ALARM -> {
                    byte batteryFaultCount = dataUnitBytes[startPos + 5];
                    byte driveMotorFaultCount = dataUnitBytes[startPos + 6 + batteryFaultCount * 4];
                    byte engineFaultCount = dataUnitBytes[startPos + 7 + batteryFaultCount * 4 + driveMotorFaultCount * 4];
                    byte otherFaultCount = dataUnitBytes[startPos + 8 + batteryFaultCount * 4 + driveMotorFaultCount * 4 + engineFaultCount * 4];
                    GbAlarmDataInfo alarm = new GbAlarmDataInfo(batteryFaultCount, driveMotorFaultCount, engineFaultCount, otherFaultCount);
                    alarm.parse(Arrays.copyOfRange(dataUnitBytes, startPos, startPos + alarm.getLength()));
                    dataInfoList.add(alarm);
                    startPos += alarm.getLength();
                }
            }
        }
    }

    @Override
    public byte[] toByteArray() {
        byte[] bytes = this.collectTime;
        for (GbMessageDataInfo dataInfo : dataInfoList) {
            bytes = ArrayUtil.addAll(bytes, dataInfo.toByteArray());
        }
        return bytes;
    }
}
