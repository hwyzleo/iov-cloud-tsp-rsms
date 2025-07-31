package net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.handler;

import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessage;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.AbstractChecker;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.CheckItem;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.InspectionHandler;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.GbInspectionReportPo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.CheckItem.MESSAGE_TIME;

/**
 * 数据时效性检测处理器
 * 时序性: 时间需满足先后顺序
 * 时间唯一性: 不应有重复时间帧
 * 时间连续性: 连续状态内上传时间是否丢帧
 *
 * @author hwyz_leo
 */
public class TimelinessInspectionHandler extends BaseInspectionHandler implements InspectionHandler {

    @Override
    public void inspect(GbInspectionReportPo report, List<GbMessage> gbMessages) {
        Map<String, Map<String, AbstractChecker>> checkers = new HashMap<>();
        gbMessages.forEach(message -> {
            String vin = message.getVin();
            Date messageTime = message.getMessageTime();
            Map<String, AbstractChecker> vehicleCheckers = getVehicleCheckers(vin, checkers);
            int errorCount = 0;
            switch (message.getHeader().getCommandFlag()) {
                case REALTIME_REPORT, REISSUE_REPORT ->
                        errorCount += getVehicleChecker(MESSAGE_TIME, null, TYPE_DUPLICATE, vehicleCheckers).check(messageTime);
            }
            if (errorCount > 0) {
                report.getErrorMessages().add(message);
            }
        });
        summarize(report, checkers);
    }

    @Override
    public int validate(Date messageTime, int value, CheckItem item, Integer sn, Map<String, AbstractChecker> vehicleCheckers) {
        return 0;
    }
}
