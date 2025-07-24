package net.hwyz.iov.cloud.tsp.rsms.api.feign.service;

import net.hwyz.iov.cloud.framework.common.constant.ServiceNameConstants;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.ReportVehicleReportStateExService;
import net.hwyz.iov.cloud.tsp.rsms.api.feign.service.factory.ExRegisteredVehicleServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 上报车辆相关服务接口
 *
 * @author hwyz_leo
 */
@FeignClient(contextId = "exReportVehicleService", value = ServiceNameConstants.TSP_RSMS, path = "/service/reportVehicle", fallbackFactory = ExRegisteredVehicleServiceFallbackFactory.class)
public interface ExReportVehicleService {

    /**
     * 改变车辆上报状态
     *
     * @param vin                车架号
     * @param vehicleReportState 车辆上报状态
     */
    @PostMapping("/{vin}/action/changeReportState")
    void changeReportState(@PathVariable String vin, @RequestBody @Validated ReportVehicleReportStateExService vehicleReportState);

}
