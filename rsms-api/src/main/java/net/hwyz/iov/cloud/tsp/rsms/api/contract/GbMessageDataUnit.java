package net.hwyz.iov.cloud.tsp.rsms.api.contract;

import lombok.Data;

/**
 * 国标数据单元
 *
 * @author hwyz_leo
 */
@Data
public abstract class GbMessageDataUnit {

    /**
     * 消息时间
     */
    protected byte[] messageTime;

    /**
     * 解析数据单元
     *
     * @param dataUnitBytes 数据单元字节数组
     */
    abstract public void parse(byte[] dataUnitBytes);

    /**
     * 转为字节数组
     *
     * @return 字节数组
     */
    abstract public byte[] toByteArray();

}
