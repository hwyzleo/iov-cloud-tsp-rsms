package net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.cache;

import net.hwyz.iov.cloud.tsp.rsms.service.domain.client.model.ClientPlatformDo;

import java.util.Optional;

/**
 * 缓存服务接口
 *
 * @author hwyz_leo
 */
public interface CacheService {

    /**
     * 获取客户端平台
     *
     * @param id 客户端平台ID
     * @return 客户端平台
     */
    Optional<ClientPlatformDo> getClientPlatform(Long id);

    /**
     * 设置客户端平台
     *
     * @param clientPlatform 客户端平台
     */
    void setClientPlatform(ClientPlatformDo clientPlatform);

}
