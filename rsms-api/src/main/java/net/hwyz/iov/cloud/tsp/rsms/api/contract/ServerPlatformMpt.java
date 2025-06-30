package net.hwyz.iov.cloud.tsp.rsms.api.contract;

import lombok.*;
import net.hwyz.iov.cloud.framework.common.web.domain.BaseRequest;

import java.util.Date;

/**
 * 管理后台服务端平台
 *
 * @author hwyz_leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ServerPlatformMpt extends BaseRequest {

    /**
     * 主键
     */
    private Long id;

    /**
     * 平台代码
     */
    private String code;

    /**
     * 平台名称
     */
    private String name;

    /**
     * 平台类型：1-国标，2-地标，3-企标
     */
    private Integer type;

    /**
     * 平台地址
     */
    private String url;

    /**
     * 平台端口
     */
    private Integer port;

    /**
     * 平台协议
     */
    private String protocol;

    /**
     * 采集频率
     */
    private Integer collectFrequency;

    /**
     * 上报频率
     */
    private Integer reportFrequency;

    /**
     * 是否读写同步
     */
    private Boolean readWriteSync;

    /**
     * 是否维持心跳
     */
    private Boolean heartbeat;

    /**
     * 数据加密方式
     */
    private Integer encryptType;

    /**
     * 数据加密KEY
     */
    private String encryptKey;

}
