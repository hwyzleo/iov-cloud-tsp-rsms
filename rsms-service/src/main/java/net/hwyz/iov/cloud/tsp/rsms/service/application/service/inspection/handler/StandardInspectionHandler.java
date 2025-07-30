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

    /**
     * 验证检查项相关类型
     *
     * @param messageTime     消息时间
     * @param value           检查项值
     * @param item            检查项
     * @param sn              检查项序号
     * @param vehicleCheckers 车辆检查器
     */
    @Override
    public void validate(Date messageTime, int value, CheckItem item, Integer sn, Map<String, AbstractChecker> vehicleCheckers) {
        getVehicleChecker(item, sn, TYPE_ABNORMAL, vehicleCheckers).check(value);
        getVehicleChecker(item, sn, TYPE_ABNORMAL_CONTINUOUS, vehicleCheckers).check(value, messageTime);
        getVehicleChecker(item, sn, TYPE_INVALID, vehicleCheckers).check(value);
        getVehicleChecker(item, sn, TYPE_INVALID_CONTINUOUS, vehicleCheckers).check(value, messageTime);
    }

}
