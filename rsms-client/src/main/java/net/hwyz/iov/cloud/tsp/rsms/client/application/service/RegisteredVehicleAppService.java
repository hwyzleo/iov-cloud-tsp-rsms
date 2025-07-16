package net.hwyz.iov.cloud.tsp.rsms.client.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.dao.RegisteredVehicleDao;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 已注册车辆应用服务类
 *
 * @author hwyz_leo
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RegisteredVehicleAppService {

    private final RegisteredVehicleDao registeredVehicleDao;

    /**
     * 查询指定平台已上报车辆
     *
     * @param serverPlatformCode 服务端平台代码
     * @return 已上报车辆
     */
    public Set<String> getServerPlatformReportVin(String serverPlatformCode) {
        return registeredVehicleDao.selectReportVinByServerPlatformCode(serverPlatformCode);
    }

}
