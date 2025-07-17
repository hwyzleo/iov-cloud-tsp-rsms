package net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.dao;

import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.po.ReissueTimePeriodPo;
import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 平台补发时间段 DAO
 * </p>
 *
 * @author hwyz_leo
 * @since 2025-07-16
 */
@Mapper
public interface ReissueTimePeriodDao extends BaseDao<ReissueTimePeriodPo, Long> {

    /**
     * 查询平台上次未结束补发时间段
     *
     * @param clientPlatformId 平台ID
     * @return 补发时间段
     */
    List<ReissueTimePeriodPo> selectLastNotEndReissueTimePeriod(Long clientPlatformId);

    /**
     * 查询平台上次待补发时间段
     *
     * @param clientPlatformId ID
     * @return 补发时间段
     */
    List<ReissueTimePeriodPo> selectLastReadyReissueTimePeriod(Long clientPlatformId);

}
