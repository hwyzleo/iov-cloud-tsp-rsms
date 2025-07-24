package net.hwyz.iov.cloud.tsp.rsms.api.feign.service.factory;

import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.ReportVehicleReportStateExService;
import net.hwyz.iov.cloud.tsp.rsms.api.feign.service.ExReportVehicleService;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 已注册车辆相关服务降级处理
 *
 * @author hwyz_leo
 */
@Slf4j
@Component
public class ExRegisteredVehicleServiceFallbackFactory implements FallbackFactory<ExReportVehicleService> {

    @Override
    public ExReportVehicleService create(Throwable throwable) {
        return new ExReportVehicleService() {
            @Override
            public void changeReportState(String vin, ReportVehicleReportStateExService vehicleReportState) {
                logger.error("上报车辆服务改变车辆[{}]上报状态[{}]调用失败", vin, vehicleReportState.getReportState(), throwable);
            }
        };
    }
}
