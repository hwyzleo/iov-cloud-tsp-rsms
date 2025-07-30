package net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection;

import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessage;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.GbInspectionReportPo;

import java.util.List;

/**
 * 国标数据质量检测处理器接口
 *
 * @author hwyz_leo
 */
public interface InspectionHandler {

    /**
     * 数据质量检测
     *
     * @param report     国标检测报告
     * @param gbMessages 国标数据列表
     */
    void inspect(GbInspectionReportPo report, List<GbMessage> gbMessages);

}
