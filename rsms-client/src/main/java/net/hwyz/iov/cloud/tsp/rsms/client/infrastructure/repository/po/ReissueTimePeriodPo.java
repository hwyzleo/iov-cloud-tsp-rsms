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
 * 平台补发时间段 数据对象
 * </p>
 *
 * @author hwyz_leo
 * @since 2025-07-16
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_reissue_time_period")
public class ReissueTimePeriodPo extends BasePo {

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
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 主机名
     */
    @TableField("hostname")
    private String hostname;

    /**
     * 平台中断开始时间
     */
    @TableField("start_time")
    private Date startTime;

    /**
     * 平台中断结束时间
     */
    @TableField("end_time")
    private Date endTime;

    /**
     * 补发开始时间
     */
    @TableField("reissue_time")
    private Date reissueTime;

    /**
     * 补发状态：0-未补发，1-正在补发，2-补发完成
     */
    @TableField("reissue_state")
    private Integer reissueState;
}
