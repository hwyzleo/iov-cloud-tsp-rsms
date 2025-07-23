package net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.dao;

import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.po.RegisteredVehiclePo;
import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

/**
 * <p>
 * 服务端平台已注册车辆 DAO
 * </p>
 *
 * @author hwyz_leo
 * @since 2025-07-10
 */
@Mapper
public interface RegisteredVehicleDao extends BaseDao<RegisteredVehiclePo, Long> {

    /**
     * 根据客户端平台ID查询可上报的已注册车辆
     *
     * @param clientPlatformId 客户端平台ID
     * @return 已注册车辆列表
     */
    Set<String> selectReportVinByClientPlatformId(Long clientPlatformId);

}
