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
        this.positionValid = GbUtil.isPositionValid(dataInfoBytes[0]);
        this.southLatitude = GbUtil.isSouthLatitude(dataInfoBytes[0]);
        this.westLongitude = GbUtil.isWestLongitude(dataInfoBytes[0]);
        this.longitude = GbUtil.bytesToDword(Arrays.copyOfRange(dataInfoBytes, 1, 5));
        this.latitude = GbUtil.bytesToDword(Arrays.copyOfRange(dataInfoBytes, 5, 9));
        return 9;
    }

    @Override
    public byte[] toByteArray() {
        return ArrayUtil.addAll(new byte[]{this.dataInfoType.getCode()},
                new byte[]{GbUtil.combinePositionByte(this.positionValid, this.southLatitude, this.westLongitude)},
                GbUtil.dwordToBytes(this.longitude), GbUtil.dwordToBytes(this.latitude));
    }
}
