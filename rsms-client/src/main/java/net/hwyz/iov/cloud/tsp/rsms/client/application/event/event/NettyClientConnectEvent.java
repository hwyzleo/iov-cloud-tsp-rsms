package net.hwyz.iov.cloud.tsp.rsms.client.application.event.event;

import lombok.Getter;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.model.ClientPlatformDo;

/**
 * Netty客户端连接事件
 *
 * @author hwyz_leo
 */
@Getter
public class NettyClientConnectEvent extends BaseEvent {

    /**
     * 客户端平台
     */
    private final ClientPlatformDo clientPlatform;
    /**
     * 连接结果
     */
    private final Boolean connectResult;

    public NettyClientConnectEvent(ClientPlatformDo clientPlatform, Boolean connectResult) {
        super();
        this.clientPlatform = clientPlatform;
        this.connectResult = connectResult;
    }

}
