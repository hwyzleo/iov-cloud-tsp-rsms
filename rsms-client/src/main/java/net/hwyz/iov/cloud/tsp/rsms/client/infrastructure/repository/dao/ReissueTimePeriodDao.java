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
     * 查询平台上次初始化补发时间段
     *
     * @param clientPlatformId 平台ID
     * @return 补发时间段
     */
    List<ReissueTimePeriodPo> selectLastInitReissueTimePeriod(Long clientPlatformId);

}
