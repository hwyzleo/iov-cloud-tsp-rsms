package net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao;

import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.ClientPlatformAccountPo;
import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

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
     * 批量物理删除客户端平台账号
     *
     * @param ids 服务端平台id数组
     * @return 影响行数
     */
    int batchPhysicalDeletePo(Long[] ids);

}
