package net.hwyz.iov.cloud.tsp.rsms.client.application.service.protocol;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessage;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.ProtocolMessage;
import net.hwyz.iov.cloud.tsp.rsms.client.application.service.ProtocolHandler;
import net.hwyz.iov.cloud.tsp.rsms.client.application.service.platform.GbPlatformHandler;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.model.ClientPlatformDo;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 国标平台登出成功消息处理器
 *
 * @author hwyz_leo
 */
@Slf4j
@Component("GB_PLATFORM_LOGOUT_SUCCESS")
@RequiredArgsConstructor
public class GbPlatformLogoutSuccessMessageHandler extends BaseGbMessageHandler implements ProtocolHandler {

    private final GbPlatformHandler platformHandler;

    @Override
    public Optional<ProtocolMessage> handle(ProtocolMessage message, ClientPlatformDo clientPlatform) {
        GbMessage gbMessage = convert(message);
        logger.info("客户端平台[{}]收到登出成功应答消息", gbMessage.getHeader().getUniqueCode());
        platformHandler.logoutSuccess(clientPlatform);
        return Optional.empty();
    }
}
