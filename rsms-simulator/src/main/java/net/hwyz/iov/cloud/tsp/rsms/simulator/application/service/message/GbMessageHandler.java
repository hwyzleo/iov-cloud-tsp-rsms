package net.hwyz.iov.cloud.tsp.rsms.simulator.application.service.message;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessage;
import net.hwyz.iov.cloud.tsp.rsms.simulator.application.service.GbProtocolHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 国标协议处理类
 *
 * @author hwyz_leo
 */
@Slf4j
@ChannelHandler.Sharable
@RequiredArgsConstructor
@Component("gbMessageHandler")
public class GbMessageHandler extends SimpleChannelInboundHandler<GbMessage> {

    private final ApplicationContext applicationContext;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GbMessage message) throws Exception {
        String beanName = message.getHeader().getCommandFlag().name() + "_" + message.getHeader().getAckFlag().name();
        GbProtocolHandler handler = applicationContext.getBean(beanName, GbProtocolHandler.class);
        handler.handle(message).ifPresent(ctx::writeAndFlush);
    }
}
