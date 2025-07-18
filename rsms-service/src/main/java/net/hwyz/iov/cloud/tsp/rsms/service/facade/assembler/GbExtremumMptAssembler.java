package net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler;

import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbExtremumMpt;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.datainfo.GbExtremumDataInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 管理后台国标极值数据转换类
 *
 * @author hwyz_leo
 */
@Mapper
public interface GbExtremumMptAssembler {

    GbExtremumMptAssembler INSTANCE = Mappers.getMapper(GbExtremumMptAssembler.class);

    /**
     * 国标数据信息转数据传输对象
     *
     * @param gbExtremumDataInfo 数据对象
     * @return 数据传输对象
     */
    @Mappings({
            @Mapping(target = "maxVoltageBatteryDeviceNo", source = "maxVoltageBatteryDeviceNo"),
            @Mapping(target = "maxVoltageCellNo", source = "maxVoltageCellNo"),
            @Mapping(target = "cellMaxVoltage", expression = "java(java.math.BigDecimal.valueOf(gbExtremumDataInfo.getCellMaxVoltage()).divide(java.math.BigDecimal.valueOf(1000)))"),
            @Mapping(target = "minVoltageBatteryDeviceNo", source = "minVoltageBatteryDeviceNo"),
            @Mapping(target = "minVoltageCellNo", source = "minVoltageCellNo"),
            @Mapping(target = "cellMinVoltage", expression = "java(java.math.BigDecimal.valueOf(gbExtremumDataInfo.getCellMinVoltage()).divide(java.math.BigDecimal.valueOf(1000)))"),
            @Mapping(target = "maxTemperatureDeviceNo", source = "maxTemperatureDeviceNo"),
            @Mapping(target = "maxTemperatureProbeNo", source = "maxTemperatureProbeNo"),
            @Mapping(target = "maxTemperature", expression = "java(gbExtremumDataInfo.getMaxTemperature()-40)"),
            @Mapping(target = "minTemperatureDeviceNo", source = "minTemperatureDeviceNo"),
            @Mapping(target = "minTemperatureProbeNo", source = "minTemperatureProbeNo"),
            @Mapping(target = "minTemperature", expression = "java(gbExtremumDataInfo.getMinTemperature()-40)")
    })
    GbExtremumMpt fromDataInfo(GbExtremumDataInfo gbExtremumDataInfo);

}
