package net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.handler;

import net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.AbstractChecker;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.CheckItem;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.InspectionHandler;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * 数据规范性检测处理器
 * 异常值和无效值以及连续性
 *
 * @author hwyz_leo
 */
@Component
public class StandardInspectionHandler extends BaseInspectionHandler implements InspectionHandler {

    @Override
    public int validate(Date messageTime, int value, CheckItem item, Integer sn, Map<String, AbstractChecker> vehicleCheckers) {
        int errorCount = 0;
        errorCount += getVehicleChecker(item, sn, TYPE_ABNORMAL, vehicleCheckers).check(value);
        errorCount += getVehicleChecker(item, sn, TYPE_ABNORMAL_CONTINUOUS, vehicleCheckers).check(value, messageTime);
        errorCount += getVehicleChecker(item, sn, TYPE_INVALID, vehicleCheckers).check(value);
        errorCount += getVehicleChecker(item, sn, TYPE_INVALID_CONTINUOUS, vehicleCheckers).check(value, messageTime);
        return errorCount;
    }

}
