package net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository;

import cn.hutool.core.util.ObjUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.framework.common.domain.AbstractRepository;
import net.hwyz.iov.cloud.framework.common.domain.DoState;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.factory.ServerPlatformFactory;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.server.model.ServerPlatformDo;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.server.repository.ServerPlatformRepository;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.cache.CacheService;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.dao.ServerPlatformDao;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.po.ServerPlatformPo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 客户端平台领域仓库接口实现类
 *
 * @author hwyz_leo
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ServerPlatformRepositoryImpl extends AbstractRepository<String, ServerPlatformDo> implements ServerPlatformRepository {

    private final CacheService cacheService;
    private final ServerPlatformFactory factory;
    private final ServerPlatformDao serverPlatformDao;

    @Override
    public Optional<ServerPlatformDo> getById(String code) {
        return Optional.ofNullable(cacheService.getServerPlatform(code)
                .orElseGet(() -> {
                    ServerPlatformPo serverPlatformPo = serverPlatformDao.selectPoByCode(code);
                    if (ObjUtil.isNull(serverPlatformPo)) {
                        logger.warn("未找到服务端平台[{}]", code);
                        return null;
                    }
                    ServerPlatformDo serverPlatform = factory.build(serverPlatformPo);
                    save(serverPlatform);
                    return serverPlatform;
                }));
    }

    @Override
    public boolean save(ServerPlatformDo serverPlatform) {
        if (serverPlatform.getState() != DoState.UNCHANGED) {
            logger.info("保存客户端平台[{}]", serverPlatform.getCode());
            cacheService.setServerPlatform(serverPlatform);
        }
        return true;
    }

    @Override
    public List<ServerPlatformDo> getAll() {
        List<ServerPlatformDo> list = new ArrayList<>();
        serverPlatformDao.selectPoByExample(ServerPlatformPo.builder().build()).forEach(po -> {
            list.add(cacheService.getServerPlatform(po.getCode())
                    .orElseGet(() -> {
                        ServerPlatformDo serverPlatform = factory.build(po);
                        save(serverPlatform);
                        return serverPlatform;
                    }));
        });
        return list;
    }
}
