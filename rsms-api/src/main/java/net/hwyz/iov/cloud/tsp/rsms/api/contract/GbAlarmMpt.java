package net.hwyz.iov.cloud.tsp.rsms.api.contract;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * 管理后台国标报警数据数据信息
 *
 * @author hwyz_leo
 */
@Data
@Slf4j
@NoArgsConstructor
public class GbAlarmMpt {

    /**
     * 最高报警等级
     */
    private String maxAlarmLevel;

    /**
     * 通用报警标志
     */
    private Map<Integer, Boolean> alarmFlagMap;
    /**
     * 可充电储能装置故障总数N1
     * N1个可充电储能装置故障，有效值范围：0~252，“0xFE”表示异常，“0xFF”表示无效
     */
    private Integer batteryFaultCount;
    /**
     * 可充电储能装置故障代码列表
     */
    private List<Integer> batteryFaultList;
    /**
     * 驱动电机故障总数N2
     * N2个驱动电机故障，有效值范围：0~252，“0xFE”表示异常，“0xFF”表示无效
     */
    private Integer driveMotorFaultCount;
    /**
     * 驱动电机故障代码列表
     */
    private List<Integer> driveMotorFaultList;
    /**
     * 发动机故障总数N3
     * N3个发动机故障，有效值范围：0~252，“0xFE”表示异常，“0xFF”表示无效
     */
    private Integer engineFaultCount;
    /**
     * 发动机故障代码列表
     */
    private List<Integer> engineFaultList;
    /**
     * 其他故障总数N4
     * N4个其他故障，有效值范围：0~252，“0xFE”表示异常，“0xFF”表示无效
     */
    private Integer otherFaultCount;
    /**
     * 其他故障代码列表
     */
    private List<Integer> otherFaultList;

}
