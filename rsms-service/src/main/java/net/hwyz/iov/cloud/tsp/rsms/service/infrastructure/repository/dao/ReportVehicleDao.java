package net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao;

import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.ReportVehiclePo;
import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 上报车辆 DAO
 * </p>
 *
 * @author hwyz_leo
 * @since 2025-07-24
 */
@Mapper
public interface ReportVehicleDao extends BaseDao<ReportVehiclePo, Long> {

    /**
     * 根据车架号查询上报车辆
     *
     * @param vin 车架号
     * @return 上报车辆
     */
    ReportVehiclePo selectPoByVin(String vin);

    /**
     * 批量物理删除
     *
     * @param ids id数组
     * @return 影响行数
     */
    int batchPhysicalDeletePo(Long[] ids);

}
