package net.hwyz.iov.cloud.tsp.rsms.simulator.infrastructure.repository.dao;

import net.hwyz.iov.cloud.tsp.rsms.simulator.infrastructure.repository.po.ClientPlatformPo;
import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 客户端平台 DAO
 * </p>
 *
 * @author hwyz_leo
 * @since 2025-06-20
 */
@Mapper
public interface ClientPlatformDao extends BaseDao<ClientPlatformPo, Long> {

}
