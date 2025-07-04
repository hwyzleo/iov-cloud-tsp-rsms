package net.hwyz.iov.cloud.tsp.rsms.api.contract.datainfo;

import cn.hutool.core.util.ArrayUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessageDataInfo;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbDataInfoType;
import net.hwyz.iov.cloud.tsp.rsms.api.util.GbUtil;

import java.util.Arrays;

/**
 * 国标车辆位置数据信息
 *
 * @author hwyz_leo
 */
@Data
@Slf4j
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GbPositionDataInfo extends GbMessageDataInfo {

    /**
     * 数据信息类型
     */
    private GbDataInfoType dataInfoType;
    /**
     * 定位状态
     * 位0：0：有效定位；1：无效定位（当数据通信正常，而不能获取定位信息时，发送最后一次有效定位信息，并将定位状态置位无效）
     * 位1：0：北纬；1：南纬
     * 位2：0：东经；1：西经
     * 位3~7：保留
     */
    private byte state;
    /**
     * 经度
     * 以度为单位的纬度值乘以10°，精确到百万分之一度
     */
    private int longitude;
    /**
     * 纬度
     * 以度为单位的纬度值乘以10°，精确到百万分之一度
     */
    private int latitude;

    @Override
    public int parse(byte[] dataInfoBytes) {
        if (dataInfoBytes == null || dataInfoBytes.length == 0) {
            return 0;
        }
        this.dataInfoType = GbDataInfoType.POSITION;
        this.state = dataInfoBytes[0];
        this.longitude = GbUtil.bytesToDword(Arrays.copyOfRange(dataInfoBytes, 1, 5));
        this.latitude = GbUtil.bytesToDword(Arrays.copyOfRange(dataInfoBytes, 5, 9));
        return 9;
    }

    @Override
    public byte[] toByteArray() {
        return ArrayUtil.addAll(new byte[]{this.dataInfoType.getCode()}, new byte[]{this.state},
                GbUtil.dwordToBytes(this.longitude), GbUtil.dwordToBytes(this.latitude));
    }
}
