package net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler;

import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbVehicleDataMpt;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.datainfo.GbVehicleDataDataInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 管理后台国标整车数据转换类
 *
 * @author hwyz_leo
 */
@Mapper
public interface GbVehicleDataMptAssembler {

    GbVehicleDataMptAssembler INSTANCE = Mappers.getMapper(GbVehicleDataMptAssembler.class);

    /**
     * 国标数据信息转数据传输对象
     *
     * @param gbVehicleDataDataInfo 数据对象
     * @return 数据传输对象
     */
    @Mappings({
            @Mapping(target = "vehicleState", source = "vehicleState.name"),
            @Mapping(target = "chargingState", source = "chargingState.name"),
            @Mapping(target = "runningMode", source = "runningMode.name"),
            @Mapping(target = "speed", expression = "java(java.math.BigDecimal.valueOf(gbVehicleDataDataInfo.getSpeed()).divide(java.math.BigDecimal.TEN))"),
            @Mapping(target = "totalOdometer", expression = "java(java.math.BigDecimal.valueOf(gbVehicleDataDataInfo.getTotalOdometer()).divide(java.math.BigDecimal.TEN))"),
            @Mapping(target = "totalVoltage", expression = "java(java.math.BigDecimal.valueOf(gbVehicleDataDataInfo.getTotalVoltage()).divide(java.math.BigDecimal.TEN))"),
            @Mapping(target = "totalCurrent", expression = "java(java.math.BigDecimal.valueOf(gbVehicleDataDataInfo.getTotalCurrent()).divide(java.math.BigDecimal.TEN))"),
            @Mapping(target = "soc", source = "soc"),
            @Mapping(target = "dcdcState", source = "dcdcState.name"),
            @Mapping(target = "driving", source = "driving"),
            @Mapping(target = "braking", source = "braking"),
            @Mapping(target = "gear", source = "gear.name"),
            @Mapping(target = "insulationResistance", source = "insulationResistance"),
            @Mapping(target = "acceleratorPedalPosition", source = "acceleratorPedalPosition"),
            @Mapping(target = "brakePedalPosition", source = "brakePedalPosition")
    })
    GbVehicleDataMpt fromDataInfo(GbVehicleDataDataInfo gbVehicleDataDataInfo);

}
