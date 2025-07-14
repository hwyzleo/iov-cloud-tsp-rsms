package net.hwyz.iov.cloud.tsp.rsms.client.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.server.repository.ServerPlatformRepository;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.dao.RegisteredVehicleDao;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 服务端平台应用服务类
 *
 * @author hwyz_leo
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ServerPlatformAppService {

    private final RegisteredVehicleDao registeredVehicleDao;
    private final ServerPlatformRepository serverPlatformRepository;

    /**
     * 同步所有平台已注册车辆集合
     */
    public void syncVehicleSet() {
        serverPlatformRepository.getAll().forEach(serverPlatform -> {
            Set<String> vehicleSet = registeredVehicleDao.selectReportVinByServerPlatformCode(serverPlatform.getCode());
            serverPlatform.syncVehicleSet(vehicleSet);
            serverPlatformRepository.save(serverPlatform);
        });
    }

}
