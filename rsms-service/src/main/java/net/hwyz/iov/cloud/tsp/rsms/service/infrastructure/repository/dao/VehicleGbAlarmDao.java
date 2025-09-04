package net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao;

import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.VehicleGbAlarmPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
     * 根据车辆vin、报警标识查询车辆最后未恢复国标报警
     *
     * @param vin       车架号
     * @param alarmFlag 报警标识
     * @return 车辆国标报警
     */
    VehicleGbAlarmPo selectLastNotRestorationByVinAndAlarmFlag(String vin, Integer alarmFlag);

    /**
     * 查询车辆30天内频繁报警车辆
     *
     * @return 车辆vin列表
     */
    List<String> selectFrequentAlarmVehicleIn30Days();

}
