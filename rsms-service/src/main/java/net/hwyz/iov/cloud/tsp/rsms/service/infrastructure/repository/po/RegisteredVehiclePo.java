package net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import net.hwyz.iov.cloud.framework.mysql.po.BasePo;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 服务端平台已注册车辆 数据对象
 * </p>
 *
 * @author hwyz_leo
 * @since 2025-07-01
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_registered_vehicle")
public class RegisteredVehiclePo extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 服务端平台代码
     */
    @TableField("server_platform_code")
    private String serverPlatformCode;

    /**
     * 车架号
     */
    @TableField("vin")
    private String vin;

    /**
     * 上报状态
     */
    @TableField("report_state")
    private Integer reportState;

    /**
     * 车载终端所使用SIM卡ICCID编号
     */
    @TableField("iccid")
    private String iccid;

    /**
     * 车辆备案时所用车辆型号
     */
    @TableField("model")
    private String model;

    /**
     * 驱动电机在整车中的布置型式及位置，如轮边电机、轮毂电机、前后双电机等
     */
    @TableField("drive_motor_type")
    private String driveMotorType;

    /**
     * 整车最高车速
     */
    @TableField("max_speed")
    private Short maxSpeed;

    /**
     * 在纯电行驶状态下的续驶里程（工况法）
     */
    @TableField("ev_range")
    private Short evRange;

    /**
     * 各挡位下的传动比，CVT无此项
     */
    @TableField("gear_ratio")
    private String gearRatio;

    /**
     * 电池相关参数
     */
    @TableField("battery_param")
    private String batteryParam;

    /**
     * 驱动电机相关参数
     */
    @TableField("drive_motor_param")
    private String driveMotorParam;

    /**
     * 通用报警预值
     */
    @TableField("alarm_default")
    private String alarmDefault;

    /**
     * 发动机编号
     */
    @TableField("engine_sn")
    private String engineSn;

    /**
     * 燃油类型
     */
    @TableField("fuel_type")
    private String fuelType;

    /**
     * 燃油标号
     */
    @TableField("fuel_mark")
    private String fuelMark;

    /**
     * 最大输出功率
     */
    @TableField("engine_max_power")
    private String engineMaxPower;

    /**
     * 最大输出转矩
     */
    @TableField("engine_max_torque")
    private String engineMaxTorque;
}
