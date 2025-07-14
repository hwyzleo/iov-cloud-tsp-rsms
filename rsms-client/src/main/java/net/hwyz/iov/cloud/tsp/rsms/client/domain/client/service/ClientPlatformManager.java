package net.hwyz.iov.cloud.tsp.rsms.client.domain.client.service;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.system.SystemUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.client.application.event.event.VehicleGbMessageEvent;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.model.ClientPlatformDo;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.repository.ClientPlatformRepository;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.cache.CacheService;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.util.NettyClient;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 客户端平台管理类
 *
 * @author hwyz_leo
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ClientPlatformManager {

    private final ApplicationContext ctx;
    private final CacheService cacheService;
    private final ClientPlatformRepository clientPlatformRepository;

    /**
     * 初始化
     */
    private void init() {
        logger.info("初始化所有启用客户端平台");
        cacheService.resetClientPlatformState();
        clientPlatformRepository.getAllEnabled().forEach(this::start);
    }

    /**
     * 启动
     *
     * @param clientPlatform 客户端平台
     */
    private void start(ClientPlatformDo clientPlatform) {
        String hostname = SystemUtil.getHostInfo().getName();
        if (!clientPlatform.checkHostname(hostname)) {
            return;
        }
        clientPlatform.bindHostname(hostname);
        clientPlatformRepository.save(clientPlatform);
        startNetty(clientPlatform);
    }

    /**
     * 启动Netty
     *
     * @param clientPlatform 客户端平台
     */
    private void startNetty(ClientPlatformDo clientPlatform) {
        logger.info("客户端平台[{}]启动Netty", clientPlatform.getUniqueKey());
        try {
            NettyClient nettyClient = ctx.getBean(NettyClient.class);
            ThreadUtil.newExecutor(1).submit(() -> {
                try {
                    nettyClient.connect(clientPlatform);
                } catch (Exception e) {
                    logger.error("启动客户端平台[{}]失败", clientPlatform.getUniqueKey(), e);
                }
            });
        } catch (Exception e) {
            logger.warn("启动客户端平台[{}]失败", clientPlatform.getUniqueKey(), e);
        }
    }

    /**
     * 订阅车辆国标消息事件
     *
     * @param event 车辆国标消息事件
     */
    @EventListener
    public void onVehicleGbMessageEvent(VehicleGbMessageEvent event) {
        clientPlatformRepository.getById(event.getClientPlatformId()).ifPresent(clientPlatform -> {
            if (clientPlatform.isLogin()) {
                clientPlatform.send(event.getGbMessage());
            }
        });
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        init();
    }

}
