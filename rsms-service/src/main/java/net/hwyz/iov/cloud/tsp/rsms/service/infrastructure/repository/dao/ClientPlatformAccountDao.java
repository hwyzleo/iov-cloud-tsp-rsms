package net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao;

import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.ClientPlatformAccountPo;
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

}
