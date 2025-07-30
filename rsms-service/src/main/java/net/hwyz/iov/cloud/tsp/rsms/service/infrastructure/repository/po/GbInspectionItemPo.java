package net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Set;

import net.hwyz.iov.cloud.framework.mysql.po.BasePo;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 国标数据质量检测项 数据对象
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
@TableName("tb_gb_inspection_item")
public class GbInspectionItemPo extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 报告主键
     */
    @TableField("report_id")
    private Long reportId;

    /**
     * 检查项大类
     */
    @TableField("item_category")
    private String itemCategory;

    /**
     * 检查项小类
     */
    @TableField("item_type")
    private String itemType;

    /**
     * 检查项
     */
    @TableField("item_code")
    private String itemCode;

    /**
     * 总数量
     */
    @TableField("total_data_count")
    private Long totalDataCount;

    /**
     * 错误数量
     */
    @TableField("error_data_count")
    private Long errorDataCount;

    /**
     * 总车辆数
     */
    @TableField("total_vehicle_count")
    private Long totalVehicleCount;

    /**
     * 车辆集合
     */
    private Set<String> vehicleSet;

    /**
     * 错误车辆数
     */
    @TableField("error_vehicle_count")
    private Long errorVehicleCount;

    /**
     * 错误车辆集合
     */
    private Set<String> errorVehicleSet;

    public GbInspectionItemPo(Long reportId, String itemCategory, String itemCode) {
        this.reportId = reportId;
        this.itemCategory = itemCategory;
        this.itemCode = itemCode;
        this.totalDataCount = 0L;
        this.errorDataCount = 0L;
        this.totalVehicleCount = 0L;
        this.errorVehicleCount = 0L;
    }
}
