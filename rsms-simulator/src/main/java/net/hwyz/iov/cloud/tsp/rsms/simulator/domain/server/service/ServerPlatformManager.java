package net.hwyz.iov.cloud.tsp.rsms.simulator.domain.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.simulator.domain.contract.enums.ServerPlatformType;
import net.hwyz.iov.cloud.tsp.rsms.simulator.domain.factory.ServerPlatformFactory;
import net.hwyz.iov.cloud.tsp.rsms.simulator.domain.server.model.ServerPlatformDo;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 服务端平台管理类
 *
 * @author hwyz_leo
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ServerPlatformManager {

    private final ServerPlatformService serverPlatformService;
    private final ServerPlatformFactory serverPlatformFactory;

    /**
     * 初始化
     */
    private void init() {
        logger.info("初始化所有服务端平台");
        start(serverPlatformFactory.build(ServerPlatformType.PLATFORM_FORWARD));
        start(serverPlatformFactory.build(ServerPlatformType.VEHICLE_CONNECT));
    }

    /**
     * 启动服务端
     *
     * @param serverPlatform 服务端平台
     */
    private void start(ServerPlatformDo serverPlatform) {
        serverPlatformService.start(serverPlatform);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        init();
    }

}
