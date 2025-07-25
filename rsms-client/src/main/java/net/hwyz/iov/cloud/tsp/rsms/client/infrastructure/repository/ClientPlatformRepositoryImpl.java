package net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository;

import cn.hutool.core.util.ObjUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.framework.common.domain.AbstractRepository;
import net.hwyz.iov.cloud.framework.common.domain.DoState;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.model.ClientPlatformAccountDo;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.model.ClientPlatformDo;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.repository.ClientPlatformRepository;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.factory.ClientPlatformAccountFactory;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.factory.ClientPlatformFactory;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.server.model.ServerPlatformDo;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.server.repository.ServerPlatformRepository;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.cache.CacheService;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.dao.ClientPlatformAccountDao;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.dao.ClientPlatformDao;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.dao.RegisteredVehicleDao;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.po.ClientPlatformAccountPo;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.po.ClientPlatformPo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    private final ClientPlatformDao clientPlatformDao;
    private final RegisteredVehicleDao registeredVehicleDao;
    private final ClientPlatformFactory clientPlatformFactory;
    private final ServerPlatformRepository serverPlatformRepository;
    private final ClientPlatformAccountDao clientPlatformAccountDao;
    private final ClientPlatformAccountFactory clientPlatformAccountFactory;

    @Override
    public Optional<ClientPlatformDo> getById(Long id) {
        return Optional.ofNullable(cacheService.getClientPlatform(id)
                .orElseGet(() -> {
                    ClientPlatformPo clientPlatform = clientPlatformDao.selectPoById(id);
                    if (ObjUtil.isNull(clientPlatform)) {
                        logger.warn("未找到客户端平台[{}]", id);
                        return null;
                    }
                    Optional<ServerPlatformDo> serverPlatformOptional = serverPlatformRepository.getById(clientPlatform.getServerPlatformCode());
                    if (serverPlatformOptional.isEmpty()) {
                        logger.warn("未找到服务端平台[{}]", clientPlatform.getServerPlatformCode());
                        return null;
                    }
                    List<ClientPlatformAccountPo> accountPoList = clientPlatformAccountDao.selectPoByEnabled(id);
                    if (accountPoList.isEmpty()) {
                        logger.warn("客户端平台[{}]下无可用账号", id);
                        return null;
                    }
                    List<ClientPlatformAccountDo> accounts = new ArrayList<>();
                    accountPoList.forEach(accountPo -> accounts.add(clientPlatformAccountFactory.build(accountPo)));
                    Set<String> vehicleSet = registeredVehicleDao.selectReportVinByClientPlatformId(id);
                    ClientPlatformDo clientPlatformDo = clientPlatformFactory.build(clientPlatform, serverPlatformOptional.get(),
                            accounts, vehicleSet);
                    save(clientPlatformDo);
                    return clientPlatformDo;
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
        clientPlatformDao.selectPoByEnabled().forEach(po -> {
            ClientPlatformDo clientPlatformDo = cacheService.getClientPlatform(po.getId()).orElseGet(() -> {
                Optional<ServerPlatformDo> serverPlatformDoOptional = serverPlatformRepository.getById(po.getServerPlatformCode());
                if (serverPlatformDoOptional.isEmpty()) {
                    logger.warn("未找到服务端平台[{}]", po.getServerPlatformCode());
                    return null;
                }
                List<ClientPlatformAccountPo> accountPoList = clientPlatformAccountDao.selectPoByEnabled(po.getId());
                if (accountPoList.isEmpty()) {
                    logger.warn("客户端平台[{}]下无可用账号", po.getId());
                    return null;
                }
                List<ClientPlatformAccountDo> accounts = new ArrayList<>();
                accountPoList.forEach(accountPo -> accounts.add(clientPlatformAccountFactory.build(accountPo)));
                Set<String> vehicleSet = registeredVehicleDao.selectReportVinByClientPlatformId(po.getId());
                ClientPlatformDo clientPlatformDoTmp = clientPlatformFactory.build(po, serverPlatformDoOptional.get(), accounts, vehicleSet);
                save(clientPlatformDoTmp);
                return clientPlatformDoTmp;
            });
            if (ObjUtil.isNotNull(clientPlatformDo)) {
                list.add(clientPlatformDo);
            }
        });
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
