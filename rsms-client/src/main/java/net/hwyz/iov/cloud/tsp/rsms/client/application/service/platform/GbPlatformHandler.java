package net.hwyz.iov.cloud.tsp.rsms.client.application.service.platform;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.ProtocolType;
import net.hwyz.iov.cloud.tsp.rsms.client.application.event.event.NettyClientConnectEvent;
import net.hwyz.iov.cloud.tsp.rsms.client.application.event.event.ClientPlatformCmdEvent;
import net.hwyz.iov.cloud.tsp.rsms.client.application.service.ClientPlatformLoginHistoryAppService;
import net.hwyz.iov.cloud.tsp.rsms.client.application.service.PlatformHandler;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.model.ClientPlatformDo;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.repository.ClientPlatformRepository;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.cache.CacheService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 国标平台业务处理器
 *
 * @author hwyz_leo
 */
@Slf4j
@RequiredArgsConstructor
@Component("gbPlatformHandler")
public class GbPlatformHandler implements PlatformHandler {

    private final CacheService cacheService;
    private final ClientPlatformRepository clientPlatformRepository;
    private final ClientPlatformLoginHistoryAppService clientPlatformLoginHistoryAppService;

    /**
     * 登录重试最大次数
     */
    private final static int MAX_RETRY_COUNT = 3;
    /**
     * 登录重试短间隔时间
     */
    @Value("${biz.gbLoginRetryShortInterval:60}")
    private Integer loginRetryShortInterval;
    /**
     * 登录重试长间隔时间
     */
    @Value("${biz.gbLoginRetryShortInterval:1800}")
    private Integer loginRetryLongInterval;

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
        // 客户端平台在规定时间内未收到应答指令，应每间隔1min重新进行登入；若连续重复3次登人无应答，应间隔30min后，
        // 继续重新链接，并把链接成功前存储的未成功发送的数据重新上报，重复登入间隔时间可以设置。
        if (clientPlatform.getFailureCount().get() < MAX_RETRY_COUNT) {
            logger.info("等待[{}]秒后重试登录", loginRetryShortInterval);
            try {
                Thread.sleep(loginRetryShortInterval * 1000);
                clientPlatform.login();
            } catch (InterruptedException e) {
                logger.warn("等待线程被中断");
            }
        } else {
            ThreadUtil.newExecutor(1).submit(() -> {
                logger.info("等待[{}]秒后重试登录", loginRetryLongInterval);
                try {
                    Thread.sleep(loginRetryLongInterval * 1000);
                    clientPlatform.resetLogin();
                } catch (InterruptedException e) {
                    logger.warn("等待线程被中断");
                }
            });
        }
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

    /**
     * 订阅Netty客户端连接事件
     *
     * @param event 客户端平台登录事件
     */
    @EventListener
    public void onNettyClientConnectEvent(NettyClientConnectEvent event) {
        ClientPlatformDo clientPlatform = event.getClientPlatform();
        if (ProtocolType.GB == clientPlatform.getServerPlatform().getProtocol()) {
            if (event.getConnectResult()) {
                connectSuccess(clientPlatform);
                login(clientPlatform);
            } else {
                logout(clientPlatform);
                connectFailure(clientPlatform);
            }
        }
    }

    /**
     * 订阅客户端平台命令事件
     *
     * @param event 客户端平台命令事件
     */
    @EventListener
    public void onClientPlatformCmdEvent(ClientPlatformCmdEvent event) {
        ClientPlatformDo clientPlatform = event.getClientPlatform();
        if (ProtocolType.GB == clientPlatform.getServerPlatform().getProtocol()) {
            switch (event.getCommandFlag()) {
                case PLATFORM_LOGIN -> login(clientPlatform);
                case PLATFORM_LOGOUT -> logout(clientPlatform);
            }
        }
    }

}
