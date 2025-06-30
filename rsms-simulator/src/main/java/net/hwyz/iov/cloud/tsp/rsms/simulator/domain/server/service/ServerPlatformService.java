package net.hwyz.iov.cloud.tsp.rsms.simulator.domain.server.service;

import cn.hutool.core.thread.ThreadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.simulator.domain.server.model.ServerPlatformDo;
import net.hwyz.iov.cloud.tsp.rsms.simulator.infrastructure.util.NettyServer;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * 服务端平台领域服务
 *
 * @author hwyz_leo
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ServerPlatformService {

    private final ApplicationContext ctx;

    /**
     * 启动
     *
     * @param serverPlatform 服务端平台
     */
    public void start(ServerPlatformDo serverPlatform) {
        logger.info("启动服务端平台[{}]", serverPlatform.getServerPlatformType());
        try {
            NettyServer nettyServer = ctx.getBean(NettyServer.class);
            serverPlatform.bindServer(nettyServer);
            ThreadUtil.newExecutor(1).submit(() -> {
                try {
                    nettyServer.start(serverPlatform);
                } catch (Exception e) {
                    logger.error("启动服务端平台[{}]失败", serverPlatform.getServerPlatformType(), e);
                }
            });
        } catch (Exception e) {
            logger.warn("启动服务端平台[{}]失败", serverPlatform.getServerPlatformType(), e);
        }
    }

}
