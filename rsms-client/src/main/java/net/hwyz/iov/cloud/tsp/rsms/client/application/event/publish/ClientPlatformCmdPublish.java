package net.hwyz.iov.cloud.tsp.rsms.client.application.event.publish;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.ClientPlatformCmd;
import net.hwyz.iov.cloud.tsp.rsms.client.application.event.event.ClientPlatformCmdEvent;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.model.ClientPlatformDo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * 客户端平台命令发布类
 *
 * @author hwyz_leo
 */
@Slf4j
@Component
@RequiredArgsConstructor
@DependsOn("clientPlatformManager")
public class ClientPlatformCmdPublish {

    private final ApplicationContext ctx;

    /**
     * 发送平台命令消息
     *
     * @param clientPlatform 客户端平台
     * @param cmd            客户端平台命令
     */
    public void sendPlatformCmd(ClientPlatformDo clientPlatform, ClientPlatformCmd cmd) {
        ctx.publishEvent(new ClientPlatformCmdEvent(clientPlatform, cmd));
    }

}
