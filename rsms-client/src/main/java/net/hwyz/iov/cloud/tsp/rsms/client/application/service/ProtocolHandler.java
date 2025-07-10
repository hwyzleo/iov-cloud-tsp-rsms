package net.hwyz.iov.cloud.tsp.rsms.client.application.service;

import net.hwyz.iov.cloud.tsp.rsms.api.contract.ProtocolMessage;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.model.ClientPlatformDo;

import java.util.Optional;

/**
 * 协议数据处理器
 *
 * @author hwyz_leo
 */
public interface ProtocolHandler {

    /**
     * 处理协议消息
     *
     * @param message        协议消息
     * @param clientPlatform 客户端平台
     * @return 协议消息
     */
    Optional<ProtocolMessage> handle(ProtocolMessage message, ClientPlatformDo clientPlatform);

}
