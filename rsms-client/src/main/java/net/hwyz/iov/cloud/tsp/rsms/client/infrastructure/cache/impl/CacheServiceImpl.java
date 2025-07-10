package net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.cache.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.model.ClientPlatformDo;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.server.model.ServerPlatformDo;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.cache.CacheService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
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

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 服务端平台缓存Map
     */
    private static final ConcurrentHashMap<String, ServerPlatformDo> serverPlatformCacheMap = new ConcurrentHashMap<>(16);
    /**
     * 客户端平台缓存Map
     */
    private static final ConcurrentHashMap<Long, ClientPlatformDo> clientPlatformCacheMap = new ConcurrentHashMap<>(16);
    /**
     * Redis Key：客户端平台状态
     */
    private static final String REDIS_KEY_CLIENT_PLATFORM_STATE = "rsms:clientPlatformState";
    /**
     * Redis Key：客户端平台状态流水号
     */
    private static final String REDIS_KEY_CLIENT_PLATFORM_STATE_SN = "rsms:clientPlatformStateSn";
    /**
     * Redis Key前缀：客户端平台连接状态
     */
    private static final String REDIS_KEY_PREFIX_CLIENT_PLATFORM_CONNECT_STATE = "rsms:clientPlatformConnectState:";
    /**
     * Redis Key前缀：客户端平台登录状态
     */
    private static final String REDIS_KEY_PREFIX_CLIENT_PLATFORM_LOGIN_STATE = "rsms:clientPlatformLoginState:";

    /**
     * Jenkins发布流水号
     */
    @Value("${BUILD_NUMBER:unknown}")
    private String buildNumber;

    @Override
    public Optional<ServerPlatformDo> getServerPlatform(String serverPlatformCode) {
        logger.debug("获取服务端平台[{}]缓存", serverPlatformCode);
        if (serverPlatformCacheMap.containsKey(serverPlatformCode)) {
            ServerPlatformDo serverPlatform = serverPlatformCacheMap.get(serverPlatformCode);
            return Optional.of(serverPlatform);
        }
        return Optional.empty();
    }

    @Override
    public void setServerPlatform(ServerPlatformDo serverPlatform) {
        logger.debug("设置客户端平台[{}]缓存", serverPlatform.getCode());
        serverPlatformCacheMap.put(serverPlatform.getCode(), serverPlatform);
    }

    @Override
    public Optional<ClientPlatformDo> getClientPlatform(Long clientPlatformId) {
        logger.debug("获取客户端平台[{}]缓存", clientPlatformId);
        if (clientPlatformCacheMap.containsKey(clientPlatformId)) {
            ClientPlatformDo clientPlatform = clientPlatformCacheMap.get(clientPlatformId);
            return Optional.of(clientPlatform);
        }
        return Optional.empty();
    }

    @Override
    public void setClientPlatform(ClientPlatformDo clientPlatform) {
        logger.debug("设置客户端平台[{}]缓存", clientPlatform.getId());
        clientPlatformCacheMap.put(clientPlatform.getId(), clientPlatform);
    }

    @Override
    public void resetClientPlatformState() {
        String sn = redisTemplate.opsForValue().get(REDIS_KEY_CLIENT_PLATFORM_STATE_SN);
        if (!buildNumber.equalsIgnoreCase(sn)) {
            logger.debug("重置所有客户端平台相关状态[{}->{}]", sn, buildNumber);
            redisTemplate.opsForHash().entries(REDIS_KEY_CLIENT_PLATFORM_STATE).forEach((key, value) -> {
                redisTemplate.delete(REDIS_KEY_PREFIX_CLIENT_PLATFORM_CONNECT_STATE + key);
                redisTemplate.delete(REDIS_KEY_PREFIX_CLIENT_PLATFORM_LOGIN_STATE + key);
            });
            redisTemplate.delete(REDIS_KEY_CLIENT_PLATFORM_STATE);
            redisTemplate.opsForValue().set(REDIS_KEY_CLIENT_PLATFORM_STATE_SN, buildNumber);
        }
    }

    @Override
    public void setClientPlatformConnectState(ClientPlatformDo clientPlatform) {
        logger.debug("设置客户端平台[{}]连接状态", clientPlatform.getUniqueKey());
        if (!redisTemplate.opsForHash().hasKey(REDIS_KEY_CLIENT_PLATFORM_STATE, clientPlatform.getUniqueKey())) {
            redisTemplate.opsForHash().put(REDIS_KEY_CLIENT_PLATFORM_STATE, clientPlatform.getUniqueKey(), System.currentTimeMillis());
        }
        redisTemplate.opsForHash().put(REDIS_KEY_PREFIX_CLIENT_PLATFORM_CONNECT_STATE + clientPlatform.getUniqueKey(),
                clientPlatform.getCurrentHostname(), clientPlatform.isConnect());
    }

    @Override
    public void setClientPlatformLoginState(ClientPlatformDo clientPlatform) {
        logger.debug("设置客户端平台[{}]登录状态", clientPlatform.getUniqueKey());
        if (!redisTemplate.opsForHash().hasKey(REDIS_KEY_CLIENT_PLATFORM_STATE, clientPlatform.getUniqueKey())) {
            redisTemplate.opsForHash().put(REDIS_KEY_CLIENT_PLATFORM_STATE, clientPlatform.getUniqueKey(), System.currentTimeMillis());
        }
        redisTemplate.opsForHash().put(REDIS_KEY_PREFIX_CLIENT_PLATFORM_LOGIN_STATE + clientPlatform.getUniqueKey(),
                clientPlatform.getCurrentHostname(), clientPlatform.isLogin());
    }
}
