package net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao;

import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.GbInspectionItemPo;
import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 国标数据质量检测项 DAO
 * </p>
 *
 * @author hwyz_leo
 * @since 2025-07-28
 */
@Mapper
public interface GbInspectionItemDao extends BaseDao<GbInspectionItemPo, Long> {

    /**
     * 根据检测报告ID批量删除
     *
     * @param reportId 检测报告ID
     * @return 删除数量
     */
    int batchPhysicalDeletePoByReportId(Long reportId);

}
