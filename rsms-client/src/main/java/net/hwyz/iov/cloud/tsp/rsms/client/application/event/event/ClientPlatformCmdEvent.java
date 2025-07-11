package net.hwyz.iov.cloud.tsp.rsms.client.application.event.event;

import lombok.Getter;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.CommandFlag;
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
     * 命令标识
     */
    private final CommandFlag commandFlag;

    public ClientPlatformCmdEvent(ClientPlatformDo clientPlatform, CommandFlag commandFlag) {
        super();
        this.clientPlatform = clientPlatform;
        this.commandFlag = commandFlag;
    }

}
