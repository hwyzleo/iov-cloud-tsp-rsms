package net.hwyz.iov.cloud.tsp.rsms.service.domain.factory;

import lombok.RequiredArgsConstructor;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbDataUnitEncryptType;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.ProtocolType;
import net.hwyz.iov.cloud.tsp.rsms.service.domain.server.model.ServerPlatformDo;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.ServerPlatformPo;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 服务端平台领域工厂类
 *
 * @author hwyz_leo
 */
@Component
@RequiredArgsConstructor
public class ServerPlatformFactory {

    /**
     * 创建服务端平台领域对象
     *
     * @param serverPlatformPo 服务端平台
     * @param vehicleSet       已注册车辆集合
     * @return 服务端平台领域对象
     */
    public ServerPlatformDo build(ServerPlatformPo serverPlatformPo, Set<String> vehicleSet) {
        ServerPlatformDo serverPlatform = ServerPlatformDo.builder()
                .id(serverPlatformPo.getCode())
                .name(serverPlatformPo.getName())
                .type(serverPlatformPo.getType())
                .url(serverPlatformPo.getUrl())
                .port(serverPlatformPo.getPort())
                .protocol(ProtocolType.valOf(serverPlatformPo.getProtocol()))
                .collectFrequency(serverPlatformPo.getCollectFrequency())
                .reportFrequency(serverPlatformPo.getReportFrequency())
                .readWriteSync(serverPlatformPo.getReadWriteSync())
                .heartbeat(serverPlatformPo.getHeartbeat())
                .encryptType(GbDataUnitEncryptType.valOf((byte) serverPlatformPo.getEncryptType().intValue()))
                .encryptKey(serverPlatformPo.getEncryptKey())
                .vehicleSet(vehicleSet)
                .build();
        serverPlatform.init();
        return serverPlatform;
    }

}
