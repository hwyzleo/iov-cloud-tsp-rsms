package net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.handler;

import net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.AbstractChecker;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.CheckItem;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.InspectionHandler;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * 数据准确性检测处理器
 * 非协议值
 *
 * @author hwyz_leo
 */
@Component
public class AccuracyInspectionHandler extends BaseInspectionHandler implements InspectionHandler {

    @Override
    public int validate(Date messageTime, int value, CheckItem item, Integer sn, Map<String, AbstractChecker> vehicleCheckers) {
        int errorCount = 0;
        errorCount += getVehicleChecker(item, sn, TYPE_RANGE, vehicleCheckers).check(value);
        errorCount += getVehicleChecker(item, sn, TYPE_RANGE_CONTINUOUS, vehicleCheckers).check(value, messageTime);
        return errorCount;
    }
}
