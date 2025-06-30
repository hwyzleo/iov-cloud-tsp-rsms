package net.hwyz.iov.cloud.tsp.rsms.service.domain.client.service;

import cn.hutool.core.thread.ThreadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.service.domain.client.model.ClientPlatformDo;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.util.NettyClient;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * 客户端平台领域服务
 *
 * @author hwyz_leo
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClientPlatformService {

    private final ApplicationContext ctx;

    /**
     * 启动
     *
     * @param clientPlatform 客户端平台
     */
    public void start(ClientPlatformDo clientPlatform) {
        logger.info("启动客户端平台[{}]", clientPlatform.getUniqueKey());
        try {
            NettyClient nettyClient = ctx.getBean(NettyClient.class);
            ThreadUtil.newExecutor(1).submit(() -> {
                try {
                    nettyClient.connect(clientPlatform);
                } catch (Exception e) {
                    logger.error("启动客户端平台[{}]失败", clientPlatform.getUniqueKey(), e);
                }
            });
        } catch (Exception e) {
            logger.warn("启动客户端平台[{}]失败", clientPlatform.getUniqueKey(), e);
        }
    }

}
