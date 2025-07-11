package net.hwyz.iov.cloud.tsp.rsms.client.application.service;

import cn.hutool.core.util.ObjUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.model.ClientPlatformDo;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.dao.ClientPlatformLoginHistoryDao;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.po.ClientPlatformLoginHistoryPo;
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
                    .hostname(clientPlatform.getCurrentHostname())
                    .loginTime(clientPlatform.getLoginTime())
                    .loginSn(clientPlatform.getLoginSn())
                    .loginResult(clientPlatform.getLoginState().get())
                    .failureReason(clientPlatform.getFailureReason().get())
                    .failureCount(clientPlatform.getFailureCount().get())
                    .build());
        }
    }

    /**
     * 记录客户端平台登出历史
     *
     * @param clientPlatform 客户端平台
     */
    public void recordLogout(ClientPlatformDo clientPlatform) {
        logger.info("记录客户端平台[{}]登出[{}]历史", clientPlatform.getUniqueKey(), clientPlatform.getLoginState().get());
        ClientPlatformLoginHistoryPo history = clientPlatformLoginHistoryDao.selectLastPoByClientPlatformId(clientPlatform.getId());
        if (ObjUtil.isNotNull(history) && history.getLoginSn() == clientPlatform.getLoginSn()) {
            history.setLogoutTime(clientPlatform.getLogoutTime());
            clientPlatformLoginHistoryDao.updatePo(history);
        } else {
            logger.warn("记录客户端平台[{}]登出历史异常[无对应登录记录]", clientPlatform.getUniqueKey());
            clientPlatformLoginHistoryDao.insertPo(ClientPlatformLoginHistoryPo.builder()
                    .clientPlatformId(clientPlatform.getId())
                    .hostname(clientPlatform.getCurrentHostname())
                    .loginTime(clientPlatform.getLoginTime())
                    .loginSn(clientPlatform.getLoginSn())
                    .loginResult(clientPlatform.getLoginState().get())
                    .failureReason(clientPlatform.getFailureReason().get())
                    .failureCount(clientPlatform.getFailureCount().get())
                    .build());
        }
    }

}
