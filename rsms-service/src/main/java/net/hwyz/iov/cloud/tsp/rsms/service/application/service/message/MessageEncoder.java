package net.hwyz.iov.cloud.tsp.rsms.service.application.service.message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.ProtocolMessage;

/**
 * 消息编码器
 *
 * @author hwyz_leo
 */
@Slf4j
public abstract class MessageEncoder extends MessageToByteEncoder<ProtocolMessage> {

    abstract protected void encode(ChannelHandlerContext ctx, ProtocolMessage message, ByteBuf out) throws Exception;

}
