package net.hwyz.iov.cloud.tsp.rsms.api.feign.service.factory;

import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.RegisteredVehicleReportStateExService;
import net.hwyz.iov.cloud.tsp.rsms.api.feign.service.ExRegisteredVehicleService;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 已注册车辆相关服务降级处理
 *
 * @author hwyz_leo
 */
@Slf4j
@Component
public class ExRegisteredVehicleServiceFallbackFactory implements FallbackFactory<ExRegisteredVehicleService> {

    @Override
    public ExRegisteredVehicleService create(Throwable throwable) {
        return new ExRegisteredVehicleService() {
            @Override
            public void changeReportState(String vin, RegisteredVehicleReportStateExService vehicleReportState) {
                logger.error("已注册车辆服务改变车辆[{}]上报状态[{}]调用失败", vin, vehicleReportState.getReportState(), throwable);
            }
        };
    }
}
