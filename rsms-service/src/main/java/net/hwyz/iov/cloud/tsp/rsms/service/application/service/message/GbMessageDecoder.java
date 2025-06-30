package net.hwyz.iov.cloud.tsp.rsms.service.application.service.message;

import cn.hutool.core.util.ObjUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessage;

import java.util.List;

import static net.hwyz.iov.cloud.tsp.rsms.api.contract.constant.GbConstants.GB_DATA_HEADER_LENGTH;
import static net.hwyz.iov.cloud.tsp.rsms.api.contract.constant.GbConstants.GB_DATA_STARTING_SYMBOLS;

/**
 * 国标消息解码器
 *
 * @author hwyz_leo
 */
@Slf4j
public class GbMessageDecoder extends MessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 识别起始符
        if (!findStartingSymbol(in)) return;
        // 解析报文头
        GbMessage gbData = new GbMessage();
        gbData.setStartingSymbols(GB_DATA_STARTING_SYMBOLS);
        byte[] headerBytes = new byte[GB_DATA_HEADER_LENGTH];
        in.readBytes(headerBytes);
        gbData.parseHeader(headerBytes);
        if (ObjUtil.isNull(gbData.getHeader())) return;
        // 解析数据单元
        int dataUnitLength = gbData.getHeader().getDataUnitLength();
        byte[] dataUnitBytes = new byte[dataUnitLength];
        in.readBytes(dataUnitBytes);
        gbData.setDataUnitBytes(dataUnitBytes);
        // 验证校验码
        byte checkCode = in.readByte();
        gbData.calculateCheckCode();
        if (checkCode != gbData.getCheckCode()) {
            logger.warn("校验失败，跳过当前数据包");
            return;
        }
        out.add(gbData);
    }

    /**
     * 从流中查找起始符号
     */
    private boolean findStartingSymbol(ByteBuf in) {
        byte[] buffer = new byte[2];
        int bufferPos = 0;
        while (true) {
            try {
                in.readBytes(buffer, bufferPos, 1);
            } catch (Exception e) {
                logger.warn("读取流异常", e);
                return false;
            }
            boolean match = true;
            for (int i = 0; i <= bufferPos; i++) {
                if (buffer[i] != GB_DATA_STARTING_SYMBOLS[i]) {
                    match = false;
                    bufferPos = 0;
                    break;
                }
            }
            if (match) {
                bufferPos++;
            }
            if (bufferPos == GB_DATA_STARTING_SYMBOLS.length) {
                return true;
            }
        }
    }
}
