package net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.hwyz.iov.cloud.framework.mysql.po.BasePo;

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
     * 客户端平台ID
     */
    @TableField("client_platform_id")
    private Long clientPlatformId;

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
     * 车辆备案时所用车辆型号
     */
    @TableField("model")
    private String model;

}
