package net.hwyz.iov.cloud.tsp.rsms.client.application.event.publish;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.ClientPlatformState;
import net.hwyz.iov.cloud.tsp.rsms.client.application.event.event.ClientPlatformStateEvent;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.model.ClientPlatformDo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * 客户端平台状态发布类
 *
 * @author hwyz_leo
 */
@Slf4j
@Component
@RequiredArgsConstructor
@DependsOn("clientPlatformManager")
public class ClientPlatformStatePublish {

    private final ApplicationContext ctx;

    /**
     * 发送平台状态消息
     *
     * @param clientPlatform 客户端平台
     * @param state          客户端平台状态
     */
    public void sendPlatformState(ClientPlatformDo clientPlatform, ClientPlatformState state) {
        ctx.publishEvent(new ClientPlatformStateEvent(clientPlatform, state));
    }

}
