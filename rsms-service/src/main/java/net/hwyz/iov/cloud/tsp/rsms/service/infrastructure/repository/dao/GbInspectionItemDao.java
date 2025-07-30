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

}
