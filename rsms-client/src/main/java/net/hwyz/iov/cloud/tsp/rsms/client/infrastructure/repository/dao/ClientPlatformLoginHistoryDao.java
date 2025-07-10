package net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.dao;

import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.po.ClientPlatformLoginHistoryPo;
import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 客户端平台登录历史 DAO
 * </p>
 *
 * @author hwyz_leo
 * @since 2025-07-10
 */
@Mapper
public interface ClientPlatformLoginHistoryDao extends BaseDao<ClientPlatformLoginHistoryPo, Long> {

    /**
     * 查询指定客户端平台最后一次登录历史
     *
     * @param clientPlatformId 客户端平台ID
     * @return 客户端平台登录历史
     */
    ClientPlatformLoginHistoryPo selectLastPoByClientPlatformId(Long clientPlatformId);

}
