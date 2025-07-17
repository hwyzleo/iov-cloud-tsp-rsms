package net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.dao;

import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.po.VehicleGbMessagePo;
import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 车辆国标消息 DAO
 * </p>
 *
 * @author hwyz_leo
 * @since 2025-07-17
 */
@Mapper
public interface VehicleGbMessageDao extends BaseDao<VehicleGbMessagePo, Long> {

    /**
     * 查询指定时间段内的消息
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 消息列表
     */
    List<VehicleGbMessagePo> selectByStartAndEndTime(Date startTime, Date endTime);

}
