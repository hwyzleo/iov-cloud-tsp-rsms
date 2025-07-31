package net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.handler;

import cn.hutool.core.util.ObjUtil;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessageDataInfo;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.datainfo.*;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbChargingState;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbVehicleState;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.AbstractChecker;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.CheckItem;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.InspectionHandler;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 数据一致性检测处理器
 * 关联一致性
 *
 * @author hwyz_leo
 */
@Component
public class ConsistencyInspectionHandler extends BaseInspectionHandler implements InspectionHandler {

    @Override
    protected long handleDataInfo(Date messageTime, List<GbMessageDataInfo> dataInfoList, Map<String, AbstractChecker> vehicleCheckers) {
        long errorCount = 0;
        GbVehicleState vehicleState = null;
        Integer speed = null;
        GbChargingState chargingState = null;
        Integer totalCurrent = null;
        Integer totalVoltage = null;
        Integer batteryVoltageSum = null;
        Integer maxVoltageDeviceNo = null;
        Integer maxVoltageCellNo = null;
        Integer batteryMaxVoltageDeviceNo = null;
        Integer batteryMaxVoltageCellNo = null;
        Integer minVoltageDeviceNo = null;
        Integer minVoltageCellNo = null;
        Integer batteryMinVoltageDeviceNo = null;
        Integer batteryMinVoltageCellNo = null;
        Integer cellMaxVoltage = null;
        Integer batteryMaxVoltage = null;
        Integer cellMinVoltage = null;
        Integer batteryMinVoltage = null;
        Integer maxTemperatureDeviceNo = null;
        Integer maxTemperatureProbeNo = null;
        Integer batteryMaxTemperatureDeviceNo = null;
        Integer batteryMaxTemperatureProbeNo = null;
        Integer minTemperatureDeviceNo = null;
        Integer minTemperatureProbeNo = null;
        Integer batteryMinTemperatureDeviceNo = null;
        Integer batteryMinTemperatureProbeNo = null;
        Integer maxTemperature = null;
        Integer batteryMaxTemperature = null;
        Integer minTemperature = null;
        Integer batteryMinTemperature = null;
        for (GbMessageDataInfo dataInfo : dataInfoList) {
            switch (dataInfo.getDataInfoType()) {
                case VEHICLE -> {
                    GbVehicleDataDataInfo vehicleDataInfo = (GbVehicleDataDataInfo) dataInfo;
                    vehicleState = vehicleDataInfo.getVehicleState();
                    speed = vehicleDataInfo.getSpeed();
                    chargingState = vehicleDataInfo.getChargingState();
                    totalCurrent = vehicleDataInfo.getTotalCurrent();
                    totalVoltage = vehicleDataInfo.getTotalVoltage();
                }
                case EXTREMUM -> {
                    GbExtremumDataInfo extremumDataInfo = (GbExtremumDataInfo) dataInfo;
                    maxVoltageDeviceNo = (int) extremumDataInfo.getMaxVoltageBatteryDeviceNo();
                    maxVoltageCellNo = (int) extremumDataInfo.getMaxVoltageCellNo();
                    cellMaxVoltage = extremumDataInfo.getCellMaxVoltage();
                    minVoltageDeviceNo = (int) extremumDataInfo.getMinVoltageBatteryDeviceNo();
                    minVoltageCellNo = (int) extremumDataInfo.getMinVoltageCellNo();
                    cellMinVoltage = extremumDataInfo.getCellMinVoltage();
                    maxTemperatureDeviceNo = (int) extremumDataInfo.getMaxTemperatureDeviceNo();
                    maxTemperatureProbeNo = (int) extremumDataInfo.getMaxTemperatureProbeNo();
                    minTemperatureDeviceNo = (int) extremumDataInfo.getMinTemperatureDeviceNo();
                    minTemperatureProbeNo = (int) extremumDataInfo.getMinTemperatureProbeNo();
                    maxTemperature = (int) extremumDataInfo.getMaxTemperature();
                    minTemperature = (int) extremumDataInfo.getMinTemperature();
                }
                case BATTERY_VOLTAGE -> {
                    GbBatteryVoltageDataInfo batteryVoltageDataInfo = (GbBatteryVoltageDataInfo) dataInfo;
                    LinkedList<GbSingleBatteryVoltageDataInfo> voltageList = batteryVoltageDataInfo.getBatteryVoltageList();
                    if (!voltageList.isEmpty()) {
                        batteryVoltageSum = 0;
                        batteryMaxVoltage = 0;
                        batteryMinVoltage = 0;
                        batteryMaxVoltageDeviceNo = 0;
                        batteryMinVoltageDeviceNo = 0;
                        batteryMaxVoltageCellNo = 0;
                        batteryMinVoltageCellNo = 0;
                        for (GbSingleBatteryVoltageDataInfo batteryVoltage : voltageList) {
                            int i = 0;
                            for (Integer voltage : batteryVoltage.getCellVoltageList()) {
                                i++;
                                batteryVoltageSum += voltage;
                                if (voltage > batteryMaxVoltage) {
                                    batteryMaxVoltage = voltage;
                                    batteryMaxVoltageDeviceNo = (int) batteryVoltage.getSn();
                                    batteryMaxVoltageCellNo = i;
                                }
                                if (batteryMinVoltage == 0 || voltage < batteryMinVoltage) {
                                    batteryMinVoltage = voltage;
                                    batteryMinVoltageDeviceNo = (int) batteryVoltage.getSn();
                                    batteryMinVoltageCellNo = i;
                                }
                            }
                        }
                    }
                }
                case BATTERY_TEMPERATURE -> {
                    GbBatteryTemperatureDataInfo batteryTemperatureDataInfo = (GbBatteryTemperatureDataInfo) dataInfo;
                    LinkedList<GbSingleBatteryTemperatureDataInfo> temperatureList = batteryTemperatureDataInfo.getBatteryTemperatureList();
                    if (!temperatureList.isEmpty()) {
                        batteryMaxTemperature = 0;
                        batteryMinTemperature = 0;
                        batteryMaxTemperatureDeviceNo = 0;
                        batteryMinTemperatureDeviceNo = 0;
                        batteryMaxTemperatureProbeNo = 0;
                        batteryMinTemperatureProbeNo = 0;
                        for (GbSingleBatteryTemperatureDataInfo batteryTemperature : temperatureList) {
                            int i = 0;
                            for (byte temperature : batteryTemperature.getTemperatures()) {
                                i++;
                                if ((int) temperature > batteryMaxTemperature) {
                                    batteryMaxTemperature = (int) temperature;
                                    batteryMaxTemperatureDeviceNo = (int) batteryTemperature.getSn();
                                    batteryMaxTemperatureProbeNo = i;
                                }
                                if (batteryMinTemperature == 0 || (int) temperature < batteryMinTemperature) {
                                    batteryMinTemperature = (int) temperature;
                                    batteryMinTemperatureDeviceNo = (int) batteryTemperature.getSn();
                                    batteryMinTemperatureProbeNo = i;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (ObjUtil.isNotNull(vehicleState) && ObjUtil.isNotNull(speed)) {
            errorCount += validate(messageTime, GbVehicleState.POWER_ON != vehicleState && speed > 0 ? 1 : 0, CheckItem.VEHICLE_STATE,
                    vehicleCheckers);
        }
        if (ObjUtil.isNotNull(chargingState) && ObjUtil.isNotNull(speed) && ObjUtil.isNotNull(totalCurrent)) {
            errorCount += validate(messageTime, GbChargingState.PARKING_CHARGING == chargingState && (speed > 0 || totalCurrent >= 0) ? 1 : 0,
                    CheckItem.CHARGING_STATE, vehicleCheckers);
        }
        if (ObjUtil.isNotNull(totalVoltage) && ObjUtil.isNotNull(batteryVoltageSum)) {
            errorCount += validate(messageTime, Math.abs(totalVoltage * 100 - batteryVoltageSum) > totalVoltage ? 1 : 0,
                    CheckItem.TOTAL_VOLTAGE, vehicleCheckers);
        }
        if (ObjUtil.isNotNull(maxVoltageDeviceNo) && ObjUtil.isNotNull(batteryMaxVoltageDeviceNo)) {
            errorCount += validate(messageTime, maxVoltageDeviceNo.intValue() != batteryMaxVoltageDeviceNo ? 1 : 0,
                    CheckItem.MAX_VOLTAGE_BATTERY_DEVICE_NO, vehicleCheckers);
        }
        if (ObjUtil.isNotNull(maxVoltageCellNo) && ObjUtil.isNotNull(batteryMaxVoltageCellNo)) {
            errorCount += validate(messageTime, maxVoltageCellNo.intValue() != batteryMaxVoltageCellNo ? 1 : 0,
                    CheckItem.MAX_VOLTAGE_CELL_NO, vehicleCheckers);
        }
        if (ObjUtil.isNotNull(cellMaxVoltage) && ObjUtil.isNotNull(batteryMaxVoltage)) {
            errorCount += validate(messageTime, cellMaxVoltage.intValue() != batteryMaxVoltage ? 1 : 0,
                    CheckItem.CELL_MAX_VOLTAGE, vehicleCheckers);
        }
        if (ObjUtil.isNotNull(minVoltageDeviceNo) && ObjUtil.isNotNull(batteryMinVoltageDeviceNo)) {
            errorCount += validate(messageTime, minVoltageDeviceNo.intValue() != batteryMinVoltageDeviceNo ? 1 : 0,
                    CheckItem.MIN_VOLTAGE_BATTERY_DEVICE_NO, vehicleCheckers);
        }
        if (ObjUtil.isNotNull(minVoltageCellNo) && ObjUtil.isNotNull(batteryMinVoltageCellNo)) {
            errorCount += validate(messageTime, minVoltageCellNo.intValue() != batteryMinVoltageCellNo ? 1 : 0,
                    CheckItem.MIN_VOLTAGE_CELL_NO, vehicleCheckers);
        }
        if (ObjUtil.isNotNull(cellMinVoltage) && ObjUtil.isNotNull(batteryMinVoltage)) {
            errorCount += validate(messageTime, cellMinVoltage.intValue() != batteryMinVoltage ? 1 : 0,
                    CheckItem.CELL_MIN_VOLTAGE, vehicleCheckers);
        }
        if (ObjUtil.isNotNull(maxTemperatureDeviceNo) && ObjUtil.isNotNull(batteryMaxTemperatureDeviceNo)) {
            errorCount += validate(messageTime, maxTemperatureDeviceNo.intValue() != batteryMaxTemperatureDeviceNo ? 1 : 0,
                    CheckItem.MAX_TEMPERATURE_DEVICE_NO, vehicleCheckers);
        }
        if (ObjUtil.isNotNull(maxTemperatureProbeNo) && ObjUtil.isNotNull(batteryMaxTemperatureProbeNo)) {
            errorCount += validate(messageTime, maxTemperatureProbeNo.intValue() != batteryMaxTemperatureProbeNo ? 1 : 0,
                    CheckItem.MAX_TEMPERATURE_PROBE_NO, vehicleCheckers);
        }
        if (ObjUtil.isNotNull(maxTemperature) && ObjUtil.isNotNull(batteryMaxTemperature)) {
            errorCount += validate(messageTime, maxTemperature.intValue() != batteryMaxTemperature ? 1 : 0,
                    CheckItem.MAX_TEMPERATURE, vehicleCheckers);
        }
        if (ObjUtil.isNotNull(minTemperatureDeviceNo) && ObjUtil.isNotNull(batteryMinTemperatureDeviceNo)) {
            errorCount += validate(messageTime, minTemperatureDeviceNo.intValue() != batteryMinTemperatureDeviceNo ? 1 : 0,
                    CheckItem.MIN_TEMPERATURE_DEVICE_NO, vehicleCheckers);
        }
        if (ObjUtil.isNotNull(minTemperatureProbeNo) && ObjUtil.isNotNull(batteryMinTemperatureProbeNo)) {
            errorCount += validate(messageTime, minTemperatureProbeNo.intValue() != batteryMinTemperatureProbeNo ? 1 : 0,
                    CheckItem.MIN_TEMPERATURE_PROBE_NO, vehicleCheckers);
        }
        if (ObjUtil.isNotNull(minTemperature) && ObjUtil.isNotNull(batteryMinTemperature)) {
            errorCount += validate(messageTime, minTemperature.intValue() != batteryMinTemperature ? 1 : 0,
                    CheckItem.MIN_TEMPERATURE, vehicleCheckers);
        }
        return errorCount;
    }

    @Override
    public long validate(Date messageTime, int value, CheckItem item, Integer sn, Map<String, AbstractChecker> vehicleCheckers) {
        long errorCount = 0;
        errorCount += getVehicleChecker(item, sn, TYPE_INCONSISTENCY, vehicleCheckers).check(value);
        return errorCount;
    }
}
