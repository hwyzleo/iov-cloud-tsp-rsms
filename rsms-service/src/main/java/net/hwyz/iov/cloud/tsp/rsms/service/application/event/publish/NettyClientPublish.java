package net.hwyz.iov.cloud.tsp.rsms.service.application.event.publish;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.service.application.event.event.NettyClientConnectEvent;
import net.hwyz.iov.cloud.tsp.rsms.service.domain.client.model.ClientPlatformDo;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Netty客户端事件发布类
 *
 * @author hwyz_leo
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NettyClientPublish {

    private final ApplicationContext ctx;

    /**
     * Netty客户端建立连接成功
     *
     * @param clientPlatform 客户端平台
     */
    public void connectSuccess(ClientPlatformDo clientPlatform) {
        logger.info("发送Netty客户端[{}]建立连接成功事件", clientPlatform.getUniqueKey());
        ctx.publishEvent(new NettyClientConnectEvent(clientPlatform, true));
    }

    /**
     * Netty客户端断连
     *
     * @param clientPlatform 客户端平台
     */
    public void disconnect(ClientPlatformDo clientPlatform) {
        logger.info("发送Netty客户端[{}]断连事件", clientPlatform.getUniqueKey());
        ctx.publishEvent(new NettyClientConnectEvent(clientPlatform, false));
    }

}
