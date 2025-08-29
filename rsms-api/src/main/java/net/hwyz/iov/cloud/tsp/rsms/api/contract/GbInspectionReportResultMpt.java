package net.hwyz.iov.cloud.tsp.rsms.api.contract;

import cn.hutool.json.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 管理后台国标检测报告结果
 *
 * @author hwyz_leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GbInspectionReportResultMpt {

    /**
     * 主键
     */
    private Long id;

    /**
     * 检测开始时间
     */
    private Date startTime;

    /**
     * 检测结束时间
     */
    private Date endTime;

    /**
     * 报告类型：1-单车报告，2-车型报告
     */
    private Integer reportType;

    /**
     * 报告类型名称
     */
    private String reportTypeName;

    /**
     * 车型或车架号
     */
    private String vehicle;

    /**
     * 数据开始时间
     */
    private Date dataStartTime;

    /**
     * 数据结束时间
     */
    private Date dataEndTime;

    /**
     * 车辆数
     */
    private Long vehicleCount;

    /**
     * 错误车辆数
     */
    private Long vehicleErrorCount;

    /**
     * 错误车辆数数占比
     */
    private Double vehicleErrorPercentage;

    /**
     * 场景：0-不指定，1-车辆行驶，2-车辆充电，3-车辆报警，4-车辆补发，5-平台补发
     */
    private Integer scene;

    /**
     * 单车场景开始时间
     */
    private Date sceneStartTime;

    /**
     * 单车场景结束时间
     */
    private Date sceneEndTime;

    /**
     * 消息总条数
     */
    private Long messageCount;

    /**
     * 错误消息条数
     */
    private Long messageErrorCount;

    /**
     * 错误消息条数占比
     */
    private Double messageErrorPercentage;

    /**
     * 数据总条数
     */
    private Long dataCount;

    /**
     * 错误数据条数
     */
    private Long dataErrorCount;

    /**
     * 错误数据条数占比
     */
    private Double dataErrorPercentage;

    /**
     * 检测项
     */
    private JSONObject items;

    /**
     * 创建时间
     */
    private Date createTime;

}
