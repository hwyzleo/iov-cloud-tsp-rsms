package net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.handler;

import cn.hutool.core.util.ObjUtil;
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 国标数据质量检测处理器基础类
 *
 * @author hwyz_leo
 */
public abstract class BaseInspectionHandler implements InspectionHandler {

    protected static final String CATEGORY_STANDARD = "STANDARD";
    protected static final String TYPE_ABNORMAL = "ABNORMAL";
    protected static final String TYPE_ABNORMAL_CONTINUOUS = "ABNORMAL_CONTINUOUS";
    protected static final String TYPE_INVALID = "INVALID";
    protected static final String TYPE_INVALID_CONTINUOUS = "INVALID_CONTINUOUS";
    protected static final String TYPE_NULL = "NULL";
    protected static final String TYPE_NULL_CONTINUOUS = "NULL_CONTINUOUS";
    protected static final String TYPE_RANGE = "RANGE";
    protected static final String TYPE_RANGE_CONTINUOUS = "RANGE_CONTINUOUS";

    @Override
    public void inspect(GbInspectionReportPo report, List<GbMessage> gbMessages) {
        Map<String, Map<String, AbstractChecker>> checkers = new HashMap<>();
        gbMessages.forEach(message -> {
            String vin = message.getVin();
            Date messageTime = message.getMessageTime();
            Map<String, AbstractChecker> vehicleCheckers = getVehicleChecker(vin, checkers);
            switch (message.getHeader().getCommandFlag()) {
                case REALTIME_REPORT -> {
                    GbRealtimeReportDataUnit dataUnit = (GbRealtimeReportDataUnit) message.getDataUnit();
                    dataUnit.getDataInfoList().forEach(dataInfo -> handleDataInfo(messageTime, dataInfo, vehicleCheckers));
                }
                case REISSUE_REPORT -> {
                    GbReissueReportDataUnit dataUnit = (GbReissueReportDataUnit) message.getDataUnit();
                    dataUnit.getDataInfoList().forEach(dataInfo -> handleDataInfo(messageTime, dataInfo, vehicleCheckers));
                }
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
    protected Map<String, AbstractChecker> getVehicleChecker(String vin, Map<String, Map<String, AbstractChecker>> checkers) {
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
     * @param dataInfo        数据信息
     * @param vehicleCheckers 车辆检查器
     */
    protected void handleDataInfo(Date messageTime, GbMessageDataInfo dataInfo, Map<String, AbstractChecker> vehicleCheckers) {
        switch (dataInfo.getDataInfoType()) {
            case VEHICLE -> {
                GbVehicleDataDataInfo vehicleDataInfo = (GbVehicleDataDataInfo) dataInfo;
                validate(messageTime, vehicleDataInfo.getVehicleState().getCode(), CheckItem.VEHICLE_STATE, vehicleCheckers);
                validate(messageTime, vehicleDataInfo.getChargingState().getCode(), CheckItem.CHARGING_STATE, vehicleCheckers);
                validate(messageTime, vehicleDataInfo.getRunningMode().getCode(), CheckItem.RUNNING_MODE, vehicleCheckers);
                validate(messageTime, vehicleDataInfo.getSpeed(), CheckItem.SPEED, vehicleCheckers);
                validate(messageTime, vehicleDataInfo.getTotalOdometer(), CheckItem.TOTAL_ODOMETER, vehicleCheckers);
                validate(messageTime, vehicleDataInfo.getTotalVoltage(), CheckItem.TOTAL_VOLTAGE, vehicleCheckers);
                validate(messageTime, vehicleDataInfo.getTotalCurrent(), CheckItem.TOTAL_CURRENT, vehicleCheckers);
                validate(messageTime, vehicleDataInfo.getDcdcState().getCode(), CheckItem.DCDC_STATE, vehicleCheckers);
                validate(messageTime, vehicleDataInfo.getSoc(), CheckItem.SOC, vehicleCheckers);
                validate(messageTime, vehicleDataInfo.getInsulationResistance(), CheckItem.INSULATION_RESISTANCE, vehicleCheckers);
            }
            case DRIVE_MOTOR -> {
                GbDriveMotorDataInfo driveMotorDataInfo = (GbDriveMotorDataInfo) dataInfo;
                driveMotorDataInfo.getDriveMotorList().forEach(driveMotor -> {
                    validate(messageTime, driveMotor.getState().getCode(), CheckItem.DRIVE_MOTOR_STATE, (int) driveMotor.getSn(), vehicleCheckers);
                    validate(messageTime, driveMotor.getControllerTemperature(), CheckItem.DRIVE_MOTOR_CONTROLLER_TEMPERATURE, (int) driveMotor.getSn(), vehicleCheckers);
                    validate(messageTime, driveMotor.getTorque(), CheckItem.DRIVE_MOTOR_TORQUE, (int) driveMotor.getSn(), vehicleCheckers);
                    validate(messageTime, driveMotor.getTemperature(), CheckItem.DRIVE_MOTOR_TEMPERATURE, (int) driveMotor.getSn(), vehicleCheckers);
                    validate(messageTime, driveMotor.getControllerInputVoltage(), CheckItem.DRIVE_MOTOR_CONTROLLER_INPUT_VOLTAGE, (int) driveMotor.getSn(), vehicleCheckers);
                    validate(messageTime, driveMotor.getControllerDcBusCurrent(), CheckItem.DRIVE_MOTOR_CONTROLLER_DC_BUS_CURRENT, (int) driveMotor.getSn(), vehicleCheckers);
                });
            }
            case FUEL_CELL -> {
                GbFuelCellDataInfo fuelCellDataInfo = (GbFuelCellDataInfo) dataInfo;
                validate(messageTime, fuelCellDataInfo.getVoltage(), CheckItem.FUEL_CELL_VOLTAGE, vehicleCheckers);
                validate(messageTime, fuelCellDataInfo.getCurrent(), CheckItem.FUEL_CELL_CURRENT, vehicleCheckers);
                validate(messageTime, fuelCellDataInfo.getConsumptionRate(), CheckItem.FUEL_CELL_CONSUMPTION_RATE, vehicleCheckers);
                validate(messageTime, fuelCellDataInfo.getTemperatureProbeCount(), CheckItem.FUEL_CELL_TEMPERATURE_PROBE_COUNT, vehicleCheckers);
                validate(messageTime, fuelCellDataInfo.getHydrogenSystemMaxTemperature(), CheckItem.FUEL_CELL_HYDROGEN_SYSTEM_MAX_TEMPERATURE, vehicleCheckers);
                validate(messageTime, fuelCellDataInfo.getHydrogenSystemMaxTemperatureProbe(), CheckItem.FUEL_CELL_HYDROGEN_SYSTEM_MAX_TEMPERATURE_PROBE, vehicleCheckers);
                validate(messageTime, fuelCellDataInfo.getHydrogenMaxConcentration(), CheckItem.FUEL_CELL_HYDROGEN_MAX_CONCENTRATION, vehicleCheckers);
                validate(messageTime, fuelCellDataInfo.getHydrogenMaxConcentrationSensor(), CheckItem.FUEL_CELL_HYDROGEN_MAX_CONCENTRATION_SENSOR, vehicleCheckers);
                validate(messageTime, fuelCellDataInfo.getHydrogenMaxPressure(), CheckItem.FUEL_CELL_HYDROGEN_MAX_PRESSURE, vehicleCheckers);
                validate(messageTime, fuelCellDataInfo.getHydrogenMaxPressureSensor(), CheckItem.FUEL_CELL_HYDROGEN_MAX_PRESSURE_SENSOR, vehicleCheckers);
                validate(messageTime, fuelCellDataInfo.getHighPressureDcdcState().getCode(), CheckItem.FUEL_CELL_HIGH_PRESSURE_DCDC_STATE, vehicleCheckers);
            }
            case ENGINE -> {
                GbEngineDataInfo engineDataInfo = (GbEngineDataInfo) dataInfo;
                validate(messageTime, engineDataInfo.getState().getCode(), CheckItem.ENGINE_STATE, vehicleCheckers);
                validate(messageTime, engineDataInfo.getCrankshaftSpeed(), CheckItem.ENGINE_CRANKSHAFT_SPEED, vehicleCheckers);
                validate(messageTime, engineDataInfo.getConsumptionRate(), CheckItem.ENGINE_CONSUMPTION_RATE, vehicleCheckers);
            }
            case EXTREMUM -> {
                GbExtremumDataInfo extremumDataInfo = (GbExtremumDataInfo) dataInfo;
                validate(messageTime, extremumDataInfo.getMaxVoltageBatteryDeviceNo(), CheckItem.MAX_VOLTAGE_BATTERY_DEVICE_NO, vehicleCheckers);
                validate(messageTime, extremumDataInfo.getMaxVoltageCellNo(), CheckItem.MAX_VOLTAGE_CELL_NO, vehicleCheckers);
                validate(messageTime, extremumDataInfo.getCellMaxVoltage(), CheckItem.CELL_MAX_VOLTAGE, vehicleCheckers);
                validate(messageTime, extremumDataInfo.getMinVoltageBatteryDeviceNo(), CheckItem.MIN_VOLTAGE_BATTERY_DEVICE_NO, vehicleCheckers);
                validate(messageTime, extremumDataInfo.getMinVoltageCellNo(), CheckItem.MIN_VOLTAGE_CELL_NO, vehicleCheckers);
                validate(messageTime, extremumDataInfo.getCellMinVoltage(), CheckItem.CELL_MIN_VOLTAGE, vehicleCheckers);
                validate(messageTime, extremumDataInfo.getMaxTemperatureDeviceNo(), CheckItem.MAX_TEMPERATURE_DEVICE_NO, vehicleCheckers);
                validate(messageTime, extremumDataInfo.getMaxTemperatureProbeNo(), CheckItem.MAX_TEMPERATURE_PROBE_NO, vehicleCheckers);
                validate(messageTime, extremumDataInfo.getMaxTemperature(), CheckItem.MAX_TEMPERATURE, vehicleCheckers);
                validate(messageTime, extremumDataInfo.getMinTemperatureDeviceNo(), CheckItem.MIN_TEMPERATURE_DEVICE_NO, vehicleCheckers);
                validate(messageTime, extremumDataInfo.getMinTemperatureProbeNo(), CheckItem.MIN_TEMPERATURE_PROBE_NO, vehicleCheckers);
                validate(messageTime, extremumDataInfo.getMinTemperature(), CheckItem.MIN_TEMPERATURE, vehicleCheckers);
            }
            case ALARM -> {
                GbAlarmDataInfo alarmDataInfo = (GbAlarmDataInfo) dataInfo;
                validate(messageTime, alarmDataInfo.getMaxAlarmLevel().getCode(), CheckItem.MAX_ALARM_LEVEL, vehicleCheckers);
                validate(messageTime, alarmDataInfo.getBatteryFaultCount(), CheckItem.BATTERY_FAULT_COUNT, vehicleCheckers);
                validate(messageTime, alarmDataInfo.getDriveMotorFaultCount(), CheckItem.DRIVE_MOTOR_FAULT_COUNT, vehicleCheckers);
                validate(messageTime, alarmDataInfo.getEngineFaultCount(), CheckItem.ENGINE_FAULT_COUNT, vehicleCheckers);
                validate(messageTime, alarmDataInfo.getOtherFaultCount(), CheckItem.OTHER_FAULT_COUNT, vehicleCheckers);
            }
        }
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
                        vehicleCheckers.put(mapKey, new NullValueChecker(defaultChecker.getVin(), CATEGORY_STANDARD, type, itemKey));
                case TYPE_NULL_CONTINUOUS ->
                        vehicleCheckers.put(mapKey, new ContinuousNullValueChecker(defaultChecker.getVin(), CATEGORY_STANDARD, type, itemKey));
                case TYPE_RANGE ->
                        vehicleCheckers.put(mapKey, new RangeValueChecker(defaultChecker.getVin(), CATEGORY_STANDARD, type, itemKey, item.getMinValue(), item.getMaxValue()));
                case TYPE_RANGE_CONTINUOUS ->
                        vehicleCheckers.put(mapKey, new ContinuousRangeValueChecker(defaultChecker.getVin(), CATEGORY_STANDARD, type, itemKey, item.getMinValue(), item.getMaxValue()));
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
     */
    public void validate(Date messageTime, int value, CheckItem item, Map<String, AbstractChecker> vehicleCheckers) {
        validate(messageTime, value, item, null, vehicleCheckers);
    }

    /**
     * 验证检查项相关类型
     *
     * @param messageTime     消息时间
     * @param value           检查项值
     * @param item            检查项
     * @param sn              检查项序号
     * @param vehicleCheckers 车辆检查器
     */
    public abstract void validate(Date messageTime, int value, CheckItem item, Integer sn, Map<String, AbstractChecker> vehicleCheckers);

    /**
     * 汇总所有车辆检查数据
     *
     * @param report   检查报告
     * @param checkers 检查项
     */
    private void summarize(GbInspectionReportPo report, Map<String, Map<String, AbstractChecker>> checkers) {
        Map<String, GbInspectionItemPo> map = new HashMap<>();
        checkers.forEach((vin, vehicleCheckers) -> {
            vehicleCheckers.forEach((item, checker) -> {
                GbInspectionItemPo itemPo = map.get(item);
                if (ObjUtil.isNull(itemPo)) {
                    itemPo = new GbInspectionItemPo(report.getId(), checker.getCategory(), item);
                }
                itemPo.getVehicleSet().add(vin);
                itemPo.setTotalDataCount(itemPo.getTotalDataCount() + checker.getCount());
                if (checker.getErrorCount() > 0) {
                    itemPo.getErrorVehicleSet().add(vin);
                }
                itemPo.setErrorDataCount(itemPo.getErrorDataCount() + checker.getErrorCount());
            });
        });
        map.forEach((key, item) -> {
            item.setTotalVehicleCount((long) item.getVehicleSet().size());
            item.setErrorVehicleCount((long) item.getErrorVehicleSet().size());
            report.getItems().add(item);
        });
    }

}
