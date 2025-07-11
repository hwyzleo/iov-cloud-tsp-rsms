package net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.po;

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

import java.util.Date;

/**
 * <p>
 * 客户端平台登录历史 数据对象
 * </p>
 *
 * @author hwyz_leo
 * @since 2025-07-10
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_client_platform_login_history")
public class ClientPlatformLoginHistoryPo extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 客户端平台主键
     */
    @TableField("client_platform_id")
    private Long clientPlatformId;

    /**
     * 主机名
     */
    @TableField("hostname")
    private String hostname;

    /**
     * 登录时间
     */
    @TableField("login_time")
    private Date loginTime;

    /**
     * 登录流水号
     */
    @TableField("login_sn")
    private Integer loginSn;

    /**
     * 登录结果
     */
    @TableField("login_result")
    private Boolean loginResult;

    /**
     * 登录失败原因：1-无应答，2-错误应答
     */
    @TableField("failure_reason")
    private Integer failureReason;

    /**
     * 连续登录失败次数
     */
    @TableField("failure_count")
    private Integer failureCount;

    /**
     * 登出时间
     */
    @TableField("logout_time")
    private Date logoutTime;
}
