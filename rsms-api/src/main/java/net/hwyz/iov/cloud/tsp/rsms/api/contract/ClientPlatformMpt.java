package net.hwyz.iov.cloud.tsp.rsms.api.contract;

import lombok.*;
import net.hwyz.iov.cloud.framework.common.web.domain.BaseRequest;

import java.util.Date;

/**
 * 管理后台客户端平台
 *
 * @author hwyz_leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ClientPlatformMpt extends BaseRequest {

    /**
     * 主键
     */
    private Long id;

    /**
     * 服务端平台代码
     */
    private String serverPlatformCode;

    /**
     * 唯一识别码
     */
    private String uniqueCode;

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
     * 连接统计
     */
    private String connectStat;

    /**
     * 登录统计
     */
    private String loginStat;

    /**
     * 创建时间
     */
    private Date createTime;

}
