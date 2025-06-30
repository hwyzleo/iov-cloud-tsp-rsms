package net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao;

import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.ServerPlatformPo;
import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 服务端平台 DAO
 * </p>
 *
 * @author hwyz_leo
 * @since 2025-06-17
 */
@Mapper
public interface ServerPlatformDao extends BaseDao<ServerPlatformPo, Long> {

    /**
     * 根据代码查询服务端平台
     *
     * @param code 服务端平台代码
     * @return 服务端平台
     */
    ServerPlatformPo selectPoByCode(String code);

    /**
     * 批量物理删除服务端平台
     *
     * @param ids 服务端平台id数组
     * @return 影响行数
     */
    int batchPhysicalDeletePo(Long[] ids);

}
