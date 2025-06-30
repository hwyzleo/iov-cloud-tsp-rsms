package net.hwyz.iov.cloud.tsp.rsms.service.application.service.message;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessage;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.ProtocolMessage;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.ProtocolType;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.ProtocolHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 国标协议处理类
 *
 * @author hwyz_leo
 */
@Slf4j
@Scope("prototype")
@ChannelHandler.Sharable
@RequiredArgsConstructor
@Component("gbMessageHandler")
public class GbMessageHandler extends MessageHandler {

    private final ApplicationContext applicationContext;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtocolMessage message) throws Exception {
        GbMessage gbMessage = (GbMessage) message;
        String beanName = ProtocolType.GB.name() + "_" + gbMessage.getHeader().getCommandFlag().name() + "_" +
                gbMessage.getHeader().getAckFlag().name();
        ProtocolHandler handler = applicationContext.getBean(beanName, ProtocolHandler.class);
        handler.handle(gbMessage, clientPlatform).ifPresent(ctx::writeAndFlush);
    }
}
