package net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao;

import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.VehicleGbAlarmPo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 车辆国标报警 DAO
 * </p>
 *
 * @author hwyz_leo
 * @since 2025-07-17
 */
@Mapper
public interface VehicleGbAlarmDao extends BaseDao<VehicleGbAlarmPo, Long> {

    /**
     * 批量物理删除车辆国标报警
     *
     * @param ids 车辆国标报警id数组
     * @return 影响行数
     */
    int batchPhysicalDeletePo(Long[] ids);

    /**
     * 根据车辆vin、报警标识查询车辆最后未恢复国标报警
     *
     * @param vin       车架号
     * @param alarmFlag 报警标识
     * @return 车辆国标报警
     */
    VehicleGbAlarmPo selectLastNotRestorationByVinAndAlarmFlag(String vin, Integer alarmFlag);

}
