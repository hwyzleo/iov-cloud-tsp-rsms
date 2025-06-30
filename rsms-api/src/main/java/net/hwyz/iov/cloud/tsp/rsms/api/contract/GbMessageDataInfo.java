package net.hwyz.iov.cloud.tsp.rsms.api.contract;

import lombok.Data;

/**
 * 国标数据信息
 *
 * @author hwyz_leo
 */
@Data
public abstract class GbMessageDataInfo {

    /**
     * 获取数据信息长度
     *
     * @return 数据信息长度
     */
    abstract public int getLength();

    /**
     * 解析数据信息
     *
     * @param dataInfoBytes 数据信息字节数组
     */
    abstract public void parse(byte[] dataInfoBytes);

    /**
     * 转为字节数组
     *
     * @return 字节数组
     */
    abstract public byte[] toByteArray();

}
