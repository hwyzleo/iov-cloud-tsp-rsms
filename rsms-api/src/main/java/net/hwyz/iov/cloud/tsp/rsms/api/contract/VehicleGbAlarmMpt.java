package net.hwyz.iov.cloud.tsp.rsms.api.contract;

import lombok.*;
import net.hwyz.iov.cloud.framework.common.web.domain.BaseRequest;

import java.util.Date;

/**
 * 管理后台车辆国标报警
 *
 * @author hwyz_leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VehicleGbAlarmMpt extends BaseRequest {

    /**
     * 主键
     */
    private Long id;

    /**
     * 车架号
     */
    private String vin;

    /**
     * 报警时间
     */
    private Date alarmTime;

    /**
     * 恢复时间
     */
    private Date restorationTime;

    /**
     * 报警标志位
     */
    private Integer alarmFlag;

    /**
     * 报警级别
     */
    private Integer alarmLevel;

    /**
     * 消息数据
     */
    private String messageData;

    /**
     * 创建时间
     */
    private Date createTime;

}
