package net.hwyz.iov.cloud.tsp.rsms.api.contract;

import lombok.*;
import net.hwyz.iov.cloud.framework.common.web.domain.BaseRequest;

import java.util.Date;
import java.util.Map;

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
     * 企业唯一识别码
     */
    private String uniqueCode;

    /**
     * 采集频率
     */
    private Integer collectFrequency;

    /**
     * 上报频率
     */
    private Integer reportFrequency;

    /**
     * 数据加密方式
     */
    private Integer encryptType;

    /**
     * 数据加密KEY
     */
    private String encryptKey;

    /**
     * 是否启用
     */
    private Boolean enable;

    /**
     * 绑定主机名
     */
    private String hostname;

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
     * 已注册车辆数
     */
    private Integer vehicleCount;

    /**
     * 创建时间
     */
    private Date createTime;

}
