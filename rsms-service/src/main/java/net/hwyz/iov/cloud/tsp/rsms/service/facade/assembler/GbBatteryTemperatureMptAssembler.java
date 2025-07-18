package net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler;

import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbBatteryTemperatureMpt;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.datainfo.GbSingleBatteryTemperatureDataInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理后台国标可充电储能子系统温度数据转换类
 *
 * @author hwyz_leo
 */
@Mapper
public interface GbBatteryTemperatureMptAssembler {

    GbBatteryTemperatureMptAssembler INSTANCE = Mappers.getMapper(GbBatteryTemperatureMptAssembler.class);

    /**
     * 国标数据信息转数据传输对象
     *
     * @param gbBatteryTemperatureDataInfo 数据对象
     * @return 数据传输对象
     */
    @Mappings({
            @Mapping(target = "sn", source = "sn"),
            @Mapping(target = "probeCount", source = "probeCount"),
            @Mapping(target = "temperatures", expression = "java(net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler.GbBatteryTemperatureMptAssembler.bytesToIntList(gbBatteryTemperatureDataInfo.getTemperatures()))")
    })
    GbBatteryTemperatureMpt fromDataInfo(GbSingleBatteryTemperatureDataInfo gbBatteryTemperatureDataInfo);

    /**
     * 国标数据信息列表转数据传输对象列表
     *
     * @param gbBatteryTemperatureDataInfoList 数据对象列表
     * @return 数据传输对象列表
     */
    List<GbBatteryTemperatureMpt> fromDataInfoList(List<GbSingleBatteryTemperatureDataInfo> gbBatteryTemperatureDataInfoList);

    static List<Integer> bytesToIntList(byte[] bytes) {
        List<Integer> intList = new ArrayList<>();
        for (byte aByte : bytes) {
            // 温度有40偏移量
            intList.add((int) aByte - 40);
        }
        return intList;
    }

}
