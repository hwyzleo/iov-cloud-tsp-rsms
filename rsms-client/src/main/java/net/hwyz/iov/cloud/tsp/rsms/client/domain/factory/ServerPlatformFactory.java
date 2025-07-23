package net.hwyz.iov.cloud.tsp.rsms.client.domain.factory;

import lombok.RequiredArgsConstructor;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.ProtocolType;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.server.model.ServerPlatformDo;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.po.ServerPlatformPo;
import org.springframework.stereotype.Component;

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
     * @return 服务端平台领域对象
     */
    public ServerPlatformDo build(ServerPlatformPo serverPlatformPo) {
        ServerPlatformDo serverPlatform = ServerPlatformDo.builder()
                .id(serverPlatformPo.getCode())
                .code(serverPlatformPo.getCode())
                .name(serverPlatformPo.getName())
                .type(serverPlatformPo.getType())
                .url(serverPlatformPo.getUrl())
                .port(serverPlatformPo.getPort())
                .protocol(ProtocolType.valOf(serverPlatformPo.getProtocol()))
                .readWriteSync(serverPlatformPo.getReadWriteSync())
                .heartbeat(serverPlatformPo.getHeartbeat())
                .build();
        serverPlatform.init();
        return serverPlatform;
    }

}
