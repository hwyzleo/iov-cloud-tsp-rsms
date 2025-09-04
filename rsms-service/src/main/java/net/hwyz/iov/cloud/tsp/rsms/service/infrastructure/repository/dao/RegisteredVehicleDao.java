package net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao;

import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.RegisteredVehiclePo;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

/**
 * <p>
 * 服务端平台已注册车辆 DAO
 * </p>
 *
 * @author hwyz_leo
 * @since 2025-07-01
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

    /**
     * 根据客户端平台ID及车架号查询已注册车辆
     *
     * @param clientPlatformId 客户端平台ID
     * @param vin              车架号
     * @return 已注册车辆
     */
    RegisteredVehiclePo selectPoByClientPlatformIdAndVin(Long clientPlatformId, String vin);

}
