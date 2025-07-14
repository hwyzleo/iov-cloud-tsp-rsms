package net.hwyz.iov.cloud.tsp.rsms.service.facade.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.RegisteredVehicleReportStateExService;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.RegisteredVehicleAppService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 已注册车辆相关服务接口实现类
 *
 * @author hwyz_leo
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/service/registeredVehicle")
public class RegisteredVehicleServiceController {

    private final RegisteredVehicleAppService registeredVehicleAppService;

    /**
     * 改变车辆上报状态
     *
     * @param vin                车架号
     * @param vehicleReportState 车辆上报状态
     */
    @PostMapping("/{vin}/action/changeReportState")
    public void changeReportState(@PathVariable String vin, @RequestBody @Validated RegisteredVehicleReportStateExService vehicleReportState) {
        logger.info("改变车辆[{}]上报状态[{}]", vin, vehicleReportState.getReportState());
        registeredVehicleAppService.changeReportState(vin, vehicleReportState.getReportState());
    }

}
