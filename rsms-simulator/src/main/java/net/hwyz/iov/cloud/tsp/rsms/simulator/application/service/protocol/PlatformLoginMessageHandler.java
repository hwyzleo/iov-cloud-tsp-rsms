package net.hwyz.iov.cloud.tsp.rsms.simulator.application.service.protocol;

import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessage;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.dataunit.GbPlatformLoginAckDataUnit;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.dataunit.GbPlatformLoginDataUnit;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbAckFlag;
import net.hwyz.iov.cloud.tsp.rsms.api.util.GbUtil;
import net.hwyz.iov.cloud.tsp.rsms.simulator.application.service.GbProtocolHandler;
import org.springframework.beans.factory.annotation.Value;
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

    /**
     * 模拟器校验的用户名
     */
    @Value("${biz.simulator.username}")
    private String username;
    /**
     * 模拟器校验的密码
     */
    @Value("${biz.simulator.password}")
    private String password;

    @Override
    public Optional<GbMessage> handle(GbMessage message) {
        GbPlatformLoginDataUnit platformLogin = new GbPlatformLoginDataUnit();
        platformLogin.parse(message.getDataUnitBytes());
        logger.info("收到客户端平台[{}]今日第[{}]次登录[{}]消息", message.getHeader().getUniqueCode(), platformLogin.getLoginSn(), GbUtil.getGbDateTimeBytes(platformLogin.getMessageTime().getTime()));
        if (platformLogin.getUsername().equals(username) && platformLogin.getPassword().equals(password)) {
            message.getHeader().setAckFlag(GbAckFlag.SUCCESS);
        } else {
            message.getHeader().setAckFlag(GbAckFlag.FAILURE);
        }
        GbPlatformLoginAckDataUnit dataUnit = new GbPlatformLoginAckDataUnit();
        dataUnit.setMessageTime(platformLogin.getMessageTime());
        message.setDataUnit(dataUnit);
        message.getHeader().setDataUnitLength(message.getDataUnit().toByteArray().length);
        message.calculateCheckCode();
        logger.info("向客户端平台[{}]发送登录应答", message.getHeader().getUniqueCode());
        return Optional.of(message);
    }

}
