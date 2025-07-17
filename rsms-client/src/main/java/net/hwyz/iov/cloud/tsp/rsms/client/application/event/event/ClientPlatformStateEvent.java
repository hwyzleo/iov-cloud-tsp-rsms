package net.hwyz.iov.cloud.tsp.rsms.client.application.event.event;

import lombok.Getter;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.ClientPlatformState;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.model.ClientPlatformDo;

/**
 * 客户端平台状态事件
 *
 * @author hwyz_leo
 */
@Getter
public class ClientPlatformStateEvent extends BaseEvent {

    /**
     * 客户端平台
     */
    private final ClientPlatformDo clientPlatform;
    /**
     * 客户端平台状态
     */
    private final ClientPlatformState state;

    public ClientPlatformStateEvent(ClientPlatformDo clientPlatform, ClientPlatformState state) {
        super();
        this.clientPlatform = clientPlatform;
        this.state = state;
    }

}
