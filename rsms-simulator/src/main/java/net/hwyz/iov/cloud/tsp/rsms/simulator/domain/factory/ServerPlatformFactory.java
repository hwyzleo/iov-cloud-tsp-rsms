package net.hwyz.iov.cloud.tsp.rsms.simulator.domain.factory;

import lombok.RequiredArgsConstructor;
import net.hwyz.iov.cloud.tsp.rsms.simulator.domain.contract.enums.ServerPlatformType;
import net.hwyz.iov.cloud.tsp.rsms.simulator.domain.server.model.ServerPlatformDo;
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
     * 基于服务平台类型创建
     *
     * @param serverPlatformType 服务平台类型
     * @return 服务端平台领域对象
     */
    public ServerPlatformDo build(ServerPlatformType serverPlatformType) {
        ServerPlatformDo serverPlatform = ServerPlatformDo.builder()
                .serverPlatformType(serverPlatformType)
                .build();
        serverPlatform.init();
        return serverPlatform;
    }

}
