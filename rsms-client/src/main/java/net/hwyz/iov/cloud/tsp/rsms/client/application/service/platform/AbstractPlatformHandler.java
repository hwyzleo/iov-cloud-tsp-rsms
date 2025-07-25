package net.hwyz.iov.cloud.tsp.rsms.client.application.service.platform;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.ClientPlatformState;
import net.hwyz.iov.cloud.tsp.rsms.client.application.event.publish.ClientPlatformStatePublish;
import net.hwyz.iov.cloud.tsp.rsms.client.application.service.PlatformHandler;
import net.hwyz.iov.cloud.tsp.rsms.client.application.service.RegisteredVehicleAppService;
import net.hwyz.iov.cloud.tsp.rsms.client.application.service.ReissueTimePeriodAppService;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.model.ClientPlatformDo;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.repository.ClientPlatformRepository;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.cache.CacheService;
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
    private ClientPlatformRepository clientPlatformRepository;
    @Autowired
    private ClientPlatformStatePublish clientPlatformStatePublish;
    @Autowired
    private RegisteredVehicleAppService registeredVehicleAppService;
    @Autowired
    private ReissueTimePeriodAppService reissueTimePeriodAppService;

    @Override
    public void connectSuccess(ClientPlatformDo clientPlatform) {
        clientPlatform.connectSuccess();
        clientPlatformRepository.save(clientPlatform);
        cacheService.setClientPlatformConnectState(clientPlatform);
        clientPlatformStatePublish.sendPlatformState(clientPlatform, ClientPlatformState.CONNECT_SUCCESS);
    }

    @Override
    public void connectFailure(ClientPlatformDo clientPlatform) {
        clientPlatform.connectFailure();
        clientPlatformRepository.save(clientPlatform);
        cacheService.setClientPlatformConnectState(clientPlatform);
        clientPlatformStatePublish.sendPlatformState(clientPlatform, ClientPlatformState.CONNECT_FAILURE);
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
        cacheService.setClientPlatformLoginState(clientPlatform);
        clientPlatformStatePublish.sendPlatformState(clientPlatform, ClientPlatformState.LOGIN_SUCCESS);
    }

    @Override
    public void loginFailure(ClientPlatformDo clientPlatform) {
        clientPlatform.loginFailure();
        clientPlatformRepository.save(clientPlatform);
        cacheService.setClientPlatformLoginState(clientPlatform);
        clientPlatformStatePublish.sendPlatformState(clientPlatform, ClientPlatformState.LOGIN_FAILURE);
    }

    @Override
    public void logout(ClientPlatformDo clientPlatform) {
        clientPlatform.logout();
        clientPlatformRepository.save(clientPlatform);
        reissueTimePeriodAppService.recordTimePeriodStart(clientPlatform, true);
    }

    @Override
    public void logoutSuccess(ClientPlatformDo clientPlatform) {
        clientPlatform.logoutSuccess();
        clientPlatformRepository.save(clientPlatform);
        cacheService.setClientPlatformLoginState(clientPlatform);
        clientPlatformStatePublish.sendPlatformState(clientPlatform, ClientPlatformState.LOGOUT_SUCCESS);
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
        Set<String> vehicleSet = registeredVehicleAppService.getClientPlatformReportVin(clientPlatform.getId());
        clientPlatform.syncVehicleSet(vehicleSet);
        clientPlatformRepository.save(clientPlatform);
    }

}
