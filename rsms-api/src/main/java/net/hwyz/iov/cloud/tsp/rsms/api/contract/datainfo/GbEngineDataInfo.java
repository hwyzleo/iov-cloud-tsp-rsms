package net.hwyz.iov.cloud.tsp.rsms.api.contract.datainfo;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessageDataInfo;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbDataInfoType;
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
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GbEngineDataInfo extends GbMessageDataInfo {

    /**
     * 数据信息类型
     */
    private GbDataInfoType dataInfoType;
    /**
     * 发动机状态
     * 0x01:启动状态：0x02:关闭状态，“0xFE”表示异常，“0xFF”表示无效
     */
    private byte state;
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
    public int getLength() {
        return 5;
    }

    @Override
    public void parse(byte[] dataInfoBytes) {
        if (dataInfoBytes == null || dataInfoBytes.length != getLength()) {
            logger.warn("国标发动机数据信息[{}]异常", Arrays.toString(dataInfoBytes));
            return;
        }
        this.dataInfoType = GbDataInfoType.ENGINE;
        this.state = dataInfoBytes[0];
        this.crankshaftSpeed = GbUtil.bytesToWord(Arrays.copyOfRange(dataInfoBytes, 1, 3));
        this.consumptionRate = GbUtil.bytesToWord(Arrays.copyOfRange(dataInfoBytes, 3, 5));
    }

    @Override
    public byte[] toByteArray() {
        return ArrayUtil.addAll(new byte[]{this.dataInfoType.getCode()}, new byte[]{this.state},
                GbUtil.wordToBytes(this.crankshaftSpeed), GbUtil.wordToBytes(this.consumptionRate));
    }
}
