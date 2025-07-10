package net.hwyz.iov.cloud.tsp.rsms.client.application.service.message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessage;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.ProtocolMessage;

import java.util.Arrays;

/**
 * 国标消息编码器
 *
 * @author hwyz_leo
 */
@Slf4j
public class GbMessageEncoder extends MessageEncoder {
    @Override
    protected void encode(ChannelHandlerContext ctx, ProtocolMessage message, ByteBuf out) throws Exception {
        GbMessage gbMessage = (GbMessage) message;
        byte[] bytes = gbMessage.toByteArray();
        logger.debug("发送消息{}", Arrays.toString(bytes));
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}
