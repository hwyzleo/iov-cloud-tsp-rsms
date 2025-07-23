package net.hwyz.iov.cloud.tsp.rsms.client.domain.server.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.framework.common.domain.BaseDo;
import net.hwyz.iov.cloud.framework.common.domain.DomainObj;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbDataUnitEncryptType;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.ProtocolType;

import java.util.Set;

/**
 * 服务端平台领域对象
 *
 * @author hwyz_leo
 */
@Slf4j
@Getter
@SuperBuilder
public class ServerPlatformDo extends BaseDo<String> implements DomainObj<ServerPlatformDo> {

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
    private ProtocolType protocol;
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
     * 初始化
     */
    public void init() {
        stateInit();
    }

}
