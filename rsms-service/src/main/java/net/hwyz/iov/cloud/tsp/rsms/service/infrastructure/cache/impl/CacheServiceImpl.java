package net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.cache.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.service.domain.client.model.ClientPlatformDo;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.cache.CacheService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存服务实现类
 *
 * @author hwyz_leo
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

    /**
     * 客户端平台缓存Map
     */
    private static final ConcurrentHashMap<Long, ClientPlatformDo> clientPlatformCacheMap = new ConcurrentHashMap<>(16);

    @Override
    public Optional<ClientPlatformDo> getClientPlatform(Long clientPlatformId) {
        logger.debug("获取客户端平台[{}]缓存", clientPlatformId);
        if (clientPlatformCacheMap.contains(clientPlatformId)) {
            ClientPlatformDo clientPlatform = clientPlatformCacheMap.get(clientPlatformId);
            if (logger.isDebugEnabled()) {
                logger.debug("获取客户端平台[{}:{}]登录[{}]", clientPlatform.getServerPlatform().getName(),
                        clientPlatform.getUniqueCode(), clientPlatform.isLogin());
            }
            return Optional.of(clientPlatform);
        }
        return Optional.empty();
    }

    @Override
    public void setClientPlatform(ClientPlatformDo clientPlatform) {
        if (logger.isDebugEnabled()) {
            logger.debug("设置客户端平台[{}:{}]登录[{}]", clientPlatform.getServerPlatform().getName(),
                    clientPlatform.getUniqueCode(), clientPlatform.isLogin());
        }
        clientPlatformCacheMap.put(clientPlatform.getId(), clientPlatform);
    }
}
