package net.hwyz.iov.cloud.tsp.rsms.service.application.service.protocol;

import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessage;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.ProtocolMessage;

/**
 * 国标消息处理基础类
 *
 * @author hwyz_leo
 */
public class BaseGbMessageHandler {

    /**
     * 协议消息转换为国标消息
     *
     * @param protocolMessage 协议消息
     * @return 国标消息
     */
    protected GbMessage convert(ProtocolMessage protocolMessage) {
        return (GbMessage) protocolMessage;
    }

}
