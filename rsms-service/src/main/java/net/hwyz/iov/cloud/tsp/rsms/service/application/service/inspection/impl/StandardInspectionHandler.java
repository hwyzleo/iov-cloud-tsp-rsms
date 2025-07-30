package net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.json.JSONObject;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessage;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessageDataInfo;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.datainfo.*;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.dataunit.GbRealtimeReportDataUnit;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.dataunit.GbReissueReportDataUnit;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.InspectionHandler;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.GbInspectionItemPo;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.GbInspectionReportPo;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据规范性检测处理器
 * 异常值和无效值以及连续性
 *
 * @author hwyz_leo
 */
@Component
public class StandardInspectionHandler extends BaseInspectionHandler implements InspectionHandler {

    public enum Item {
        /**
         * 默认项
         */
        DEFAULT(0, 0),
        /**
         * 车辆状态
         **/
        VEHICLE_STATE(254, 255),
        /**
         * 充电状态
         **/
        CHARGING_STATE(254, 255),
        /**
         * 运行模式
         **/
        RUNNING_MODE(254, 255),
        /**
         * 车速
         **/
        SPEED(65534, 65535),
        /**
         * 累计里程
         **/
        TOTAL_ODOMETER(-2, -1),
        /**
         * 总电压
         **/
        TOTAL_VOLTAGE(65534, 65535),
        /**
         * 总电流
         **/
        TOTAL_CURRENT(65534, 65535),
        /**
         * DC-DC状态
         **/
        DCDC_STATE(254, 255),
        /**
         * SOC
         **/
        SOC(254, 255),
        /**
         * 绝缘电阻
         **/
        INSULATION_RESISTANCE(65534, 65535),
        /**
         * 驱动电机状态
         **/
        DRIVE_MOTOR_STATE(254, 255),
        /**
         * 驱动电机控制器温度
         **/
        DRIVE_MOTOR_CONTROLLER_TEMPERATURE(254, 255),
        /**
         * 驱动电机转速
         **/
        DRIVE_MOTOR_SPEED(65534, 65535),
        /**
         * 驱动电机转矩
         **/
        DRIVE_MOTOR_TORQUE(65534, 65535),
        /**
         * 驱动电机温度
         **/
        DRIVE_MOTOR_TEMPERATURE(254, 255),
        /**
         * 驱动电机控制器输入电压
         **/
        DRIVE_MOTOR_CONTROLLER_INPUT_VOLTAGE(65534, 65535),
        /**
         * 驱动电机控制器直流母线电流
         **/
        DRIVE_MOTOR_CONTROLLER_DC_BUS_CURRENT(65534, 65535),
        /**
         * 燃料电池电压
         **/
        FUEL_CELL_VOLTAGE(65534, 65535),
        /**
         * 燃料电池电流
         **/
        FUEL_CELL_CURRENT(65534, 65535),
        /**
         * 燃料消耗率
         **/
        FUEL_CELL_CONSUMPTION_RATE(65534, 65535),
        /**
         * 燃料电池温度探针总数
         **/
        FUEL_CELL_TEMPERATURE_PROBE_COUNT(65534, 65535),
        /**
         * 氢系统中最高温度
         **/
        FUEL_CELL_HYDROGEN_SYSTEM_MAX_TEMPERATURE(65534, 65535),
        /**
         * 氢系统中最高温度探针代号
         */
        FUEL_CELL_HYDROGEN_SYSTEM_MAX_TEMPERATURE_PROBE(254, 255),
        /**
         * 氢气最高浓度
         */
        FUEL_CELL_HYDROGEN_MAX_CONCENTRATION(65534, 65535),
        /**
         * 氢气最高浓度传感器代号
         */
        FUEL_CELL_HYDROGEN_MAX_CONCENTRATION_SENSOR(254, 255),
        /**
         * 氢气最高压力
         */
        FUEL_CELL_HYDROGEN_MAX_PRESSURE(65534, 65535),
        /**
         * 氢气最高压力传感器代号
         */
        FUEL_CELL_HYDROGEN_MAX_PRESSURE_SENSOR(254, 255),
        /**
         * 高压DC/DC状态
         */
        FUEL_CELL_HIGH_PRESSURE_DCDC_STATE(254, 255),
        /**
         * 发动机状态
         */
        ENGINE_STATE(254, 255),
        /**
         * 曲轴转速
         */
        ENGINE_CRANKSHAFT_SPEED(65534, 65535),
        /**
         * 燃料消耗率
         **/
        ENGINE_CONSUMPTION_RATE(65534, 65535),
        /**
         * 最高电压电池子系统号
         */
        MAX_VOLTAGE_BATTERY_DEVICE_NO(254, 255),
        /**
         * 最高电压电池单体代号
         */
        MAX_VOLTAGE_CELL_NO(254, 255),
        /**
         * 电池单体电压最高值
         */
        CELL_MAX_VOLTAGE(65534, 65535),
        /**
         * 最低电压电池子系统号
         */
        MIN_VOLTAGE_BATTERY_DEVICE_NO(254, 255),
        /**
         * 最低电压电池单体代号
         */
        MIN_VOLTAGE_CELL_NO(254, 255),
        /**
         * 电池单体电压最低值
         */
        CELL_MIN_VOLTAGE(65534, 65535),
        /**
         * 最高温度子系统号
         */
        MAX_TEMPERATURE_DEVICE_NO(254, 255),
        /**
         * 最高温度探针序号
         */
        MAX_TEMPERATURE_PROBE_NO(254, 255),
        /**
         * 最高温度
         */
        MAX_TEMPERATURE(254, 255),
        /**
         * 最低温度子系统号
         */
        MIN_TEMPERATURE_DEVICE_NO(254, 255),
        /**
         * 最低温度探针序号
         */
        MIN_TEMPERATURE_PROBE_NO(254, 255),
        /**
         * 最低温度
         */
        MIN_TEMPERATURE(254, 255),
        /**
         * 最高报警等级
         */
        MAX_ALARM_LEVEL(254, 255),
        /**
         * 可充电储能装置故障总数
         */
        BATTERY_FAULT_COUNT(254, 255),
        /**
         * 驱动电机故障总数
         */
        DRIVE_MOTOR_FAULT_COUNT(254, 255),
        /**
         * 发动机故障总数
         */
        ENGINE_FAULT_COUNT(254, 255),
        /**
         * 其他故障总数
         */
        OTHER_FAULT_COUNT(254, 255);

        /**
         * 异常值
         */
        private final Integer abnormalValue;
        /**
         * 无效值
         */
        private final Integer invalidValue;

        Item(Integer abnormalValue, Integer invalidValue) {
            this.abnormalValue = abnormalValue;
            this.invalidValue = invalidValue;
        }
    }

    @Override
    public void inspect(GbInspectionReportPo report, List<GbMessage> gbMessages) {
        Map<String, Map<String, StandardChecker>> checkers = new HashMap<>();
        gbMessages.forEach(message -> {
            String vin = message.getVin();
            Date messageTime = message.getMessageTime();
            Map<String, StandardChecker> vehicleCheckers = getChecker(vin, messageTime, checkers);
            switch (message.getHeader().getCommandFlag()) {
                case REALTIME_REPORT -> {
                    GbRealtimeReportDataUnit dataUnit = (GbRealtimeReportDataUnit) message.getDataUnit();
                    dataUnit.getDataInfoList().forEach(dataInfo -> handleDataInfo(dataInfo, vehicleCheckers));
                }
                case REISSUE_REPORT -> {
                    GbReissueReportDataUnit dataUnit = (GbReissueReportDataUnit) message.getDataUnit();
                    dataUnit.getDataInfoList().forEach(dataInfo -> handleDataInfo(dataInfo, vehicleCheckers));
                }
            }
        });
        summarize(report, checkers);
    }

    /**
     * 获取车辆对应检查器
     *
     * @param vin         车架号
     * @param messageTime 消息时间
     * @param checkers    车辆检查器Map
     * @return 检查器
     */
    private Map<String, StandardChecker> getChecker(String vin, Date messageTime, Map<String, Map<String, StandardChecker>> checkers) {
        if (!checkers.containsKey(vin)) {
            checkers.put(vin, initVehicleChecker(vin));
        }
        Map<String, StandardChecker> vehicleCheckers = checkers.get(vin);
        vehicleCheckers.get(Item.DEFAULT.name()).setMessageTime(messageTime);
        return vehicleCheckers;
    }

    /**
     * 初始化车辆检查器
     */
    private Map<String, StandardChecker> initVehicleChecker(String vin) {
        Map<String, StandardChecker> vehicleCheckers = new HashMap<>();
        vehicleCheckers.put(Item.DEFAULT.name(), new StandardChecker(vin, Item.DEFAULT.name(), Item.DEFAULT.abnormalValue, Item.DEFAULT.invalidValue));
        return vehicleCheckers;
    }

    /**
     * 获取车辆对应数据项检查器
     *
     * @param item            数据项
     * @param vehicleCheckers 车辆检查器
     * @return 数据项检查器
     */
    private StandardChecker getVehicleChecker(Item item, Map<String, StandardChecker> vehicleCheckers) {
        return getVehicleChecker(item, null, vehicleCheckers);
    }

    /**
     * 获取车辆对应数据项检查器
     *
     * @param item            数据项
     * @param vehicleCheckers 车辆检查器
     * @return 数据项检查器
     */
    private StandardChecker getVehicleChecker(Item item, Integer sn, Map<String, StandardChecker> vehicleCheckers) {
        StandardChecker defaultChecker = vehicleCheckers.get(Item.DEFAULT.name());
        String key = item.name();
        if (ObjUtil.isNull(sn)) {
            key += sn;
        }
        if (!vehicleCheckers.containsKey(key)) {
            vehicleCheckers.put(key, new StandardChecker(defaultChecker.getVin(), key, item.abnormalValue, item.invalidValue));
        }
        StandardChecker checker = vehicleCheckers.get(key);
        checker.setMessageTime(defaultChecker.getMessageTime());
        return checker;
    }

    /**
     * 处理数据信息
     *
     * @param dataInfo        数据信息
     * @param vehicleCheckers 车辆检查器
     */
    private void handleDataInfo(GbMessageDataInfo dataInfo, Map<String, StandardChecker> vehicleCheckers) {
        switch (dataInfo.getDataInfoType()) {
            case VEHICLE -> {
                GbVehicleDataDataInfo vehicleDataInfo = (GbVehicleDataDataInfo) dataInfo;
                validate(vehicleDataInfo.getVehicleState().getCode(), getVehicleChecker(Item.VEHICLE_STATE, vehicleCheckers));
                validate(vehicleDataInfo.getChargingState().getCode(), getVehicleChecker(Item.CHARGING_STATE, vehicleCheckers));
                validate(vehicleDataInfo.getRunningMode().getCode(), getVehicleChecker(Item.RUNNING_MODE, vehicleCheckers));
                validate(vehicleDataInfo.getSpeed(), getVehicleChecker(Item.SPEED, vehicleCheckers));
                validate(vehicleDataInfo.getTotalOdometer(), getVehicleChecker(Item.TOTAL_ODOMETER, vehicleCheckers));
                validate(vehicleDataInfo.getTotalVoltage(), getVehicleChecker(Item.TOTAL_VOLTAGE, vehicleCheckers));
                validate(vehicleDataInfo.getTotalCurrent(), getVehicleChecker(Item.TOTAL_CURRENT, vehicleCheckers));
                validate(vehicleDataInfo.getDcdcState().getCode(), getVehicleChecker(Item.DCDC_STATE, vehicleCheckers));
                validate(vehicleDataInfo.getSoc(), getVehicleChecker(Item.SOC, vehicleCheckers));
                validate(vehicleDataInfo.getInsulationResistance(), getVehicleChecker(Item.INSULATION_RESISTANCE, vehicleCheckers));
            }
            case DRIVE_MOTOR -> {
                GbDriveMotorDataInfo driveMotorDataInfo = (GbDriveMotorDataInfo) dataInfo;
                driveMotorDataInfo.getDriveMotorList().forEach(driveMotor -> {
                    validate(driveMotor.getState().getCode(), getVehicleChecker(Item.DRIVE_MOTOR_STATE, (int) driveMotor.getSn(), vehicleCheckers));
                    validate(driveMotor.getControllerTemperature(), getVehicleChecker(Item.DRIVE_MOTOR_CONTROLLER_TEMPERATURE, (int) driveMotor.getSn(), vehicleCheckers));
                    validate(driveMotor.getTorque(), getVehicleChecker(Item.DRIVE_MOTOR_TORQUE, (int) driveMotor.getSn(), vehicleCheckers));
                    validate(driveMotor.getTemperature(), getVehicleChecker(Item.DRIVE_MOTOR_TEMPERATURE, (int) driveMotor.getSn(), vehicleCheckers));
                    validate(driveMotor.getControllerInputVoltage(), getVehicleChecker(Item.DRIVE_MOTOR_CONTROLLER_INPUT_VOLTAGE, (int) driveMotor.getSn(), vehicleCheckers));
                    validate(driveMotor.getControllerDcBusCurrent(), getVehicleChecker(Item.DRIVE_MOTOR_CONTROLLER_DC_BUS_CURRENT, (int) driveMotor.getSn(), vehicleCheckers));
                });
            }
            case FUEL_CELL -> {
                GbFuelCellDataInfo fuelCellDataInfo = (GbFuelCellDataInfo) dataInfo;
                validate(fuelCellDataInfo.getVoltage(), getVehicleChecker(Item.FUEL_CELL_VOLTAGE, vehicleCheckers));
                validate(fuelCellDataInfo.getCurrent(), getVehicleChecker(Item.FUEL_CELL_CURRENT, vehicleCheckers));
                validate(fuelCellDataInfo.getConsumptionRate(), getVehicleChecker(Item.FUEL_CELL_CONSUMPTION_RATE, vehicleCheckers));
                validate(fuelCellDataInfo.getTemperatureProbeCount(), getVehicleChecker(Item.FUEL_CELL_TEMPERATURE_PROBE_COUNT, vehicleCheckers));
                validate(fuelCellDataInfo.getHydrogenSystemMaxTemperature(), getVehicleChecker(Item.FUEL_CELL_HYDROGEN_SYSTEM_MAX_TEMPERATURE, vehicleCheckers));
                validate(fuelCellDataInfo.getHydrogenSystemMaxTemperatureProbe(), getVehicleChecker(Item.FUEL_CELL_HYDROGEN_SYSTEM_MAX_TEMPERATURE_PROBE, vehicleCheckers));
                validate(fuelCellDataInfo.getHydrogenMaxConcentration(), getVehicleChecker(Item.FUEL_CELL_HYDROGEN_MAX_CONCENTRATION, vehicleCheckers));
                validate(fuelCellDataInfo.getHydrogenMaxConcentrationSensor(), getVehicleChecker(Item.FUEL_CELL_HYDROGEN_MAX_CONCENTRATION_SENSOR, vehicleCheckers));
                validate(fuelCellDataInfo.getHydrogenMaxPressure(), getVehicleChecker(Item.FUEL_CELL_HYDROGEN_MAX_PRESSURE, vehicleCheckers));
                validate(fuelCellDataInfo.getHydrogenMaxPressureSensor(), getVehicleChecker(Item.FUEL_CELL_HYDROGEN_MAX_PRESSURE_SENSOR, vehicleCheckers));
                validate(fuelCellDataInfo.getHighPressureDcdcState().getCode(), getVehicleChecker(Item.FUEL_CELL_HIGH_PRESSURE_DCDC_STATE, vehicleCheckers));
            }
            case ENGINE -> {
                GbEngineDataInfo engineDataInfo = (GbEngineDataInfo) dataInfo;
                validate(engineDataInfo.getState().getCode(), getVehicleChecker(Item.ENGINE_STATE, vehicleCheckers));
                validate(engineDataInfo.getCrankshaftSpeed(), getVehicleChecker(Item.ENGINE_CRANKSHAFT_SPEED, vehicleCheckers));
                validate(engineDataInfo.getConsumptionRate(), getVehicleChecker(Item.ENGINE_CONSUMPTION_RATE, vehicleCheckers));
            }
            case EXTREMUM -> {
                GbExtremumDataInfo extremumDataInfo = (GbExtremumDataInfo) dataInfo;
                validate(extremumDataInfo.getMaxVoltageBatteryDeviceNo(), getVehicleChecker(Item.MAX_VOLTAGE_BATTERY_DEVICE_NO, vehicleCheckers));
                validate(extremumDataInfo.getMaxVoltageCellNo(), getVehicleChecker(Item.MAX_VOLTAGE_CELL_NO, vehicleCheckers));
                validate(extremumDataInfo.getCellMaxVoltage(), getVehicleChecker(Item.CELL_MAX_VOLTAGE, vehicleCheckers));
                validate(extremumDataInfo.getMinVoltageBatteryDeviceNo(), getVehicleChecker(Item.MIN_VOLTAGE_BATTERY_DEVICE_NO, vehicleCheckers));
                validate(extremumDataInfo.getMinVoltageCellNo(), getVehicleChecker(Item.MIN_VOLTAGE_CELL_NO, vehicleCheckers));
                validate(extremumDataInfo.getCellMinVoltage(), getVehicleChecker(Item.CELL_MIN_VOLTAGE, vehicleCheckers));
                validate(extremumDataInfo.getMaxTemperatureDeviceNo(), getVehicleChecker(Item.MAX_TEMPERATURE_DEVICE_NO, vehicleCheckers));
                validate(extremumDataInfo.getMaxTemperatureProbeNo(), getVehicleChecker(Item.MAX_TEMPERATURE_PROBE_NO, vehicleCheckers));
                validate(extremumDataInfo.getMaxTemperature(), getVehicleChecker(Item.MAX_TEMPERATURE, vehicleCheckers));
                validate(extremumDataInfo.getMinTemperatureDeviceNo(), getVehicleChecker(Item.MIN_TEMPERATURE_DEVICE_NO, vehicleCheckers));
                validate(extremumDataInfo.getMinTemperatureProbeNo(), getVehicleChecker(Item.MIN_TEMPERATURE_PROBE_NO, vehicleCheckers));
                validate(extremumDataInfo.getMinTemperature(), getVehicleChecker(Item.MIN_TEMPERATURE, vehicleCheckers));
            }
            case ALARM -> {
                GbAlarmDataInfo alarmDataInfo = (GbAlarmDataInfo) dataInfo;
                validate(alarmDataInfo.getMaxAlarmLevel().getCode(), getVehicleChecker(Item.MAX_ALARM_LEVEL, vehicleCheckers));
                validate(alarmDataInfo.getBatteryFaultCount(), getVehicleChecker(Item.BATTERY_FAULT_COUNT, vehicleCheckers));
                validate(alarmDataInfo.getDriveMotorFaultCount(), getVehicleChecker(Item.DRIVE_MOTOR_FAULT_COUNT, vehicleCheckers));
                validate(alarmDataInfo.getEngineFaultCount(), getVehicleChecker(Item.ENGINE_FAULT_COUNT, vehicleCheckers));
                validate(alarmDataInfo.getOtherFaultCount(), getVehicleChecker(Item.OTHER_FAULT_COUNT, vehicleCheckers));
            }
        }
    }

    /**
     * 验证检查项
     *
     * @param value   检查项值
     * @param checker 检查器
     */
    private void validate(int value, StandardChecker checker) {
        checker.setCount(checker.getCount() + 1);
        if (value == checker.getAbnormalValue()) {
            checker.setAbnormalCount(checker.getAbnormalCount() + 1);
            if (checker.getAbnormalStartTime() == null) {
                checker.setAbnormalStartTime(checker.getMessageTime());
                checker.setAbnormalFrames(1L);
            } else {
                checker.setAbnormalEndTime(checker.getMessageTime());
                checker.setAbnormalFrames(checker.getAbnormalFrames() + 1);
            }
        } else if (value == checker.getInvalidValue()) {
            checker.setInvalidCount(checker.getInvalidCount() + 1);
            if (checker.getInvalidStartTime() == null) {
                checker.setInvalidStartTime(checker.getMessageTime());
                checker.setInvalidFrames(1L);
            } else {
                checker.setInvalidEndTime(checker.getMessageTime());
                checker.setInvalidFrames(checker.getInvalidFrames() + 1);
            }
        } else {
            if (checker.getAbnormalStartTime() != null) {
                if (checker.getAbnormalEndTime() != null) {
                    JSONObject continuous = new JSONObject();
                    continuous.set("startTime", checker.getAbnormalStartTime());
                    continuous.set("endTime", checker.getAbnormalEndTime());
                    continuous.set("frames", checker.getAbnormalFrames());
                    checker.getAbnormalContinuous().add(continuous);
                }
                checker.setAbnormalStartTime(null);
                checker.setAbnormalEndTime(null);
                checker.setAbnormalFrames(0L);
            }
            if (checker.getInvalidStartTime() != null) {
                if (checker.getInvalidEndTime() != null) {
                    JSONObject continuous = new JSONObject();
                    continuous.set("startTime", checker.getInvalidStartTime());
                    continuous.set("endTime", checker.getInvalidEndTime());
                    continuous.set("frames", checker.getInvalidFrames());
                    checker.getInvalidContinuous().add(continuous);
                }
                checker.setInvalidStartTime(null);
                checker.setInvalidEndTime(null);
                checker.setInvalidFrames(0L);
            }
        }
    }

    /**
     * 汇总所有车辆检查数据
     *
     * @param report   检查报告
     * @param checkers 检查项
     */
    private void summarize(GbInspectionReportPo report, Map<String, Map<String, StandardChecker>> checkers) {
        Map<String, GbInspectionItemPo> map = new HashMap<>();
        checkers.forEach((vin, vehicleCheckers) -> {
            vehicleCheckers.forEach((item, checker) -> {
                // 分别记录异常与无效
                String abnormalKey = item + "-ABNORMAL";
                String invalidKey = item + "-INVALID";
                GbInspectionItemPo itemAbnormal = map.get(abnormalKey);
                if (ObjUtil.isNull(itemAbnormal)) {
                    itemAbnormal = new GbInspectionItemPo(report.getId(), checker.getCategory(), abnormalKey);
                }
                GbInspectionItemPo itemInvalid = map.get(invalidKey);
                if (ObjUtil.isNull(itemInvalid)) {
                    itemInvalid = new GbInspectionItemPo(report.getId(), checker.getCategory(), invalidKey);
                }
                itemAbnormal.getVehicleSet().add(vin);
                itemAbnormal.setTotalDataCount(itemAbnormal.getTotalDataCount() + checker.getCount());
                if (checker.getAbnormalCount() > 0) {
                    itemAbnormal.getErrorVehicleSet().add(vin);
                }
                itemAbnormal.setErrorDataCount(itemAbnormal.getErrorDataCount() + checker.getAbnormalCount());
                itemInvalid.getVehicleSet().add(vin);
                itemInvalid.setTotalDataCount(itemInvalid.getTotalDataCount() + checker.getCount());
                if (checker.getInvalidCount() > 0) {
                    itemInvalid.getErrorVehicleSet().add(vin);
                }
                itemInvalid.setErrorDataCount(itemInvalid.getErrorDataCount() + checker.getInvalidCount());
            });
        });
        map.forEach((key, item) -> {
            item.setTotalVehicleCount((long) item.getVehicleSet().size());
            item.setErrorVehicleCount((long) item.getErrorVehicleSet().size());
            report.getItems().add(item);
        });
    }

}
