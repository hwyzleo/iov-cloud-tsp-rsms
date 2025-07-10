package net.hwyz.iov.cloud.tsp.rsms.client.domain.factory;

import cn.hutool.core.util.ObjUtil;
import lombok.RequiredArgsConstructor;
import net.hwyz.iov.cloud.tsp.rsms.client.application.service.ProtocolPackager;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.model.ClientPlatformDo;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.server.model.ServerPlatformDo;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.po.ClientPlatformLoginHistoryPo;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.po.ClientPlatformPo;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

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
     * @param clientPlatformPo 客户端平台
     * @param serverPlatformDo 服务端平台
     * @param loginHistory     登录历史
     * @return 客户端平台领域对象
     */
    public ClientPlatformDo build(ClientPlatformPo clientPlatformPo, ServerPlatformDo serverPlatformDo,
                                  ClientPlatformLoginHistoryPo loginHistory) {
        ProtocolPackager packager = ctx.getBean(serverPlatformDo.getProtocol().getCode() + "ProtocolPackager", ProtocolPackager.class);
        ClientPlatformDo clientPlatform = ClientPlatformDo.builder()
                .id(clientPlatformPo.getId())
                .serverPlatform(serverPlatformDo)
                .hostname(clientPlatformPo.getHostname())
                .username(clientPlatformPo.getUsername())
                .password(clientPlatformPo.getPassword())
                .uniqueCode(clientPlatformPo.getUniqueCode())
                .loginSn(ObjUtil.isNotNull(loginHistory) ? new AtomicInteger(loginHistory.getLoginSn()) : new AtomicInteger(1))
                .loginTime(ObjUtil.isNotNull(loginHistory) ? loginHistory.getLoginTime() : null)
                .failureReason(ObjUtil.isNotNull(loginHistory) ? new AtomicInteger(loginHistory.getFailureReason()) : new AtomicInteger(0))
                .failureCount(ObjUtil.isNotNull(loginHistory) ? new AtomicInteger(loginHistory.getFailureCount()) : new AtomicInteger(0))
                .packager(packager)
                .build();
        clientPlatform.init();
        return clientPlatform;
    }

}
