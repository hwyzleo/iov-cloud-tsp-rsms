package net.hwyz.iov.cloud.tsp.rsms.client.application.event.event;

import lombok.Getter;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.ClientPlatformCmd;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.model.ClientPlatformDo;

/**
 * 客户端平台指令事件
 *
 * @author hwyz_leo
 */
@Getter
public class ClientPlatformCmdEvent extends BaseEvent {

    /**
     * 客户端平台
     */
    private final ClientPlatformDo clientPlatform;
    /**
     * 客户端平台命令
     */
    private final ClientPlatformCmd cmd;

    public ClientPlatformCmdEvent(ClientPlatformDo clientPlatform, ClientPlatformCmd cmd) {
        super();
        this.clientPlatform = clientPlatform;
        this.cmd = cmd;
    }

}
