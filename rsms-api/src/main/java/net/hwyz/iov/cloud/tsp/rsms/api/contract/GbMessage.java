package net.hwyz.iov.cloud.tsp.rsms.api.contract;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import lombok.*;
import net.hwyz.iov.cloud.tsp.rsms.api.util.GbUtil;

/**
 * 国标消息
 *
 * @author hwyz_leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GbMessage extends ProtocolMessage {

    /**
     * 起始符
     */
    private byte[] startingSymbols;
    /**
     * 数据头字节数组
     */
    private byte[] headerBytes;
    /**
     * 数据头
     */
    private GbMessageHeader header;
    /**
     * 数据单元字节数组
     */
    private byte[] dataUnitBytes;
    /**
     * 数据单元
     */
    private GbMessageDataUnit dataUnit;
    /**
     * 校验码
     */
    private byte checkCode;



    /**
     * 解析数据头
     */
    public void parseHeader(byte[] headerBytes) {
        this.headerBytes = headerBytes;
        this.header = GbUtil.parseHeader(headerBytes);
        this.setCommandFlag(this.header.getCommandFlag().getCommandFlag());
    }

    /**
     * 计算校验码
     */
    public void calculateCheckCode() {
        this.checkCode = GbUtil.calculateCheckCode(ArrayUtil.addAll(
                ObjUtil.isNotNull(this.header) ? this.header.toByteArray() : this.headerBytes,
                ObjUtil.isNotNull(this.dataUnit) ? this.dataUnit.toByteArray() : this.dataUnitBytes
        ));
    }

    /**
     * 转换为字节数组
     *
     * @return 字节数组
     */
    @Override
    public byte[] toByteArray() {
        return ArrayUtil.addAll(this.startingSymbols,
                ObjUtil.isNotNull(this.header) ? this.header.toByteArray() : this.headerBytes,
                ObjUtil.isNotNull(this.dataUnit) ? this.dataUnit.toByteArray() : this.dataUnitBytes,
                new byte[]{this.checkCode});
    }

}
