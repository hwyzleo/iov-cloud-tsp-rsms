package net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.cache.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.cache.CacheService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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
     * Redis Key前缀：客户端平台连接状态
     */
    private static final String REDIS_KEY_PREFIX_CLIENT_PLATFORM_CONNECT_STATE = "rsms:clientPlatformConnectState:";
    /**
     * Redis Key前缀：客户端平台登录状态
     */
    private static final String REDIS_KEY_PREFIX_CLIENT_PLATFORM_LOGIN_STATE = "rsms:clientPlatformLoginState:";

    @Override
    public Map<String, Boolean> getClientPlatformConnectState(String clientPlatformUniqueKey) {
        logger.debug("获取客户端平台[{}]连接状态", clientPlatformUniqueKey);
        Map<String, Boolean> map = new HashMap<>();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(REDIS_KEY_PREFIX_CLIENT_PLATFORM_CONNECT_STATE + clientPlatformUniqueKey))) {
            redisTemplate.opsForHash().entries(REDIS_KEY_PREFIX_CLIENT_PLATFORM_CONNECT_STATE + clientPlatformUniqueKey)
                    .forEach((key, value) -> map.put(key.toString(), Boolean.valueOf(value.toString())));
        }
        return map;
    }

    @Override
    public Map<String, Boolean> getClientPlatformLoginState(String clientPlatformUniqueKey) {
        logger.debug("获取客户端平台[{}]登录状态", clientPlatformUniqueKey);
        Map<String, Boolean> map = new HashMap<>();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(REDIS_KEY_PREFIX_CLIENT_PLATFORM_LOGIN_STATE + clientPlatformUniqueKey))) {
            redisTemplate.opsForHash().entries(REDIS_KEY_PREFIX_CLIENT_PLATFORM_LOGIN_STATE + clientPlatformUniqueKey)
                    .forEach((key, value) -> map.put(key.toString(), Boolean.valueOf(value.toString())));
        }
        return map;
    }
}
