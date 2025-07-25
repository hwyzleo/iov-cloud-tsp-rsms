package net.hwyz.iov.cloud.tsp.rsms.client.domain.factory;

import lombok.RequiredArgsConstructor;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbDataUnitEncryptType;
import net.hwyz.iov.cloud.tsp.rsms.client.application.service.ProtocolPackager;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.model.ClientPlatformAccountDo;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.model.ClientPlatformDo;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.server.model.ServerPlatformDo;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.po.ClientPlatformPo;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 客户端平台领域工厂类
 *
 * @author hwyz_leo
 */
@Component
@RequiredArgsConstructor
public class ClientPlatformFactory {

    private final ApplicationContext ctx;

    /**
     * 创建客户端平台领域对象
     *
     * @param clientPlatform 客户端平台
     * @param serverPlatform 服务端平台
     * @param vehicleSet     车辆集合
     * @return 客户端平台领域对象
     */
    public ClientPlatformDo build(ClientPlatformPo clientPlatform, ServerPlatformDo serverPlatform, List<ClientPlatformAccountDo> accounts,
                                  Set<String> vehicleSet) {
        ProtocolPackager packager = ctx.getBean(serverPlatform.getProtocol().getCode() + "ProtocolPackager", ProtocolPackager.class);
        ClientPlatformDo clientPlatformDo = ClientPlatformDo.builder()
                .id(clientPlatform.getId())
                .serverPlatform(serverPlatform)
                .uniqueCode(clientPlatform.getUniqueCode())
                .collectFrequency(clientPlatform.getCollectFrequency())
                .reportFrequency(clientPlatform.getReportFrequency())
                .encryptType(GbDataUnitEncryptType.valOf(clientPlatform.getEncryptType().byteValue()))
                .encryptKey(clientPlatform.getEncryptKey())
                .hostname(clientPlatform.getHostname())
                .accounts(accounts)
                .packager(packager)
                .vehicleSet(vehicleSet)
                .build();
        clientPlatformDo.init();
        return clientPlatformDo;
    }

}
