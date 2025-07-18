package net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler;

import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbFuelCellMpt;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.datainfo.GbFuelCellDataInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理后台国标燃料电池数据转换类
 *
 * @author hwyz_leo
 */
@Mapper
public interface GbFuelCellMptAssembler {

    GbFuelCellMptAssembler INSTANCE = Mappers.getMapper(GbFuelCellMptAssembler.class);

    /**
     * 国标数据信息转数据传输对象
     *
     * @param gbFuelCellDataInfo 数据对象
     * @return 数据传输对象
     */
    @Mappings({
            @Mapping(target = "voltage", expression = "java(java.math.BigDecimal.valueOf(gbFuelCellDataInfo.getVoltage()).divide(java.math.BigDecimal.TEN))"),
            @Mapping(target = "current", expression = "java(java.math.BigDecimal.valueOf(gbFuelCellDataInfo.getCurrent()).divide(java.math.BigDecimal.TEN))"),
            @Mapping(target = "consumptionRate", expression = "java(java.math.BigDecimal.valueOf(gbFuelCellDataInfo.getConsumptionRate()).divide(java.math.BigDecimal.TEN).divide(java.math.BigDecimal.TEN))"),
            @Mapping(target = "temperatureProbeCount", source = "temperatureProbeCount"),
            @Mapping(target = "probeTemperature", expression = "java(net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler.GbFuelCellMptAssembler.bytesToIntList(gbFuelCellDataInfo.getProbeTemperature()))"),
            @Mapping(target = "hydrogenSystemMaxTemperature", expression = "java(java.math.BigDecimal.valueOf(gbFuelCellDataInfo.getHydrogenSystemMaxTemperature()-400).divide(java.math.BigDecimal.TEN))"),
            @Mapping(target = "hydrogenSystemMaxTemperatureProbe", source = "hydrogenSystemMaxTemperatureProbe"),
            @Mapping(target = "hydrogenMaxConcentration", source = "hydrogenMaxConcentration"),
            @Mapping(target = "hydrogenMaxConcentrationSensor", source = "hydrogenMaxConcentrationSensor"),
            @Mapping(target = "hydrogenMaxPressure", expression = "java(java.math.BigDecimal.valueOf(gbFuelCellDataInfo.getHydrogenMaxPressure()).divide(java.math.BigDecimal.TEN))"),
            @Mapping(target = "hydrogenMaxPressureSensor", source = "hydrogenMaxPressureSensor"),
            @Mapping(target = "highPressureDcdcState", source = "highPressureDcdcState.name")
    })
    GbFuelCellMpt fromDataInfo(GbFuelCellDataInfo gbFuelCellDataInfo);

    static List<Integer> bytesToIntList(byte[] bytes) {
        List<Integer> intList = new ArrayList<>();
        for (byte aByte : bytes) {
            // 温度有40偏移量
            intList.add((int) aByte - 40);
        }
        return intList;
    }

}
