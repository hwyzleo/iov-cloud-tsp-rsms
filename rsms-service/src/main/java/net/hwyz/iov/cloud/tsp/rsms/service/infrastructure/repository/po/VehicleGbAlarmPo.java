package net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import net.hwyz.iov.cloud.framework.mysql.po.BasePo;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 车辆国标报警 数据对象
 * </p>
 *
 * @author hwyz_leo
 * @since 2025-07-17
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_vehicle_gb_alarm")
public class VehicleGbAlarmPo extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 车架号
     */
    @TableField("vin")
    private String vin;

    /**
     * 报警时间
     */
    @TableField("alarm_time")
    private Date alarmTime;

    /**
     * 恢复时间
     */
    @TableField("restoration_time")
    private Date restorationTime;

    /**
     * 报警标志位
     */
    @TableField("alarm_flag")
    private Integer alarmFlag;

    /**
     * 报警级别
     */
    @TableField("alarm_level")
    private Integer alarmLevel;

    /**
     * 消息数据
     */
    @TableField("message_data")
    private String messageData;
}
