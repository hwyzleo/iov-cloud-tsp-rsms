package net.hwyz.iov.cloud.tsp.rsms.client.application.service.message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.ProtocolMessage;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.model.ClientPlatformDo;

/**
 * 协议处理类
 *
 * @author hwyz_leo
 */
@Slf4j
public abstract class MessageHandler extends SimpleChannelInboundHandler<ProtocolMessage> {

    /**
     * 客户端平台
     */
    protected ClientPlatformDo clientPlatform;

    abstract protected void channelRead0(ChannelHandlerContext ctx, ProtocolMessage message) throws Exception;

    public void setClientPlatform(ClientPlatformDo clientPlatform) {
        this.clientPlatform = clientPlatform;
    }
}
