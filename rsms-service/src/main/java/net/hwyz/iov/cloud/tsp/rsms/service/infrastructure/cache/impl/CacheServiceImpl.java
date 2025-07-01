package net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.cache.impl;

import cn.hutool.json.JSONUtil;
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
            ClientPlatformDo clientPlatformDo = clientPlatformCacheMap.get(clientPlatformId);
            if (logger.isDebugEnabled()) {
                logger.debug("获取客户端平台[{}:{}]缓存[{}]", clientPlatformDo.getServerPlatform().getName(),
                        clientPlatformDo.getUniqueCode(), JSONUtil.toJsonStr(clientPlatformDo));
            }
            return Optional.of(clientPlatformDo);
        }
        return Optional.empty();
    }

    @Override
    public void setClientPlatform(ClientPlatformDo clientPlatform) {
        if (logger.isDebugEnabled()) {
            logger.debug("设置客户端平台[{}:{}]缓存[{}]", clientPlatform.getServerPlatform().getName(),
                    clientPlatform.getUniqueCode(), JSONUtil.toJsonStr(clientPlatform));
        }
        clientPlatformCacheMap.put(clientPlatform.getId(), clientPlatform);
    }
}
