package net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.dao;

import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.po.ClientPlatformPo;
import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 客户端平台 DAO
 * </p>
 *
 * @author hwyz_leo
 * @since 2025-07-10
 */
@Mapper
public interface ClientPlatformDao extends BaseDao<ClientPlatformPo, Long> {

    /**
     * 查询所有启用的客户端平台
     *
     * @return 启用的客户端平台
     */
    List<ClientPlatformPo> selectPoByEnabled();

}
