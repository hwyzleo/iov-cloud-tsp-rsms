package net.hwyz.iov.cloud.tsp.rsms.api.contract.datainfo;

import cn.hutool.core.util.ArrayUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessageDataInfo;
import net.hwyz.iov.cloud.tsp.rsms.api.util.GbUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 国标单个可充电储能子系统电压数据信息
 *
 * @author hwyz_leo
 */
@Data
@Slf4j
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GbSingleBatteryVoltageDataInfo extends GbMessageDataInfo {

    /**
     * 可充电储能子系统号
     * 有效值范围:1~250
     */
    private byte sn;
    /**
     * 可充电储能装置电压
     * 有效值范围：0~10000（表示0V~1000V),最小计量单元：0.1V,“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private int voltage;
    /**
     * 可充电储能装置电流
     * 有效值范围：0 ~ 20000（数值偏移量1000A,表示-1000A ~ +1000A),最小计量单元：0.1A,
     * “0xFF,0xFE”表示异常，“0xFF,0xFF#表示无效
     */
    private int current;
    /**
     * 单体电池总数
     * N个电池单体，有效值范圃：1 ~ 65531，“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private int cellCount;
    /**
     * 本帧起始电池序号
     * 当本帧单体个数超过200时，应标分成多帧数据进行传输，有效值范围：1~65531
     */
    private int frameStartCellSn;
    /**
     * 本帧单体电池总数
     * 本帧单体总数m;有效值范围，1~200
     */
    private byte frameCellCount;
    /**
     * 单体电池电压列表
     * 有效值范围：0~60000（表示0V~60.000V),最小计量单元：0.001V,单体电池电压个数等于本帧单体电池总数m,
     * “0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
     */
    private List<Integer> cellVoltageList;

    @Override
    public int parse(byte[] dataInfoBytes) {
        if (dataInfoBytes == null || dataInfoBytes.length == 0) {
            return 0;
        }
        this.sn = dataInfoBytes[0];
        this.voltage = GbUtil.bytesToWord(Arrays.copyOfRange(dataInfoBytes, 1, 3));
        this.current = GbUtil.bytesToWord(Arrays.copyOfRange(dataInfoBytes, 3, 5));
        this.cellCount = GbUtil.bytesToWord(Arrays.copyOfRange(dataInfoBytes, 5, 7));
        this.frameStartCellSn = GbUtil.bytesToWord(Arrays.copyOfRange(dataInfoBytes, 7, 9));
        this.frameCellCount = dataInfoBytes[9];
        this.cellVoltageList = new ArrayList<>();
        int startPos = 10;
        for (int i = 0; i < frameCellCount; i++) {
            cellVoltageList.add(GbUtil.bytesToWord(Arrays.copyOfRange(dataInfoBytes, startPos, startPos + 2)));
            startPos += 2;
        }
        return 10 + frameCellCount * 2;
    }

    @Override
    public byte[] toByteArray() {
        byte[] bytes = ArrayUtil.addAll(new byte[]{this.sn}, GbUtil.wordToBytes(this.voltage), GbUtil.wordToBytes(this.current),
                GbUtil.wordToBytes(this.cellCount), GbUtil.wordToBytes(this.frameStartCellSn), new byte[]{this.frameCellCount});
        for (Integer cellVoltage : cellVoltageList) {
            bytes = ArrayUtil.addAll(bytes, GbUtil.wordToBytes(cellVoltage));
        }
        return bytes;
    }
}
