package net.hwyz.iov.cloud.tsp.rsms.api.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.VehicleReportState;

/**
 * 对外服务已注册车辆上报状态
 *
 * @author hwyz_leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredVehicleReportStateExService {

    /**
     * 车架号
     */
    private String vin;

    /**
     * 上报状态
     */
    private VehicleReportState reportState;

}
