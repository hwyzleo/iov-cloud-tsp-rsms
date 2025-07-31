package net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.handler;

import net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.AbstractChecker;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.CheckItem;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.InspectionHandler;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * 数据完整性检测处理器
 * 整列缺失，短时行缺失，长时行缺失
 *
 * @author hwyz_leo
 */
@Component
public class IntegrityInspectionHandler extends BaseInspectionHandler implements InspectionHandler {

    @Override
    public long validate(Date messageTime, int value, CheckItem item, Integer sn, Map<String, AbstractChecker> vehicleCheckers) {
        long errorCount = 0;
        errorCount += getVehicleChecker(item, sn, TYPE_NULL, vehicleCheckers).check(value);
        errorCount += getVehicleChecker(item, sn, TYPE_NULL_CONTINUOUS, vehicleCheckers).check(value);
        return errorCount;
    }

}
