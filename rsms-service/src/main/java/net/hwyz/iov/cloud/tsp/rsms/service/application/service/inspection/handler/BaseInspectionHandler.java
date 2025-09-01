package net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.handler;

import cn.hutool.core.util.ObjUtil;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessage;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessageDataInfo;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.datainfo.*;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.dataunit.GbRealtimeReportDataUnit;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.dataunit.GbReissueReportDataUnit;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.AbstractChecker;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.CheckItem;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.InspectionHandler;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.checker.*;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.GbInspectionItemPo;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.GbInspectionReportPo;

import java.util.*;

/**
 * 国标数据质量检测处理器基础类
 *
 * @author hwyz_leo
 */
@Slf4j
public abstract class BaseInspectionHandler implements InspectionHandler {

    protected static final String CATEGORY_STANDARD = "STANDARD";
    protected static final String CATEGORY_INTEGRITY = "INTEGRITY";
    protected static final String CATEGORY_ACCURACY = "ACCURACY";
    protected static final String CATEGORY_CONSISTENCY = "CONSISTENCY";
    protected static final String CATEGORY_TIMELINESS = "TIMELINESS";
    protected static final String TYPE_ABNORMAL = "ABNORMAL";
    protected static final String TYPE_ABNORMAL_CONTINUOUS = "ABNORMAL_CONTINUOUS";
    protected static final String TYPE_INVALID = "INVALID";
    protected static final String TYPE_INVALID_CONTINUOUS = "INVALID_CONTINUOUS";
    protected static final String TYPE_NULL = "NULL";
    protected static final String TYPE_NULL_CONTINUOUS = "NULL_CONTINUOUS";
    protected static final String TYPE_RANGE = "RANGE";
    protected static final String TYPE_RANGE_CONTINUOUS = "RANGE_CONTINUOUS";
    protected static final String TYPE_INCONSISTENCY = "INCONSISTENCY";
    protected static final String TYPE_DUPLICATE = "DUPLICATE";

    @Override
    public void inspect(GbInspectionReportPo report, List<GbMessage> gbMessages) {
        Map<String, Map<String, AbstractChecker>> checkers = new HashMap<>();
        gbMessages.forEach(message -> {
            String vin = message.getVin();
            Date messageTime = message.getMessageTime();
            Map<String, AbstractChecker> vehicleCheckers = getVehicleCheckers(vin, checkers);
            int errorCount = 0;
            switch (message.getHeader().getCommandFlag()) {
                case REALTIME_REPORT -> {
                    GbRealtimeReportDataUnit dataUnit = (GbRealtimeReportDataUnit) message.getDataUnit();
                    errorCount = handleDataInfo(messageTime, dataUnit.getDataInfoList(), vehicleCheckers);
                }
                case REISSUE_REPORT -> {
                    GbReissueReportDataUnit dataUnit = (GbReissueReportDataUnit) message.getDataUnit();
                    errorCount = handleDataInfo(messageTime, dataUnit.getDataInfoList(), vehicleCheckers);
                }
            }
            if (errorCount > 0) {
                report.getErrorMessages().add(message);
            }
        });
        summarize(report, checkers);
    }

    /**
     * 获取车辆对应检查器
     *
     * @param vin      车架号
     * @param checkers 车辆检查器Map
     * @return 检查器
     */
    protected Map<String, AbstractChecker> getVehicleCheckers(String vin, Map<String, Map<String, AbstractChecker>> checkers) {
        if (!checkers.containsKey(vin)) {
            checkers.put(vin, initVehicleChecker(vin));
        }
        return checkers.get(vin);
    }

    /**
     * 初始化车辆检查器
     *
     * @param vin 车架号
     */
    private Map<String, AbstractChecker> initVehicleChecker(String vin) {
        Map<String, AbstractChecker> vehicleCheckers = new HashMap<>();
        vehicleCheckers.put("DEFAULT", new MatchValueChecker(vin, null, null, null, null));
        return vehicleCheckers;
    }

    /**
     * 处理数据信息
     *
     * @param messageTime     消息时间
     * @param dataInfoList    数据信息列表
     * @param vehicleCheckers 车辆检查器
     * @return 错误数据数量
     */
    protected int handleDataInfo(Date messageTime, List<GbMessageDataInfo> dataInfoList, Map<String, AbstractChecker> vehicleCheckers) {
        int errorCount = 0;
        for (GbMessageDataInfo dataInfo : dataInfoList) {
            switch (dataInfo.getDataInfoType()) {
                case VEHICLE -> {
                    GbVehicleDataDataInfo vehicleDataInfo = (GbVehicleDataDataInfo) dataInfo;
                    errorCount += validate(messageTime, vehicleDataInfo.getVehicleState().getCode(), CheckItem.VEHICLE_STATE, vehicleCheckers);
                    errorCount += validate(messageTime, vehicleDataInfo.getChargingState().getCode(), CheckItem.CHARGING_STATE, vehicleCheckers);
                    errorCount += validate(messageTime, vehicleDataInfo.getRunningMode().getCode(), CheckItem.RUNNING_MODE, vehicleCheckers);
                    errorCount += validate(messageTime, vehicleDataInfo.getSpeed(), CheckItem.SPEED, vehicleCheckers);
                    errorCount += validate(messageTime, vehicleDataInfo.getTotalOdometer(), CheckItem.TOTAL_ODOMETER, vehicleCheckers);
                    errorCount += validate(messageTime, vehicleDataInfo.getTotalVoltage(), CheckItem.TOTAL_VOLTAGE, vehicleCheckers);
                    errorCount += validate(messageTime, vehicleDataInfo.getTotalCurrent(), CheckItem.TOTAL_CURRENT, vehicleCheckers);
                    errorCount += validate(messageTime, vehicleDataInfo.getDcdcState().getCode(), CheckItem.DCDC_STATE, vehicleCheckers);
                    errorCount += validate(messageTime, vehicleDataInfo.getSoc(), CheckItem.SOC, vehicleCheckers);
                    errorCount += validate(messageTime, vehicleDataInfo.getInsulationResistance(), CheckItem.INSULATION_RESISTANCE, vehicleCheckers);
                }
                case DRIVE_MOTOR -> {
                    GbDriveMotorDataInfo driveMotorDataInfo = (GbDriveMotorDataInfo) dataInfo;
                    for (GbSingleDriveMotorDataInfo driveMotor : driveMotorDataInfo.getDriveMotorList()) {
                        errorCount += validate(messageTime, driveMotor.getState().getCode(), CheckItem.DRIVE_MOTOR_STATE, (int) driveMotor.getSn(), vehicleCheckers);
                        errorCount += validate(messageTime, driveMotor.getControllerTemperature(), CheckItem.DRIVE_MOTOR_CONTROLLER_TEMPERATURE, (int) driveMotor.getSn(), vehicleCheckers);
                        errorCount += validate(messageTime, driveMotor.getTorque(), CheckItem.DRIVE_MOTOR_TORQUE, (int) driveMotor.getSn(), vehicleCheckers);
                        errorCount += validate(messageTime, driveMotor.getTemperature(), CheckItem.DRIVE_MOTOR_TEMPERATURE, (int) driveMotor.getSn(), vehicleCheckers);
                        errorCount += validate(messageTime, driveMotor.getControllerInputVoltage(), CheckItem.DRIVE_MOTOR_CONTROLLER_INPUT_VOLTAGE, (int) driveMotor.getSn(), vehicleCheckers);
                        errorCount += validate(messageTime, driveMotor.getControllerDcBusCurrent(), CheckItem.DRIVE_MOTOR_CONTROLLER_DC_BUS_CURRENT, (int) driveMotor.getSn(), vehicleCheckers);
                    }
                }
                case FUEL_CELL -> {
                    GbFuelCellDataInfo fuelCellDataInfo = (GbFuelCellDataInfo) dataInfo;
                    errorCount += validate(messageTime, fuelCellDataInfo.getVoltage(), CheckItem.FUEL_CELL_VOLTAGE, vehicleCheckers);
                    errorCount += validate(messageTime, fuelCellDataInfo.getCurrent(), CheckItem.FUEL_CELL_CURRENT, vehicleCheckers);
                    errorCount += validate(messageTime, fuelCellDataInfo.getConsumptionRate(), CheckItem.FUEL_CELL_CONSUMPTION_RATE, vehicleCheckers);
                    errorCount += validate(messageTime, fuelCellDataInfo.getTemperatureProbeCount(), CheckItem.FUEL_CELL_TEMPERATURE_PROBE_COUNT, vehicleCheckers);
                    errorCount += validate(messageTime, fuelCellDataInfo.getHydrogenSystemMaxTemperature(), CheckItem.FUEL_CELL_HYDROGEN_SYSTEM_MAX_TEMPERATURE, vehicleCheckers);
                    errorCount += validate(messageTime, fuelCellDataInfo.getHydrogenSystemMaxTemperatureProbe(), CheckItem.FUEL_CELL_HYDROGEN_SYSTEM_MAX_TEMPERATURE_PROBE, vehicleCheckers);
                    errorCount += validate(messageTime, fuelCellDataInfo.getHydrogenMaxConcentration(), CheckItem.FUEL_CELL_HYDROGEN_MAX_CONCENTRATION, vehicleCheckers);
                    errorCount += validate(messageTime, fuelCellDataInfo.getHydrogenMaxConcentrationSensor(), CheckItem.FUEL_CELL_HYDROGEN_MAX_CONCENTRATION_SENSOR, vehicleCheckers);
                    errorCount += validate(messageTime, fuelCellDataInfo.getHydrogenMaxPressure(), CheckItem.FUEL_CELL_HYDROGEN_MAX_PRESSURE, vehicleCheckers);
                    errorCount += validate(messageTime, fuelCellDataInfo.getHydrogenMaxPressureSensor(), CheckItem.FUEL_CELL_HYDROGEN_MAX_PRESSURE_SENSOR, vehicleCheckers);
                    errorCount += validate(messageTime, fuelCellDataInfo.getHighPressureDcdcState().getCode(), CheckItem.FUEL_CELL_HIGH_PRESSURE_DCDC_STATE, vehicleCheckers);
                }
                case ENGINE -> {
                    GbEngineDataInfo engineDataInfo = (GbEngineDataInfo) dataInfo;
                    errorCount += validate(messageTime, engineDataInfo.getState().getCode(), CheckItem.ENGINE_STATE, vehicleCheckers);
                    errorCount += validate(messageTime, engineDataInfo.getCrankshaftSpeed(), CheckItem.ENGINE_CRANKSHAFT_SPEED, vehicleCheckers);
                    errorCount += validate(messageTime, engineDataInfo.getConsumptionRate(), CheckItem.ENGINE_CONSUMPTION_RATE, vehicleCheckers);
                }
                case EXTREMUM -> {
                    GbExtremumDataInfo extremumDataInfo = (GbExtremumDataInfo) dataInfo;
                    errorCount += validate(messageTime, extremumDataInfo.getMaxVoltageBatteryDeviceNo(), CheckItem.MAX_VOLTAGE_BATTERY_DEVICE_NO, vehicleCheckers);
                    errorCount += validate(messageTime, extremumDataInfo.getMaxVoltageCellNo(), CheckItem.MAX_VOLTAGE_CELL_NO, vehicleCheckers);
                    errorCount += validate(messageTime, extremumDataInfo.getCellMaxVoltage(), CheckItem.CELL_MAX_VOLTAGE, vehicleCheckers);
                    errorCount += validate(messageTime, extremumDataInfo.getMinVoltageBatteryDeviceNo(), CheckItem.MIN_VOLTAGE_BATTERY_DEVICE_NO, vehicleCheckers);
                    errorCount += validate(messageTime, extremumDataInfo.getMinVoltageCellNo(), CheckItem.MIN_VOLTAGE_CELL_NO, vehicleCheckers);
                    errorCount += validate(messageTime, extremumDataInfo.getCellMinVoltage(), CheckItem.CELL_MIN_VOLTAGE, vehicleCheckers);
                    errorCount += validate(messageTime, extremumDataInfo.getMaxTemperatureDeviceNo(), CheckItem.MAX_TEMPERATURE_DEVICE_NO, vehicleCheckers);
                    errorCount += validate(messageTime, extremumDataInfo.getMaxTemperatureProbeNo(), CheckItem.MAX_TEMPERATURE_PROBE_NO, vehicleCheckers);
                    errorCount += validate(messageTime, extremumDataInfo.getMaxTemperature(), CheckItem.MAX_TEMPERATURE, vehicleCheckers);
                    errorCount += validate(messageTime, extremumDataInfo.getMinTemperatureDeviceNo(), CheckItem.MIN_TEMPERATURE_DEVICE_NO, vehicleCheckers);
                    errorCount += validate(messageTime, extremumDataInfo.getMinTemperatureProbeNo(), CheckItem.MIN_TEMPERATURE_PROBE_NO, vehicleCheckers);
                    errorCount += validate(messageTime, extremumDataInfo.getMinTemperature(), CheckItem.MIN_TEMPERATURE, vehicleCheckers);
                }
                case ALARM -> {
                    GbAlarmDataInfo alarmDataInfo = (GbAlarmDataInfo) dataInfo;
                    errorCount += validate(messageTime, alarmDataInfo.getMaxAlarmLevel().getCode(), CheckItem.MAX_ALARM_LEVEL, vehicleCheckers);
                    errorCount += validate(messageTime, alarmDataInfo.getBatteryFaultCount(), CheckItem.BATTERY_FAULT_COUNT, vehicleCheckers);
                    errorCount += validate(messageTime, alarmDataInfo.getDriveMotorFaultCount(), CheckItem.DRIVE_MOTOR_FAULT_COUNT, vehicleCheckers);
                    errorCount += validate(messageTime, alarmDataInfo.getEngineFaultCount(), CheckItem.ENGINE_FAULT_COUNT, vehicleCheckers);
                    errorCount += validate(messageTime, alarmDataInfo.getOtherFaultCount(), CheckItem.OTHER_FAULT_COUNT, vehicleCheckers);
                }
            }
        }
        return errorCount;
    }

    /**
     * 获取车辆对应数据项检查器
     *
     * @param item            数据项
     * @param sn              数据项序号
     * @param type            检查项小类
     * @param vehicleCheckers 车辆检查器
     * @return 数据项检查器
     */
    protected AbstractChecker getVehicleChecker(CheckItem item, Integer sn, String type, Map<String, AbstractChecker> vehicleCheckers) {
        AbstractChecker defaultChecker = vehicleCheckers.get("DEFAULT");
        String itemKey = item.name();
        if (ObjUtil.isNotNull(sn)) {
            itemKey += sn;
        }
        String mapKey = itemKey + "_" + type;
        if (!vehicleCheckers.containsKey(mapKey)) {
            switch (type) {
                case TYPE_ABNORMAL ->
                        vehicleCheckers.put(mapKey, new MatchValueChecker(defaultChecker.getVin(), CATEGORY_STANDARD, type, itemKey, item.getAbnormalValue()));
                case TYPE_ABNORMAL_CONTINUOUS ->
                        vehicleCheckers.put(mapKey, new ContinuousMatchValueChecker(defaultChecker.getVin(), CATEGORY_STANDARD, type, itemKey, item.getAbnormalValue()));
                case TYPE_INVALID ->
                        vehicleCheckers.put(mapKey, new MatchValueChecker(defaultChecker.getVin(), CATEGORY_STANDARD, type, itemKey, item.getInvalidValue()));
                case TYPE_INVALID_CONTINUOUS ->
                        vehicleCheckers.put(mapKey, new ContinuousMatchValueChecker(defaultChecker.getVin(), CATEGORY_STANDARD, type, itemKey, item.getInvalidValue()));
                case TYPE_NULL ->
                        vehicleCheckers.put(mapKey, new NullValueChecker(defaultChecker.getVin(), CATEGORY_INTEGRITY, type, itemKey));
                case TYPE_NULL_CONTINUOUS ->
                        vehicleCheckers.put(mapKey, new ContinuousNullValueChecker(defaultChecker.getVin(), CATEGORY_INTEGRITY, type, itemKey));
                case TYPE_RANGE ->
                        vehicleCheckers.put(mapKey, new RangeValueChecker(defaultChecker.getVin(), CATEGORY_ACCURACY, type, itemKey, item.getMinValue(), item.getMaxValue()));
                case TYPE_RANGE_CONTINUOUS ->
                        vehicleCheckers.put(mapKey, new ContinuousRangeValueChecker(defaultChecker.getVin(), CATEGORY_ACCURACY, type, itemKey, item.getMinValue(), item.getMaxValue()));
                case TYPE_INCONSISTENCY ->
                        vehicleCheckers.put(mapKey, new MatchValueChecker(defaultChecker.getVin(), CATEGORY_CONSISTENCY, type, itemKey, 1));
                case TYPE_DUPLICATE ->
                        vehicleCheckers.put(mapKey, new DuplicateTimeChecker(defaultChecker.getVin(), CATEGORY_TIMELINESS, type, itemKey));
            }
        }
        return vehicleCheckers.get(mapKey);
    }

    /**
     * 验证检查项相关类型
     *
     * @param messageTime     消息时间
     * @param value           检查项值
     * @param item            检查项
     * @param vehicleCheckers 车辆检查器
     * @return 错误数量
     */
    public int validate(Date messageTime, int value, CheckItem item, Map<String, AbstractChecker> vehicleCheckers) {
        return validate(messageTime, value, item, null, vehicleCheckers);
    }

    /**
     * 验证检查项相关类型
     *
     * @param messageTime     消息时间
     * @param value           检查项值
     * @param item            检查项
     * @param sn              检查项序号
     * @param vehicleCheckers 车辆检查器
     * @return 错误数量
     */
    public abstract int validate(Date messageTime, int value, CheckItem item, Integer sn, Map<String, AbstractChecker> vehicleCheckers);

    /**
     * 汇总所有车辆检查数据
     *
     * @param report   检查报告
     * @param checkers 检查项
     */
    protected void summarize(GbInspectionReportPo report, Map<String, Map<String, AbstractChecker>> checkers) {
        Map<String, GbInspectionItemPo> map = new HashMap<>();
        checkers.forEach((vin, vehicleCheckers) -> {
            vehicleCheckers.forEach((item, vehicleChecker) -> {
                // 过滤默认检查项
                if (!"DEFAULT".equals(item)) {
                    GbInspectionItemPo itemPo = map.get(item);
                    if (ObjUtil.isNull(itemPo)) {
                        itemPo = new GbInspectionItemPo(report.getId(), vehicleChecker.getCategory(), vehicleChecker.getType(), item);
                    }
                    itemPo.getVehicleSet().add(vin);
                    itemPo.setTotalDataCount(itemPo.getTotalDataCount() + vehicleChecker.getCount());
                    if (vehicleChecker.getErrorCount() > 0) {
                        itemPo.getErrorVehicleSet().add(vin);
                    }
                    itemPo.setErrorDataCount(itemPo.getErrorDataCount() + vehicleChecker.getErrorCount());
                    map.put(item, itemPo);
                }
            });
        });
        map.forEach((key, item) -> {
            item.setTotalVehicleCount((long) item.getVehicleSet().size());
            item.setErrorVehicleCount((long) item.getErrorVehicleSet().size());
            report.getItems().add(item);
        });
    }

}
