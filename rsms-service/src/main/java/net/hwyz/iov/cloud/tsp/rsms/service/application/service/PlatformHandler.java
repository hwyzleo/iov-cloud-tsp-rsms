package net.hwyz.iov.cloud.tsp.rsms.service.application.service;

import net.hwyz.iov.cloud.tsp.rsms.service.domain.client.model.ClientPlatformDo;

/**
 * 平台业务处理器
 *
 * @author hwyz_leo
 */
public interface PlatformHandler {

    /**
     * 登录
     * @param clientPlatform
     */
    void login(ClientPlatformDo clientPlatform);

    /**
     * 登录成功
     *
     * @param clientPlatform 客户端平台
     */
    void loginSuccess(ClientPlatformDo clientPlatform);

    /**
     * 登录失败
     *
     * @param clientPlatform 客户端平台
     */
    void loginFailure(ClientPlatformDo clientPlatform);

}
