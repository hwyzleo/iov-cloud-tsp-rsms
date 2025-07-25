package net.hwyz.iov.cloud.tsp.rsms.api.contract;

import lombok.*;
import net.hwyz.iov.cloud.framework.common.web.domain.BaseRequest;

import java.util.Date;

/**
 * 管理后台已注册车辆
 *
 * @author hwyz_leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RegisteredVehicleMpt extends BaseRequest {

    /**
     * 主键
     */
    private Long id;

    /**
     * 客户端平台ID
     */
    private Long clientPlatformId;

    /**
     * 车架号
     */
    private String vin;

    /**
     * 创建时间
     */
    private Date createTime;

}
