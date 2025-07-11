package net.hwyz.iov.cloud.tsp.rsms.service.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao.ClientPlatformLoginHistoryDao;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.ClientPlatformLoginHistoryPo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 客户端平台登录历史应用服务类
 *
 * @author hwyz_leo
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClientPlatformLoginHistoryAppService {

    private final ClientPlatformLoginHistoryDao clientPlatformLoginHistoryDao;

    /**
     * 获取客户端平台最后登录历史
     *
     * @param clientPlatformId 客户端平台ID
     * @return 客户端平台登录历史
     */
    public ClientPlatformLoginHistoryPo getLastLoginHistory(Long clientPlatformId) {
        return clientPlatformLoginHistoryDao.selectLastPoByClientPlatformId(clientPlatformId);
    }

    /**
     * 获取客户端平台登录历史
     *
     * @param clientPlatformId 客户端平台ID
     * @return 登录历史
     */
    public List<ClientPlatformLoginHistoryPo> listLoginHistory(Long clientPlatformId) {
        return clientPlatformLoginHistoryDao.selectPoByExample(ClientPlatformLoginHistoryPo.builder()
                .clientPlatformId(clientPlatformId)
                .build());
    }

}
