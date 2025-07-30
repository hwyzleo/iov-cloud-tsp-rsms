package net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao;

import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.GbInspectionReportPo;
import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 国标数据质量检测报告 DAO
 * </p>
 *
 * @author hwyz_leo
 * @since 2025-07-28
 */
@Mapper
public interface GbInspectionReportDao extends BaseDao<GbInspectionReportPo, Long> {

    /**
     * 批量物理删除
     *
     * @param ids id数组
     * @return 影响行数
     */
    int batchPhysicalDeletePo(Long[] ids);

}
