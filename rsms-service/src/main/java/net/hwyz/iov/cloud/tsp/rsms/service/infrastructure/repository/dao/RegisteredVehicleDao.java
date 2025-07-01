package net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao;

import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.RegisteredVehiclePo;
import org.apache.ibatis.annotations.Mapper;

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
     * 根据代码查询已注册车辆
     *
     * @param serverPlatformCode 服务端平台代码
     * @param vin                车架号
     * @return 已注册车辆
     */
    RegisteredVehiclePo selectPoByServerPlatformCodeAndVin(String serverPlatformCode, String vin);

    /**
     * 批量物理删除已注册车辆
     *
     * @param ids 已注册车辆id数组
     * @return 影响行数
     */
    int batchPhysicalDeletePo(Long[] ids);

}
