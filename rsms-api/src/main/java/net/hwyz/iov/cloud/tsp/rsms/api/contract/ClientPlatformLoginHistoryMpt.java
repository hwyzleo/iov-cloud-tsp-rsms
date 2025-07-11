package net.hwyz.iov.cloud.tsp.rsms.api.contract;

import lombok.*;
import net.hwyz.iov.cloud.framework.common.web.domain.BaseRequest;

import java.util.Date;

/**
 * 管理后台客户端平台登录历史
 *
 * @author hwyz_leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ClientPlatformLoginHistoryMpt extends BaseRequest {

    /**
     * 主键
     */
    private Long id;

    /**
     * 客户端平台主键
     */
    private Long clientPlatformId;

    /**
     * 主机名
     */
    private String hostname;

    /**
     * 登录时间
     */
    private Date loginTime;

    /**
     * 登录流水号
     */
    private Integer loginSn;

    /**
     * 登录结果
     */
    private Boolean loginResult;

    /**
     * 登录失败原因：1-无应答，2-错误应答
     */
    private Integer failureReason;

    /**
     * 连续登录失败次数
     */
    private Integer failureCount;

    /**
     * 登出时间
     */
    private Date logoutTime;

    /**
     * 创建时间
     */
    private Date createTime;

}
