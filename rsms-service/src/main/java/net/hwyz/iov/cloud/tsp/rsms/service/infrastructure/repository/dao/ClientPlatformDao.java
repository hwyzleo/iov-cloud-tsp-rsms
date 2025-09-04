package net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao;

import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.ClientPlatformPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 客户端平台 DAO
 * </p>
 *
 * @author hwyz_leo
 * @since 2025-06-17
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
