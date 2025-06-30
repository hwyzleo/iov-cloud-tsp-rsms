package net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao;

import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.ClientPlatformPo;
import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
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

    /**
     * 批量物理删除客户端平台
     *
     * @param ids 服务端平台id数组
     * @return 影响行数
     */
    int batchPhysicalDeletePo(Long[] ids);

}
