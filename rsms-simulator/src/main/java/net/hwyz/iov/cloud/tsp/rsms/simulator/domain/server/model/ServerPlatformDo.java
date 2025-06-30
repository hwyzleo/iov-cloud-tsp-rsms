package net.hwyz.iov.cloud.tsp.rsms.simulator.domain.server.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.framework.common.domain.BaseDo;
import net.hwyz.iov.cloud.framework.common.domain.DomainObj;
import net.hwyz.iov.cloud.tsp.rsms.simulator.domain.contract.enums.ServerPlatformType;
import net.hwyz.iov.cloud.tsp.rsms.simulator.infrastructure.util.NettyServer;

/**
 * 服务端平台领域对象
 *
 * @author hwyz_leo
 */
@Slf4j
@Getter
@Setter
@SuperBuilder
public class ServerPlatformDo extends BaseDo<ServerPlatformType> implements DomainObj<ServerPlatformDo> {

    /**
     * 服务端平台类型
     */
    private ServerPlatformType serverPlatformType;
    /**
     * 服务端
     */
    private NettyServer server;

    /**
     * 初始化
     */
    public void init() {
        logger.info("服务端平台[{}]初始化", this.serverPlatformType);
        stateInit();
    }

    /**
     * 绑定服务端
     *
     * @param server 服务端
     */
    public void bindServer(NettyServer server) {
        this.server = server;
    }

}
