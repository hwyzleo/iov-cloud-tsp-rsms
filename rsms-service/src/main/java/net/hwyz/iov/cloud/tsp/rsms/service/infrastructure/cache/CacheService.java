package net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.cache;

import java.util.Map;

/**
 * 缓存服务接口
 *
 * @author hwyz_leo
 */
public interface CacheService {

    /**
     * 获取客户端平台连接状态
     *
     * @param clientPlatformUniqueKey 客户端平台唯一值
     */
    Map<String, Boolean> getClientPlatformConnectState(String clientPlatformUniqueKey);

    /**
     * 获取客户端平台登录状态
     *
     * @param clientPlatformUniqueKey 客户端平台唯一值
     */
    Map<String, Boolean> getClientPlatformLoginState(String clientPlatformUniqueKey);

}
