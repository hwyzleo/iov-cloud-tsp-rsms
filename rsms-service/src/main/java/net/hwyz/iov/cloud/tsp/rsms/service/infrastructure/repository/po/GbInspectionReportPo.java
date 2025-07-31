package net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import net.hwyz.iov.cloud.framework.mysql.po.BasePo;
import lombok.*;
import lombok.experimental.SuperBuilder;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessage;

/**
 * <p>
 * 国标数据质量检测报告 数据对象
 * </p>
 *
 * @author hwyz_leo
 * @since 2025-07-28
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_gb_inspection_report")
public class GbInspectionReportPo extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 检测开始时间
     */
    @TableField("start_time")
    private Date startTime;

    /**
     * 检测结束时间
     */
    @TableField("end_time")
    private Date endTime;

    /**
     * 报告类型：1-单车报告，2-车型报告
     */
    @TableField("report_type")
    private Integer reportType;

    /**
     * 报告状态：1-处理中，2-处理结束
     */
    @TableField("report_state")
    private Integer reportState;

    /**
     * 车型或车架号
     */
    @TableField("vehicle")
    private String vehicle;

    /**
     * 数据开始时间
     */
    @TableField("data_start_time")
    private Date dataStartTime;

    /**
     * 单车结束时间
     */
    @TableField("data_end_time")
    private Date dataEndTime;

    /**
     * 车辆数
     */
    @TableField("vehicle_count")
    private Long vehicleCount;

    /**
     * 错误车辆数
     */
    @TableField("vehicle_error_count")
    private Long vehicleErrorCount;

    /**
     * 场景：0-不指定，1-车辆行驶，2-车辆充电，3-车辆报警，4-车辆补发，5-平台补发
     */
    @TableField("scene")
    private Integer scene;

    /**
     * 单车场景开始时间
     */
    @TableField("scene_start_time")
    private Date sceneStartTime;

    /**
     * 单车场景结束时间
     */
    @TableField("scene_end_time")
    private Date sceneEndTime;

    /**
     * 数据总条数
     */
    @TableField("message_count")
    private Long messageCount;

    /**
     * 错误数据条数
     */
    @TableField("message_error_count")
    private Long messageErrorCount;

    /**
     * 数据总条数
     */
    @TableField("data_count")
    private Long dataCount;

    /**
     * 错误数据条数
     */
    @TableField("data_error_count")
    private Long dataErrorCount;

    /**
     * 检测项
     */
    private List<GbInspectionItemPo> items;

    /**
     * 错误数据
     */
    private Set<GbMessage> errorMessages;
}
