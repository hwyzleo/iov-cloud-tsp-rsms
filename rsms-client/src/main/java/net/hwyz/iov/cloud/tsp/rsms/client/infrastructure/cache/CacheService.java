package net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.cache;

import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.model.ClientPlatformDo;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.server.model.ServerPlatformDo;

import java.util.Optional;

/**
 * 缓存服务接口
 *
 * @author hwyz_leo
 */
public interface CacheService {

    /**
     * 获取服务端平台
     *
     * @param serverPlatformCode 服务端平台代码
     * @return 服务端平台
     */
    Optional<ServerPlatformDo> getServerPlatform(String serverPlatformCode);

    /**
     * 设置服务端平台
     *
     * @param serverPlatform 服务端平台
     */
    void setServerPlatform(ServerPlatformDo serverPlatform);

    /**
     * 重置服务端平台
     *
     * @param serverPlatformCode 服务端平台代码
     */
    void resetServerPlatform(String serverPlatformCode);

    /**
     * 获取客户端平台
     *
     * @param clientPlatformId 客户端平台ID
     * @return 客户端平台
     */
    Optional<ClientPlatformDo> getClientPlatform(Long clientPlatformId);

    /**
     * 设置客户端平台
     *
     * @param clientPlatform 客户端平台
     */
    void setClientPlatform(ClientPlatformDo clientPlatform);

    /**
     * 重置客户端平台
     *
     * @param clientPlatformId 客户端平台ID
     */
    void resetClientPlatform(Long clientPlatformId);

    /**
     * 重置客户端平台相关状态
     */
    void resetClientPlatformState();

    /**
     * 设置客户端平台连接状态
     *
     * @param clientPlatform 客户端平台
     */
    void setClientPlatformConnectState(ClientPlatformDo clientPlatform);

    /**
     * 设置客户端平台登录状态
     *
     * @param clientPlatform 客户端平台
     */
    void setClientPlatformLoginState(ClientPlatformDo clientPlatform);

}
