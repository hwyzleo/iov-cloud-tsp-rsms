package net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.handler;

import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessage;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.AbstractChecker;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.CheckItem;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.InspectionHandler;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.GbInspectionItemPo;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.GbInspectionReportPo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 数据汇总检测处理器
 *
 * @author hwyz_leo
 */
public class SummaryInspectionHandler extends BaseInspectionHandler implements InspectionHandler {

    @Override
    public void inspect(GbInspectionReportPo report, List<GbMessage> gbMessages) {
        report.setDataStartTime(gbMessages.get(0).getMessageTime());
        report.setDataEndTime(gbMessages.get(gbMessages.size() - 1).getMessageTime());
        long vehicleCount = 0;
        long vehicleErrorCount = 0;
        long dataCount = 0;
        long dataErrorCount = 0;
        for (GbInspectionItemPo item : report.getItems()) {
            if (item.getTotalVehicleCount() > vehicleCount) {
                vehicleCount = item.getTotalVehicleCount();
            }
            if (item.getErrorVehicleCount() > vehicleErrorCount) {
                vehicleErrorCount = item.getErrorVehicleCount();
            }
            dataCount += item.getTotalDataCount();
            dataErrorCount += item.getErrorDataCount();
        }
        report.setVehicleCount(vehicleCount);
        report.setVehicleErrorCount(vehicleErrorCount);
        report.setMessageCount((long) gbMessages.size());
        report.setMessageErrorCount((long) report.getErrorMessages().size());
        report.setDataCount(dataCount);
        report.setDataErrorCount(dataErrorCount);
    }

    @Override
    public long validate(Date messageTime, int value, CheckItem item, Integer sn, Map<String, AbstractChecker> vehicleCheckers) {
        return 0;
    }
}
