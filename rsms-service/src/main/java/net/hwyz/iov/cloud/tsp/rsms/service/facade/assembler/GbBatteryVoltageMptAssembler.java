package net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler;

import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbBatteryVoltageMpt;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.datainfo.GbSingleBatteryVoltageDataInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

/**
 * 管理后台国标可充电储能子系统电压数据转换类
 *
 * @author hwyz_leo
 */
@Mapper
public interface GbBatteryVoltageMptAssembler {

    GbBatteryVoltageMptAssembler INSTANCE = Mappers.getMapper(GbBatteryVoltageMptAssembler.class);

    /**
     * 国标数据信息转数据传输对象
     *
     * @param gbBatteryVoltageDataInfo 数据对象
     * @return 数据传输对象
     */
    @Mappings({
            @Mapping(target = "sn", source = "sn"),
            @Mapping(target = "voltage", expression = "java(java.math.BigDecimal.valueOf(gbBatteryVoltageDataInfo.getVoltage()).divide(java.math.BigDecimal.TEN))"),
            @Mapping(target = "current", expression = "java(java.math.BigDecimal.valueOf(gbBatteryVoltageDataInfo.getCurrent()-10000).divide(java.math.BigDecimal.TEN))"),
            @Mapping(target = "cellCount", source = "cellCount"),
            @Mapping(target = "frameStartCellSn", source = "frameStartCellSn"),
            @Mapping(target = "frameCellCount", source = "frameCellCount"),
            @Mapping(target = "cellVoltageList", expression = "java(net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler.GbBatteryVoltageMptAssembler.intListToBigDecimal(gbBatteryVoltageDataInfo.getCellVoltageList()))")
    })
    GbBatteryVoltageMpt fromDataInfo(GbSingleBatteryVoltageDataInfo gbBatteryVoltageDataInfo);

    /**
     * 国标数据信息列表转数据传输对象列表
     *
     * @param gbBatteryVoltageDataInfoList 数据对象列表
     * @return 数据传输对象列表
     */
    List<GbBatteryVoltageMpt> fromDataInfoList(List<GbSingleBatteryVoltageDataInfo> gbBatteryVoltageDataInfoList);

    static List<BigDecimal> intListToBigDecimal(List<Integer> list) {
        return list.stream().map(i -> BigDecimal.valueOf(i).divide(BigDecimal.valueOf(1000))).toList();
    }

}
