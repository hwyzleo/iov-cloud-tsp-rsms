package net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection;

import lombok.Getter;

/**
 * 检查项枚举类
 *
 * @author hwyz_leo
 */
@Getter
public enum CheckItem {

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

    CheckItem(Integer abnormalValue, Integer invalidValue) {
        this.abnormalValue = abnormalValue;
        this.invalidValue = invalidValue;
    }

}
