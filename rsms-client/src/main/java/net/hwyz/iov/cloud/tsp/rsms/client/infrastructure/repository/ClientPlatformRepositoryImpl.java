package net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository;

import cn.hutool.core.util.ObjUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.framework.common.domain.AbstractRepository;
import net.hwyz.iov.cloud.framework.common.domain.DoState;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.model.ClientPlatformDo;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.repository.ClientPlatformRepository;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.factory.ClientPlatformFactory;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.server.model.ServerPlatformDo;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.server.repository.ServerPlatformRepository;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.cache.CacheService;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.dao.ClientPlatformDao;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.dao.ClientPlatformLoginHistoryDao;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.po.ClientPlatformLoginHistoryPo;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.po.ClientPlatformPo;
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
public class ClientPlatformRepositoryImpl extends AbstractRepository<Long, ClientPlatformDo> implements ClientPlatformRepository {

    private final CacheService cacheService;
    private final ClientPlatformFactory factory;
    private final ClientPlatformDao clientPlatformDao;
    private final ServerPlatformRepository serverPlatformRepository;
    private final ClientPlatformLoginHistoryDao clientPlatformLoginHistoryDao;

    @Override
    public Optional<ClientPlatformDo> getById(Long id) {
        return Optional.ofNullable(cacheService.getClientPlatform(id)
                .orElseGet(() -> {
                    ClientPlatformPo clientPlatformPo = clientPlatformDao.selectPoById(id);
                    if (ObjUtil.isNull(clientPlatformPo)) {
                        logger.warn("未找到客户端平台[{}]", id);
                        return null;
                    }
                    Optional<ServerPlatformDo> serverPlatformDoOptional = serverPlatformRepository.getById(clientPlatformPo.getServerPlatformCode());
                    if (serverPlatformDoOptional.isEmpty()) {
                        logger.warn("未找到服务端平台[{}]", clientPlatformPo.getServerPlatformCode());
                        return null;
                    }
                    ClientPlatformLoginHistoryPo loginHistory = clientPlatformLoginHistoryDao.selectLastPoByClientPlatformId(id);
                    ClientPlatformDo clientPlatform = factory.build(clientPlatformPo, serverPlatformDoOptional.get(), loginHistory);
                    save(clientPlatform);
                    return clientPlatform;
                }));
    }

    @Override
    public boolean save(ClientPlatformDo clientPlatform) {
        if (clientPlatform.getState() != DoState.UNCHANGED) {
            logger.info("保存客户端平台[{}:{}]", clientPlatform.getServerPlatform().getName(), clientPlatform.getUniqueCode());
            cacheService.setClientPlatform(clientPlatform);
        }
        return true;
    }

    @Override
    public List<ClientPlatformDo> getAllEnabled() {
        List<ClientPlatformDo> list = new ArrayList<>();
        clientPlatformDao.selectPoByEnabled().forEach(po ->
                list.add(cacheService.getClientPlatform(po.getId()).orElseGet(() -> {
                    Optional<ServerPlatformDo> serverPlatformDoOptional = serverPlatformRepository.getById(po.getServerPlatformCode());
                    if (serverPlatformDoOptional.isEmpty()) {
                        logger.warn("未找到服务端平台[{}]", po.getServerPlatformCode());
                        return null;
                    }
                    ClientPlatformLoginHistoryPo loginHistory = clientPlatformLoginHistoryDao.selectLastPoByClientPlatformId(po.getId());
                    ClientPlatformDo clientPlatform = factory.build(po, serverPlatformDoOptional.get(), loginHistory);
                    save(clientPlatform);
                    return clientPlatform;
                }))
        );
        return list;
    }

    @Override
    public List<ClientPlatformDo> getAllStarted() {
        List<ClientPlatformDo> list = new ArrayList<>();
        getAllEnabled().forEach(clientPlatform -> {
            if (clientPlatform.isLogin()) {
                list.add(clientPlatform);
            }
        });
        return list;
    }
}
