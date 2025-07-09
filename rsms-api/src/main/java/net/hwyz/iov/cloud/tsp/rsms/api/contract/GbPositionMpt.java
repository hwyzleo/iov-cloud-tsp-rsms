package net.hwyz.iov.cloud.tsp.rsms.api.contract;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * 管理后台国标车辆位置数据信息
 *
 * @author hwyz_leo
 */
@Data
@Slf4j
@NoArgsConstructor
public class GbPositionMpt {

    /**
     * 定位是否有效
     * true：有效定位
     * false：无效定位（当数据通信正常，而不能获取定位信息时，发送最后一次有效定位信息，并将定位状态置位无效）
     */
    private Boolean positionValid;
    /**
     * 纬度是否南纬
     * true：南纬
     * false：北纬
     */
    private Boolean southLatitude;
    /**
     * 经度是否西经
     * true：西经
     * false：东经
     */
    private Boolean westLongitude;
    /**
     * 经度
     * 以度为单位的纬度值乘以10°，精确到百万分之一度
     */
    private BigDecimal longitude;
    /**
     * 纬度
     * 以度为单位的纬度值乘以10°，精确到百万分之一度
     */
    private BigDecimal latitude;

}
