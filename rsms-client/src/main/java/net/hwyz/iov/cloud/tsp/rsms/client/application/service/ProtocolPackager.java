package net.hwyz.iov.cloud.tsp.rsms.client.application.service;

import net.hwyz.iov.cloud.tsp.rsms.api.contract.ProtocolMessage;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.model.ClientPlatformDo;

/**
 * 协议数据包装器
 *
 * @author hwyz_leo
 */
public interface ProtocolPackager {

    /**
     * 平台登录
     *
     * @param clientPlatform 客户端平台
     * @return 协议消息
     */
    ProtocolMessage platformLogin(ClientPlatformDo clientPlatform);

    /**
     * 平台登出
     *
     * @param clientPlatform 客户端平台
     * @return 协议消息
     */
    ProtocolMessage platformLogout(ClientPlatformDo clientPlatform);

}
