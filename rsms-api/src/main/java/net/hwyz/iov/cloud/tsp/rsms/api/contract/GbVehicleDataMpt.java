package net.hwyz.iov.cloud.tsp.rsms.api.contract;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * 管理后台国标整车数据数据信息
 *
 * @author hwyz_leo
 */
@Data
@Slf4j
@NoArgsConstructor
public class GbVehicleDataMpt {
    
    /**
     * 车辆状态
     */
    private String vehicleState;
    /**
     * 充电状态
     */
    private String chargerState;
    /**
     * 运行模式
     */
    private String runningMode;
    /**
     * 车速
     * 有效值范围：0 ~ 2200（表示0km/h ~ 220km/h),最小计量单元：0.1km/h,“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private BigDecimal speed;
    /**
     * 累计里程
     * 有效值范围：0 ~ 9999999（表示0km ~ 999999.9km),最小计量单元：0.1km。
     * “0xFF,0xFF,0xFF,0xFE”表示异常，“0xFF,0xFF,0xFF,0xFF”表示无效
     */
    private BigDecimal totalOdometer;
    /**
     * 总电压
     * 有效值范围：0 ~ 10000（表示0V ~ 1000V),最小计量单元：0.1V,“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private BigDecimal totalVoltage;
    /**
     * 总电流
     * 有效值范围：0 ~ 20000（偏移量1000A,表示-1000A ~ +1000A),最小计量单元：0.1A,“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private BigDecimal totalCurrent;
    /**
     * SOC
     * 有效值范圃：0 ~ 100（表示0% ~ 100%），最小计量单元：1%，“0xFE”表示异常，“OxFF”表示无效
     */
    private Integer soc;
    /**
     * DC/DC状态
     */
    private String dcdcState;
    /**
     * 有驱动力
     */
    private Boolean driving;
    /**
     * 有制动力
     */
    private Boolean braking;
    /**
     * 挡位
     */
    private String gear;
    /**
     * 绝缘电阻
     * 有效范围 0 ~ 60000（表示 0 kΩ ~ 60000 kΩ),最小计量单元：1 kΩ
     */
    private Integer insulationResistance;
    /**
     * 加速踏板行程值
     * 有效值范围：0 ~ 100（表示0% ~ 100%），最小计量单元：1%，“0xFE”表示异常，“0xFF”表示无效
     */
    private Integer acceleratorPedalPosition;
    /**
     * 制动踏板状态
     * 有效值范围：0 ~ 100（表示0% ~ 100%），最小计量单元：1%，“0”表示制动关的状态，
     * 在无具体行程值情祝下，用“0x65”即“101”表示制动有效状态，“0xFE”表示异常，“0xFF”表示无效
     */
    private Integer brakePedalPosition;

}
