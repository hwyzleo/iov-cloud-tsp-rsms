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
     * @param clientPlatformId 客户端平台ID
     * @return 已上报车辆列表
     */
    public Set<String> getClientPlatformReportVin(Long clientPlatformId) {
        return registeredVehicleDao.selectReportVinByClientPlatformId(clientPlatformId);
    }

}
