package net.hwyz.iov.cloud.tsp.rsms.client.domain.client.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.framework.common.domain.BaseDo;
import net.hwyz.iov.cloud.framework.common.domain.DomainObj;

/**
 * 客户端平台账号领域对象
 *
 * @author hwyz_leo
 */
@Slf4j
@Getter
@SuperBuilder
public class ClientPlatformAccountDo extends BaseDo<Long> implements DomainObj<ClientPlatformDo> {

    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 使用上限
     */
    private Integer useLimit;

    /**
     * 初始化
     */
    public void init() {
        stateInit();
    }

}
