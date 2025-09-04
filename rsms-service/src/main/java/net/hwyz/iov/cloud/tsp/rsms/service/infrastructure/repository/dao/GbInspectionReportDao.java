package net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao;

import net.hwyz.iov.cloud.framework.mysql.dao.BaseDao;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.GbInspectionReportPo;
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

}
