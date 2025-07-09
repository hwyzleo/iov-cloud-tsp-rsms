package net.hwyz.iov.cloud.tsp.rsms.api.contract;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * 管理后台国标驱动电机数据信息
 *
 * @author hwyz_leo
 */
@Data
@Slf4j
@NoArgsConstructor
public class GbDriveMotorMpt {

    /**
     * 驱动电机顺序号
     * 有效值范围:1 ~ 253
     */
    private Integer sn;
    /**
     * 驱动电机状态
     */
    private String state;
    /**
     * 驱动电机控制器温度
     * 有效值范圃：0 ~ 250（数值偏移量40℃，表示-40℃ ~ +210℃)，最小计量单元：1℃，“0xFE”表示异常，“0xFF”表示无效
     */
    private Integer controllerTemperature;
    /**
     * 驱动电机转速
     * 有效值范围：0 ~ 65531（数值偏移量20000表示-20000 r/min ~ 45531 r/min),最小计量单元：1r/min,
     * “0xFF，0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private Integer speed;
    /**
     * 驱动电机转矩
     * 有效值范围：0 ~ 65531（数值偏移量20000表示-2000 N·m ~ 45531 N·m),最小计量单元：0.1 N·m,
     * “0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private BigDecimal torque;
    /**
     * 驱动电机温度
     * 有效值范围：0 ~ 250（数值偏移量40℃，表示-40 ℃ ~ +210 ℃)，最小计量单元：1℃，“0xFE”表示异常，“0xFF”表示无效
     */
    private Integer temperature;
    /**
     * 电机控制器输入电压
     * 有效值范围：0 ~ 60000（表示0V6000V),最小计量单元：0.1V,“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private BigDecimal controllerInputVoltage;
    /**
     * 电机控制器直流母线电流
     * 有效值范围：0 ~ 20000（数值偏移量1000A,表示-1000A ~ +1000A),最小计量单元：0.1A,“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private BigDecimal controllerDcBusCurrent;

}
