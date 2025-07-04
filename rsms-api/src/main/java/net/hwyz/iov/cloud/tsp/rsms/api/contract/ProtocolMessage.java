package net.hwyz.iov.cloud.tsp.rsms.api.contract;

import lombok.Data;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.CommandFlag;

/**
 * 协议消息
 *
 * @author hwyz_leo
 */
@Data
public abstract class ProtocolMessage {

    /**
     * 命令标识
     */
    private CommandFlag commandFlag;
    /**
     * 车架号
     */
    private String vin;

    /**
     * 转换为字节数组
     *
     * @return 字节数组
     */
    public abstract byte[] toByteArray();

}
