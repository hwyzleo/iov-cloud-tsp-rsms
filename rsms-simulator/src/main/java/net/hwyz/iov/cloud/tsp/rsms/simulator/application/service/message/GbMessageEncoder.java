package net.hwyz.iov.cloud.tsp.rsms.simulator.application.service.message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessage;

import java.util.Arrays;

/**
 * 国标消息编码器
 *
 * @author hwyz_leo
 */
@Slf4j
public class GbMessageEncoder extends MessageToByteEncoder<GbMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, GbMessage message, ByteBuf out) throws Exception {
        byte[] bytes = message.toByteArray();
        logger.debug("回复[{}]消息{}", message.getHeader().getUniqueCode(), Arrays.toString(bytes));
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}
