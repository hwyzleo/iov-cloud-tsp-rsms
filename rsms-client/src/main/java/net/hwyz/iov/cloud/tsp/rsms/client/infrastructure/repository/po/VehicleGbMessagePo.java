package net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.po;

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
 * 车辆国标消息 数据对象
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
@TableName("tb_vehicle_gb_message")
public class VehicleGbMessagePo extends BasePo {

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
     * 解析时间
     */
    @TableField("parse_time")
    private Date parseTime;

    /**
     * 消息时间
     */
    @TableField("message_time")
    private Date messageTime;

    /**
     * 命令标识
     */
    @TableField("command_flag")
    private String commandFlag;

    /**
     * 消息数据
     */
    @TableField("message_data")
    private String messageData;
}
