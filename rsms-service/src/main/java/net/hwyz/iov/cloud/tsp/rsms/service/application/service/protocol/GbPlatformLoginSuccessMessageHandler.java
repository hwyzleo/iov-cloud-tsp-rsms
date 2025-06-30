package net.hwyz.iov.cloud.tsp.rsms.service.application.service.protocol;

import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessage;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.ProtocolMessage;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.ProtocolHandler;
import net.hwyz.iov.cloud.tsp.rsms.service.domain.client.model.ClientPlatformDo;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 国标平台登录成功消息处理器
 *
 * @author hwyz_leo
 */
@Slf4j
@Component("GB_PLATFORM_LOGIN_SUCCESS")
public class GbPlatformLoginSuccessMessageHandler extends BaseGbMessageHandler implements ProtocolHandler {
    @Override
    public Optional<ProtocolMessage> handle(ProtocolMessage message, ClientPlatformDo clientPlatform) {
        GbMessage gbMessage = convert(message);
        logger.info("收到客户端平台[{}]登录成功消息", gbMessage.getHeader().getUniqueCode());
        return Optional.empty();
    }
}
