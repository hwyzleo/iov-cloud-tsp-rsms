package net.hwyz.iov.cloud.tsp.rsms.simulator.application.service.protocol;

import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessage;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.dataunit.GbPlatformLoginAckDataUnit;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.dataunit.GbPlatformLoginDataUnit;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbAckFlag;
import net.hwyz.iov.cloud.tsp.rsms.api.util.GbUtil;
import net.hwyz.iov.cloud.tsp.rsms.simulator.application.service.GbProtocolHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 平台登录消息处理器
 *
 * @author hwyz_leo
 */
@Slf4j
@Component("PLATFORM_LOGIN_COMMAND")
public class PlatformLoginMessageHandler implements GbProtocolHandler {
    @Override
    public Optional<GbMessage> handle(GbMessage message) {
        GbPlatformLoginDataUnit platformLogin = new GbPlatformLoginDataUnit();
        platformLogin.parse(message.getDataUnitBytes());
        logger.info("收到客户端平台[{}]今日第[{}]次登录[{}]消息", platformLogin.getUsername(), platformLogin.getLoginSn(), GbUtil.dateTimeBytesToString(platformLogin.getLoginTime()));
        // TODO 校验登录用户名密码
        message.setDataUnit(new GbPlatformLoginAckDataUnit(platformLogin.getLoginTime()));
        message.getHeader().setAckFlag(GbAckFlag.FAILURE);
        message.getHeader().setDataUnitLength(message.getDataUnit().toByteArray().length);
        message.calculateCheckCode();
        logger.info("向客户端平台[{}]发送登录应答", platformLogin.getUsername());
        return Optional.of(message);
    }
}
