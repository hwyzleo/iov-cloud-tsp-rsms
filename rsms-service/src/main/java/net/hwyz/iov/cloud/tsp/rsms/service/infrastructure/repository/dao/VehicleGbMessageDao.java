package net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao;

import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.VehicleGbMessagePo;
import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 车辆国标消息 DAO
 * </p>
 *
 * @author hwyz_leo
 * @since 2025-07-02
 */
@Mapper
public interface VehicleGbMessageDao extends BaseDao<VehicleGbMessagePo, Long> {

    /**
     * 批量物理删除车辆国标消息
     *
     * @param ids 车辆国标消息id数组
     * @return 影响行数
     */
    int batchPhysicalDeletePo(Long[] ids);

}
