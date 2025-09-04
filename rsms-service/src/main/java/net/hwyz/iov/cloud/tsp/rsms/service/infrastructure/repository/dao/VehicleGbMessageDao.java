package net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao;

import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.VehicleGbMessagePo;
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

}
