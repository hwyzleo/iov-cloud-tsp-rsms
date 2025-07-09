package net.hwyz.iov.cloud.tsp.rsms.api.contract;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * 管理后台国标发动机数据信息
 *
 * @author hwyz_leo
 */
@Data
@Slf4j
@NoArgsConstructor
public class GbEngineMpt {

    /**
     * 发动机状态
     */
    private String state;
    /**
     * 曲轴转速
     * 有效范围：0~60000（表示0r/min60000r/min),最小计量单元：1r/min,“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private Integer crankshaftSpeed;
    /**
     * 燃料消耗率
     * 有效值范围：0~60000（表示0L/100km~600L/100km),最小计量单元：0.01L/100km,“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private BigDecimal consumptionRate;

}
