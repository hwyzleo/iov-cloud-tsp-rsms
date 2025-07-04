package net.hwyz.iov.cloud.tsp.rsms.api.contract;

import lombok.*;
import net.hwyz.iov.cloud.framework.common.web.domain.BaseRequest;

import java.util.Date;

/**
 * 管理后台车辆国标消息
 *
 * @author hwyz_leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VehicleGbMessageMpt extends BaseRequest {

    /**
     * 主键
     */
    private Long id;

    /**
     * 车架号
     */
    private String vin;

    /**
     * 解析时间
     */
    private Date parseTime;

    /**
     * 消息时间
     */
    private Date messageTime;

    /**
     * 命令标识
     */
    private String commandFlag;

    /**
     * 消息数据
     */
    private String messageData;

    /**
     * 创建时间
     */
    private Date createTime;

}
