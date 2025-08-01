package net.hwyz.iov.cloud.tsp.rsms.api.contract;

import lombok.*;
import net.hwyz.iov.cloud.framework.common.web.domain.BaseRequest;

import java.util.Date;

/**
 * 管理后台上报车辆
 *
 * @author hwyz_leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReportVehicleMpt extends BaseRequest {

    /**
     * 主键
     */
    private Long id;

    /**
     * 车架号
     */
    private String vin;

    /**
     * 车辆更新时间
     */
    private Date vehicleTime;

    /**
     * 离线天数
     */
    private Integer offlineDays;

    /**
     * 上报状态
     */
    private Integer reportState;

    /**
     * 车载终端所使用SIM卡ICCID编号
     */
    private String iccid;

    /**
     * 车辆备案时所用车辆型号
     */
    private String model;

    /**
     * 驱动电机在整车中的布置型式及位置，如轮边电机、轮毂电机、前后双电机等
     */
    private String driveMotorType;

    /**
     * 整车最高车速
     */
    private Short maxSpeed;

    /**
     * 在纯电行驶状态下的续驶里程（工况法）
     */
    private Short evRange;

    /**
     * 各挡位下的传动比，CVT无此项
     */
    private String gearRatio;

    /**
     * 电池相关参数
     */
    private String batteryParam;

    /**
     * 驱动电机相关参数
     */
    private String driveMotorParam;

    /**
     * 通用报警预值
     */
    private String alarmDefault;

    /**
     * 发动机编号
     */
    private String engineSn;

    /**
     * 燃油类型
     */
    private String fuelType;

    /**
     * 燃油标号
     */
    private String fuelMark;

    /**
     * 最大输出功率
     */
    private String engineMaxPower;

    /**
     * 最大输出转矩
     */
    private String engineMaxTorque;

    /**
     * 创建时间
     */
    private Date createTime;

}
