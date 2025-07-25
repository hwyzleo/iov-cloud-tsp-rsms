package net.hwyz.iov.cloud.tsp.rsms.api.contract.datainfo;

import cn.hutool.core.util.ArrayUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessageDataInfo;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbDataInfoType;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbEngineState;
import net.hwyz.iov.cloud.tsp.rsms.api.util.GbUtil;

import java.util.Arrays;

/**
 * 国标发动机数据信息
 *
 * @author hwyz_leo
 */
@Data
@Slf4j
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GbEngineDataInfo extends GbMessageDataInfo {

    /**
     * 发动机状态
     */
    private GbEngineState state;
    /**
     * 曲轴转速
     * 有效范围：0~60000（表示0r/min60000r/min),最小计量单元：1r/min,“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private int crankshaftSpeed;
    /**
     * 燃料消耗率
     * 有效值范围：0~60000（表示0L/100km~600L/100km),最小计量单元：0.01L/100km,“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private int consumptionRate;

    @Override
    public int parse(byte[] dataInfoBytes) {
        if (dataInfoBytes == null || dataInfoBytes.length == 0) {
            return 0;
        }
        this.dataInfoType = GbDataInfoType.ENGINE;
        this.state = GbEngineState.valOf(dataInfoBytes[0]);
        this.crankshaftSpeed = GbUtil.bytesToWord(Arrays.copyOfRange(dataInfoBytes, 1, 3));
        this.consumptionRate = GbUtil.bytesToWord(Arrays.copyOfRange(dataInfoBytes, 3, 5));
        return 5;
    }

    @Override
    public byte[] toByteArray() {
        return ArrayUtil.addAll(new byte[]{this.dataInfoType.getCode()}, new byte[]{this.state.getCode()},
                GbUtil.wordToBytes(this.crankshaftSpeed), GbUtil.wordToBytes(this.consumptionRate));
    }
}
