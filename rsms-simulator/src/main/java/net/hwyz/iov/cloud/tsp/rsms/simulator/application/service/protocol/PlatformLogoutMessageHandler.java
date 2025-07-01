package net.hwyz.iov.cloud.tsp.rsms.simulator.application.service.protocol;

import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessage;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.dataunit.GbPlatformLogoutAckDataUnit;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.dataunit.GbPlatformLogoutDataUnit;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbAckFlag;
import net.hwyz.iov.cloud.tsp.rsms.api.util.GbUtil;
import net.hwyz.iov.cloud.tsp.rsms.simulator.application.service.GbProtocolHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 平台登出消息处理器
 *
 * @author hwyz_leo
 */
@Slf4j
@Component("PLATFORM_LOGOUT_COMMAND")
public class PlatformLogoutMessageHandler implements GbProtocolHandler {

    @Override
    public Optional<GbMessage> handle(GbMessage message) {
        GbPlatformLogoutDataUnit platformLogout = new GbPlatformLogoutDataUnit();
        platformLogout.parse(message.getDataUnitBytes());
        logger.info("收到客户端平台[{}]今日第[{}]次登出[{}]消息", message.getHeader().getUniqueCode(), platformLogout.getLoginSn(), GbUtil.dateTimeBytesToString(platformLogout.getLogoutTime()));
        message.setDataUnit(new GbPlatformLogoutAckDataUnit(platformLogout.getLogoutTime()));
        message.getHeader().setDataUnitLength(message.getDataUnit().toByteArray().length);
        message.getHeader().setAckFlag(GbAckFlag.SUCCESS);
        message.calculateCheckCode();
        logger.info("向客户端平台[{}]发送登出应答", message.getHeader().getUniqueCode());
        return Optional.of(message);
    }

}
