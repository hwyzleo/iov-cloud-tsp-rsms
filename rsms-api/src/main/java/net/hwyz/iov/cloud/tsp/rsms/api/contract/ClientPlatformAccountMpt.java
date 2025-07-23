package net.hwyz.iov.cloud.tsp.rsms.api.contract;

import lombok.*;
import net.hwyz.iov.cloud.framework.common.web.domain.BaseRequest;

import java.util.Date;
import java.util.Map;

/**
 * 管理后台客户端平台账号
 *
 * @author hwyz_leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ClientPlatformAccountMpt extends BaseRequest {

    /**
     * 主键
     */
    private Long id;

    /**
     * 客户端平台ID
     */
    private Long clientPlatformId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 绑定主机名
     */
    private String hostname;

    /**
     * 是否启用
     */
    private Boolean enable;

    /**
     * 连接状态
     */
    private Map<String, Boolean> connectState;

    /**
     * 连接统计
     */
    private String connectStat;

    /**
     * 登录状态
     */
    private Map<String, Boolean> loginState;

    /**
     * 登录统计
     */
    private String loginStat;

    /**
     * 创建时间
     */
    private Date createTime;

}
