package net.hwyz.iov.cloud.tsp.rsms.client.domain.client.repository;

import net.hwyz.iov.cloud.framework.common.domain.BaseRepository;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.model.ClientPlatformDo;

import java.util.List;

/**
 * 客户端平台领域仓库接口
 *
 * @author hwyz_leo
 */
public interface ClientPlatformRepository extends BaseRepository<Long, ClientPlatformDo> {

    /**
     * 获取所有已启用的客户端平台
     *
     * @return 启用的客户端平台列表
     */
    List<ClientPlatformDo> getAllEnabled();

    /**
     * 获取所有已启动的客户端平台
     *
     * @return 已启动的客户端平台列表
     */
    List<ClientPlatformDo> getAllStarted();

}
