package net.hwyz.iov.cloud.tsp.rsms.client.domain.server.repository;

import net.hwyz.iov.cloud.framework.common.domain.BaseRepository;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.server.model.ServerPlatformDo;

import java.util.List;

/**
 * 服务端平台领域仓库接口
 *
 * @author hwyz_leo
 */
public interface ServerPlatformRepository extends BaseRepository<String, ServerPlatformDo> {

    /**
     * 获取所有服务端平台
     *
     * @return 服务端平台列表
     */
    List<ServerPlatformDo> getAll();

}
