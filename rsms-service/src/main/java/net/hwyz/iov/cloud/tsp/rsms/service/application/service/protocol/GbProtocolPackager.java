package net.hwyz.iov.cloud.tsp.rsms.service.application.service.protocol;

import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessage;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessageDataUnit;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessageHeader;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.ProtocolMessage;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.dataunit.GbPlatformLoginDataUnit;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.dataunit.GbPlatformLogoutDataUnit;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbAckFlag;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbCommandFlag;
import net.hwyz.iov.cloud.tsp.rsms.api.util.GbUtil;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.ProtocolPackager;
import net.hwyz.iov.cloud.tsp.rsms.service.domain.client.model.ClientPlatformDo;
import org.springframework.stereotype.Component;

import static net.hwyz.iov.cloud.tsp.rsms.api.contract.constant.GbConstants.GB_DATA_STARTING_SYMBOLS;

/**
 * 国标协议数据处理器
 *
 * @author hwyz_leo
 */
@Slf4j
@Component("gbProtocolPackager")
public class GbProtocolPackager implements ProtocolPackager {

    @Override
    public ProtocolMessage platformLogin(ClientPlatformDo clientPlatform) {
        GbMessage gbData = GbMessage.builder()
                .startingSymbols(GB_DATA_STARTING_SYMBOLS)
                .build();
        GbMessageDataUnit dataUnit = new GbPlatformLoginDataUnit(
                GbUtil.getGbDateTimeBytes(System.currentTimeMillis()), clientPlatform.getLoginSn(),
                clientPlatform.getUsername(), clientPlatform.getPassword(),
                clientPlatform.getServerPlatform().getEncryptType());
        gbData.setDataUnit(dataUnit);
        GbMessageHeader header = GbMessageHeader.builder()
                .commandFlag(GbCommandFlag.PLATFORM_LOGIN)
                .ackFlag(GbAckFlag.COMMAND)
                .uniqueCode(clientPlatform.getUniqueCode())
                .dataUnitEncryptType(clientPlatform.getServerPlatform().getEncryptType())
                .dataUnitLength(dataUnit.toByteArray().length)
                .build();
        gbData.setHeader(header);
        gbData.calculateCheckCode();
        gbData.setCommandFlag(header.getCommandFlag().getCommandFlag());
        return gbData;
    }

    @Override
    public ProtocolMessage platformLogout(ClientPlatformDo clientPlatform) {
        GbMessage gbData = GbMessage.builder()
                .startingSymbols(GB_DATA_STARTING_SYMBOLS)
                .build();
        GbMessageDataUnit dataUnit = new GbPlatformLogoutDataUnit(GbUtil.getGbDateTimeBytes(System.currentTimeMillis()),
                clientPlatform.getLoginSn());
        gbData.setDataUnit(dataUnit);
        GbMessageHeader header = GbMessageHeader.builder()
                .commandFlag(GbCommandFlag.PLATFORM_LOGOUT)
                .ackFlag(GbAckFlag.COMMAND)
                .uniqueCode(clientPlatform.getUniqueCode())
                .dataUnitEncryptType(clientPlatform.getServerPlatform().getEncryptType())
                .dataUnitLength(dataUnit.toByteArray().length)
                .build();
        gbData.setHeader(header);
        gbData.calculateCheckCode();
        gbData.setCommandFlag(header.getCommandFlag().getCommandFlag());
        return gbData;
    }
}
