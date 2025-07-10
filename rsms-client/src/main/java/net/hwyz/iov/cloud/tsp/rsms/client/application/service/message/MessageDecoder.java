package net.hwyz.iov.cloud.tsp.rsms.client.application.service.message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 消息解码器
 *
 * @author hwyz_leo
 */
@Slf4j
public abstract class MessageDecoder extends ByteToMessageDecoder {
    abstract protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception;

}
