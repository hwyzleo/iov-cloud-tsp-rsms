package net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.dao;

import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.po.ClientPlatformAccountPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 客户端平台账号 DAO
 * </p>
 *
 * @author hwyz_leo
 * @since 2025-07-21
 */
@Mapper
public interface ClientPlatformAccountDao extends BaseDao<ClientPlatformAccountPo, Long> {

    /**
     * 查询所有启用的客户端平台账号
     *
     * @param clientPlatformId 客户端平台ID
     * @return 启用的客户端平台账号
     */
    List<ClientPlatformAccountPo> selectPoByEnabled(Long clientPlatformId);

}
