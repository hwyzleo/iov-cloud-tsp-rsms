package net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler;

import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbEngineMpt;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.datainfo.GbEngineDataInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 管理后台国标发动机数据转换类
 *
 * @author hwyz_leo
 */
@Mapper
public interface GbEngineMptAssembler {

    GbEngineMptAssembler INSTANCE = Mappers.getMapper(GbEngineMptAssembler.class);

    /**
     * 国标数据信息转数据传输对象
     *
     * @param gbEngineDataInfo 数据对象
     * @return 数据传输对象
     */
    @Mappings({
            @Mapping(target = "state", source = "state.name"),
            @Mapping(target = "crankshaftSpeed", source = "crankshaftSpeed"),
            @Mapping(target = "consumptionRate", expression = "java(java.math.BigDecimal.valueOf(gbEngineDataInfo.getConsumptionRate()).divide(java.math.BigDecimal.TEN).divide(java.math.BigDecimal.TEN))")
    })
    GbEngineMpt fromDataInfo(GbEngineDataInfo gbEngineDataInfo);

}
