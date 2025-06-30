package net.hwyz.iov.cloud.tsp.rsms.service.application.service;

import cn.hutool.core.util.ObjUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.service.domain.client.model.ClientPlatformDo;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao.ClientPlatformLoginHistoryDao;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.ClientPlatformLoginHistoryPo;
import org.springframework.stereotype.Service;

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
     * 记录客户端平台登录历史
     *
     * @param clientPlatform 客户端平台
     */
    public void recordLogin(ClientPlatformDo clientPlatform) {
        logger.info("记录客户端平台[{}]登录[{}]历史", clientPlatform.getUniqueKey(), clientPlatform.getLoginState().get());
        ClientPlatformLoginHistoryPo history = clientPlatformLoginHistoryDao.selectLastPoByClientPlatformId(clientPlatform.getId());
        if (ObjUtil.isNull(history) || history.getLoginSn() != clientPlatform.getLoginSn()) {
            clientPlatformLoginHistoryDao.insertPo(ClientPlatformLoginHistoryPo.builder()
                    .clientPlatformId(clientPlatform.getId())
                    .loginTime(clientPlatform.getLoginTime())
                    .loginSn(clientPlatform.getLoginSn())
                    .loginResult(clientPlatform.getLoginState().get())
                    .failureReason(clientPlatform.getFailureReason().get())
                    .failureCount(clientPlatform.getFailureCount().get())
                    .build());
        }
    }

    /**
     * 获取客户端平台最后登录历史
     *
     * @param clientPlatformId 客户端平台ID
     * @return 客户端平台登录历史
     */
    public ClientPlatformLoginHistoryPo getLastLoginHistory(Long clientPlatformId) {
        return clientPlatformLoginHistoryDao.selectLastPoByClientPlatformId(clientPlatformId);
    }

}
