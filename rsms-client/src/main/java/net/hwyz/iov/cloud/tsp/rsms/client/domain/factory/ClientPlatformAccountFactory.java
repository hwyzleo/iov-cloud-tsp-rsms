package net.hwyz.iov.cloud.tsp.rsms.client.domain.factory;

import lombok.RequiredArgsConstructor;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.model.ClientPlatformAccountDo;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.po.ClientPlatformAccountPo;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 客户端平台账号领域工厂类
 *
 * @author hwyz_leo
 */
@Component
@RequiredArgsConstructor
public class ClientPlatformAccountFactory {

    private final ApplicationContext ctx;

    /**
     * 创建客户端平台账号领域对象
     *
     * @param clientPlatformAccount 客户端平台账号
     * @return 客户端平台账号领域对象
     */
    public ClientPlatformAccountDo build(ClientPlatformAccountPo clientPlatformAccount) {
        ClientPlatformAccountDo clientPlatformAccountDo = ClientPlatformAccountDo.builder()
                .id(clientPlatformAccount.getId())
                .username(clientPlatformAccount.getUsername())
                .password(clientPlatformAccount.getPassword())
                .useLimit(clientPlatformAccount.getUseLimit())
                .build();
        clientPlatformAccountDo.init();
        return clientPlatformAccountDo;
    }

}
