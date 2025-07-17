package net.hwyz.iov.cloud.tsp.rsms.client.application.service;

import cn.hutool.core.util.ObjUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.client.application.event.event.ClientPlatformStateEvent;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.model.ClientPlatformDo;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.dao.ClientPlatformLoginHistoryDao;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.po.ClientPlatformLoginHistoryPo;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Date;
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
     * 记录客户端平台登录历史
     *
     * @param clientPlatform 客户端平台
     */
    public void recordLogin(ClientPlatformDo clientPlatform) {
        logger.info("记录客户端平台[{}:{}]登录[{}]历史", clientPlatform.getUniqueKey(), clientPlatform.getCurrentHostname(),
                clientPlatform.getLoginState().get());
        ClientPlatformLoginHistoryPo history = clientPlatformLoginHistoryDao.selectLastPoByClientPlatformId(clientPlatform.getId(),
                clientPlatform.getCurrentHostname());
        if (ObjUtil.isNull(history) || !dateCompare(history.getLoginTime(), clientPlatform.getLoginTime())) {
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
        logger.info("记录客户端平台[{}:{}]登出[{}]历史", clientPlatform.getUniqueKey(), clientPlatform.getCurrentHostname(),
                clientPlatform.getLoginState().get());
        ClientPlatformLoginHistoryPo history = clientPlatformLoginHistoryDao.selectLastPoByClientPlatformId(clientPlatform.getId(),
                clientPlatform.getCurrentHostname());
        if (ObjUtil.isNotNull(history) && dateCompare(history.getLoginTime(), clientPlatform.getLoginTime())) {
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

    /**
     * 时间比较
     * 3秒内差距属于一致
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return true:相同 false:不同
     */
    private boolean dateCompare(Date date1, Date date2) {
        return Math.abs((date1.getTime() / 1000) - (date2.getTime() / 1000)) < 3;
    }

    /**
     * 关闭未登出的记录
     */
    private void closeNotLogoutRecord() {
        List<ClientPlatformLoginHistoryPo> loginHistoryPoList = clientPlatformLoginHistoryDao.selectLogoutTimeIsNullPo();
        if (!loginHistoryPoList.isEmpty()) {
            logger.info("补偿关闭未登出记录");
            loginHistoryPoList.forEach(po -> {
                po.setLogoutTime(new Date());
                clientPlatformLoginHistoryDao.updatePo(po);
            });
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        closeNotLogoutRecord();
    }

    /**
     * 订阅客户端平台状态事件
     *
     * @param event 客户端平台状态事件
     */
    @EventListener
    public void onClientPlatformStateEvent(ClientPlatformStateEvent event) {
        ClientPlatformDo clientPlatform = event.getClientPlatform();
        switch (event.getState()) {
            case LOGIN_SUCCESS, LOGIN_FAILURE -> recordLogin(clientPlatform);
            case LOGOUT_SUCCESS -> recordLogout(clientPlatform);
        }
    }

}
