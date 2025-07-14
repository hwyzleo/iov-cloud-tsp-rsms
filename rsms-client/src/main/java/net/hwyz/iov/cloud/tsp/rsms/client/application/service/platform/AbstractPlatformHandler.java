package net.hwyz.iov.cloud.tsp.rsms.client.application.service.platform;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.client.application.service.ClientPlatformLoginHistoryAppService;
import net.hwyz.iov.cloud.tsp.rsms.client.application.service.PlatformHandler;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.model.ClientPlatformDo;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.repository.ClientPlatformRepository;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.server.model.ServerPlatformDo;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.cache.CacheService;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.ServerPlatformRepositoryImpl;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.dao.RegisteredVehicleDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * 平台业务处理抽象类
 */
@Slf4j
public abstract class AbstractPlatformHandler implements PlatformHandler {

    @Autowired
    private CacheService cacheService;
    @Autowired
    private RegisteredVehicleDao registeredVehicleDao;
    @Autowired
    private ClientPlatformRepository clientPlatformRepository;
    @Autowired
    private ServerPlatformRepositoryImpl serverPlatformRepository;
    @Autowired
    private ClientPlatformLoginHistoryAppService clientPlatformLoginHistoryAppService;

    @Override
    public void connectSuccess(ClientPlatformDo clientPlatform) {
        clientPlatform.connectSuccess();
        clientPlatformRepository.save(clientPlatform);
        cacheService.setClientPlatformConnectState(clientPlatform);
    }

    @Override
    public void connectFailure(ClientPlatformDo clientPlatform) {
        clientPlatform.connectFailure();
        clientPlatformRepository.save(clientPlatform);
        cacheService.setClientPlatformConnectState(clientPlatform);
    }

    @Override
    public void login(ClientPlatformDo clientPlatform) {
        clientPlatform.login();
        clientPlatformRepository.save(clientPlatform);
    }

    @Override
    public void loginSuccess(ClientPlatformDo clientPlatform) {
        clientPlatform.loginSuccess();
        clientPlatformRepository.save(clientPlatform);
        clientPlatformLoginHistoryAppService.recordLogin(clientPlatform);
        cacheService.setClientPlatformLoginState(clientPlatform);
    }

    @Override
    public void loginFailure(ClientPlatformDo clientPlatform) {
        clientPlatform.loginFailure();
        clientPlatformRepository.save(clientPlatform);
        clientPlatformLoginHistoryAppService.recordLogin(clientPlatform);
        cacheService.setClientPlatformLoginState(clientPlatform);
    }

    @Override
    public void logout(ClientPlatformDo clientPlatform) {
        clientPlatform.logout();
        clientPlatformRepository.save(clientPlatform);
    }

    @Override
    public void logoutSuccess(ClientPlatformDo clientPlatform) {
        clientPlatform.logoutSuccess();
        clientPlatformRepository.save(clientPlatform);
        clientPlatformLoginHistoryAppService.recordLogout(clientPlatform);
        cacheService.setClientPlatformLoginState(clientPlatform);
    }

    @Override
    public void logoutFailure(ClientPlatformDo clientPlatform) {
        logger.warn("出现未预料的登出失败场景[{}]", JSONUtil.toJsonStr(clientPlatform));
    }

    @Override
    public void syncPlatform(ClientPlatformDo clientPlatform) {
        cacheService.resetServerPlatform(clientPlatform.getServerPlatform().getCode());
        cacheService.resetClientPlatform(clientPlatform.getId());
    }

    @Override
    public void syncVehicle(ClientPlatformDo clientPlatform) {
        ServerPlatformDo serverPlatform = clientPlatform.getServerPlatform();
        Set<String> vehicleSet = registeredVehicleDao.selectVinByServerPlatformCode(serverPlatform.getCode());
        serverPlatform.syncVehicleSet(vehicleSet);
        serverPlatformRepository.save(serverPlatform);
    }

}
