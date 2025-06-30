package net.hwyz.iov.cloud.tsp.rsms.service.domain.client.service;

import cn.hutool.system.SystemUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.framework.common.util.StrUtil;
import net.hwyz.iov.cloud.tsp.rsms.service.application.event.event.VehicleGbMessageEvent;
import net.hwyz.iov.cloud.tsp.rsms.service.domain.client.model.ClientPlatformDo;
import net.hwyz.iov.cloud.tsp.rsms.service.domain.client.repository.ClientPlatformRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
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

    private final ClientPlatformService clientPlatformService;
    private final ClientPlatformRepository clientPlatformRepository;

    /**
     * 初始化
     */
    private void init() {
        logger.info("初始化所有启用客户端平台");
        clientPlatformRepository.getAllEnabled().forEach(this::start);
    }

    /**
     * 启动
     *
     * @param clientPlatform 客户端平台
     */
    private void start(ClientPlatformDo clientPlatform) {
        String hostname = SystemUtil.getHostInfo().getName();
        if (!checkHostname(hostname, clientPlatform.getHostname())) {
            // 主机名不匹配则跳过
            return;
        }
        clientPlatformService.start(clientPlatform);
    }

    /**
     * 检查主机名
     *
     * @param localHostname 本地主机名
     * @param bindHostname  绑定主机名
     * @return true:匹配成功
     */
    private boolean checkHostname(String localHostname, String bindHostname) {
        boolean isMatch = true;
        if (StrUtil.isNotBlank(bindHostname)) {
            isMatch = false;
            for (String hostname : bindHostname.split(",")) {
                if (hostname.equalsIgnoreCase(localHostname)) {
                    isMatch = true;
                    break;
                }
            }
        }
        return isMatch;
    }

    /**
     * 订阅车辆国标消息事件
     *
     * @param event 车辆国标消息事件
     */
    @EventListener
    public void onGbVehicleDataEvent(VehicleGbMessageEvent event) {
        clientPlatformRepository.getAllStarted().forEach(clientPlatform -> {
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
