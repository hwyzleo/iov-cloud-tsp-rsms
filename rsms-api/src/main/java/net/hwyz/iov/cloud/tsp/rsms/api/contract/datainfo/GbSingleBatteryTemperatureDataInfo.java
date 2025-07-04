package net.hwyz.iov.cloud.tsp.rsms.api.contract.datainfo;

import cn.hutool.core.util.ArrayUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessageDataInfo;
import net.hwyz.iov.cloud.tsp.rsms.api.util.GbUtil;

import java.util.Arrays;

/**
 * 国标单个可充电储能子系统温度数据信息
 *
 * @author hwyz_leo
 */
@Data
@Slf4j
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GbSingleBatteryTemperatureDataInfo extends GbMessageDataInfo {

    /**
     * 可充电储能子系统号
     * 有效值范围:1~250
     */
    private byte sn;
    /**
     * 可充电储能温度探针个数
     * N个温度探针，有效值范围：1~65531，“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private int probeCount;
    /**
     * 可充电储能子系统各温度探针检测到的温度值
     * 有效值范围：0 ~ 250（数值偏移量40℃，表示-40℃ ~ +210℃)，最小计量单元：1℃，“0xFE”表示异常，“0xFF”表示无效
     */
    private byte[] temperatures;

    @Override
    public int parse(byte[] dataInfoBytes) {
        if (dataInfoBytes == null || dataInfoBytes.length == 0) {
            return 0;
        }
        this.sn = dataInfoBytes[0];
        this.probeCount = GbUtil.bytesToWord(Arrays.copyOfRange(dataInfoBytes, 1, 3));
        this.temperatures = Arrays.copyOfRange(dataInfoBytes, 3, 3 + probeCount);
        return 3 + probeCount;
    }

    @Override
    public byte[] toByteArray() {
        return ArrayUtil.addAll(new byte[]{this.sn}, GbUtil.wordToBytes(this.probeCount), this.temperatures);
    }
}
