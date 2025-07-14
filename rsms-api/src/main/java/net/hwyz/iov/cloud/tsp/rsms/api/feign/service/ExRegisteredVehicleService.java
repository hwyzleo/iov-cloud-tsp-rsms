package net.hwyz.iov.cloud.tsp.rsms.api.feign.service;

import net.hwyz.iov.cloud.framework.common.constant.ServiceNameConstants;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.RegisteredVehicleReportStateExService;
import net.hwyz.iov.cloud.tsp.rsms.api.feign.service.factory.ExRegisteredVehicleServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 已注册车辆相关服务接口
 *
 * @author hwyz_leo
 */
@FeignClient(contextId = "exRegisteredVehicleService", value = ServiceNameConstants.TSP_RSMS, path = "/service/registeredVehicle", fallbackFactory = ExRegisteredVehicleServiceFallbackFactory.class)
public interface ExRegisteredVehicleService {

    /**
     * 改变车辆上报状态
     *
     * @param vin                车架号
     * @param vehicleReportState 车辆上报状态
     */
    @PostMapping("/{vin}/action/changeReportState")
    void changeReportState(@PathVariable String vin, @RequestBody @Validated RegisteredVehicleReportStateExService vehicleReportState);

}
