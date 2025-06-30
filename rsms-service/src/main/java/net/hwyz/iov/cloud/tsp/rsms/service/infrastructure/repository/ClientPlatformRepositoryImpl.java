package net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository;

import cn.hutool.core.util.ObjUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.framework.common.domain.AbstractRepository;
import net.hwyz.iov.cloud.framework.common.domain.DoState;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbDataUnitEncryptType;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.ProtocolPackager;
import net.hwyz.iov.cloud.tsp.rsms.service.domain.client.model.ClientPlatformDo;
import net.hwyz.iov.cloud.tsp.rsms.service.domain.client.repository.ClientPlatformRepository;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.cache.CacheService;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao.ClientPlatformDao;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao.ClientPlatformLoginHistoryDao;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao.ServerPlatformDao;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.ClientPlatformLoginHistoryPo;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.ServerPlatformPo;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 客户端平台领域仓库接口实现类
 *
 * @author hwyz_leo
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ClientPlatformRepositoryImpl extends AbstractRepository<Long, ClientPlatformDo> implements ClientPlatformRepository {

    private final ApplicationContext ctx;
    private final CacheService cacheService;
    private final ServerPlatformDao serverPlatformDao;
    private final ClientPlatformDao clientPlatformDao;
    private final ClientPlatformLoginHistoryDao clientPlatformLoginHistoryDao;

    @Override
    public Optional<ClientPlatformDo> getById(Long aLong) {
        return Optional.empty();
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
                list.add(
                        cacheService.getClientPlatform(po.getId())
                                .orElseGet(() -> {
                                    ServerPlatformPo serverPlatform = serverPlatformDao.selectPoByCode(po.getServerPlatformCode());
                                    if (ObjUtil.isNull(serverPlatform)) {
                                        logger.warn("未找到服务端平台[{}]", po.getServerPlatformCode());
                                        return null;
                                    }
                                    ProtocolPackager packager = ctx.getBean(serverPlatform.getProtocol() + "ProtocolPackager", ProtocolPackager.class);
                                    ClientPlatformLoginHistoryPo loginHistory = clientPlatformLoginHistoryDao.selectLastPoByClientPlatformId(po.getId());
                                    ClientPlatformDo clientPlatform = ClientPlatformDo.builder()
                                            .id(po.getId())
                                            .serverPlatform(serverPlatform)
                                            .dataUnitEncryptType(GbDataUnitEncryptType.valOf((byte) serverPlatform.getEncryptType().intValue()))
                                            .hostname(po.getHostname())
                                            .username(po.getUsername())
                                            .password(po.getPassword())
                                            .uniqueCode(po.getUniqueCode())
                                            .loginSn(ObjUtil.isNotNull(loginHistory) ? new AtomicInteger(loginHistory.getLoginSn()) : new AtomicInteger(1))
                                            .loginTime(ObjUtil.isNotNull(loginHistory) ? loginHistory.getLoginTime() : null)
                                            .failureReason(ObjUtil.isNotNull(loginHistory) ? new AtomicInteger(loginHistory.getFailureReason()) : new AtomicInteger(0))
                                            .failureCount(ObjUtil.isNotNull(loginHistory) ? new AtomicInteger(loginHistory.getFailureCount()) : new AtomicInteger(0))
                                            .packager(packager)
                                            .build();
                                    clientPlatform.init();
                                    save(clientPlatform);
                                    return clientPlatform;
                                })
                )
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
