package net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler;

import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbEngineMpt;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbPositionMpt;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.datainfo.GbEngineDataInfo;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.datainfo.GbPositionDataInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 管理后台国标车辆位置数据转换类
 *
 * @author hwyz_leo
 */
@Mapper
public interface GbPositionMptAssembler {

    GbPositionMptAssembler INSTANCE = Mappers.getMapper(GbPositionMptAssembler.class);

    /**
     * 国标数据信息转数据传输对象
     *
     * @param gbPositionDataInfo 数据对象
     * @return 数据传输对象
     */
    @Mappings({
            @Mapping(target = "positionValid", source = "positionValid"),
            @Mapping(target = "southLatitude", source = "southLatitude"),
            @Mapping(target = "westLongitude", source = "westLongitude"),
            @Mapping(target = "longitude", expression = "java(java.math.BigDecimal.valueOf(gbPositionDataInfo.getLongitude()).divide(java.math.BigDecimal.valueOf(1000000)))"),
            @Mapping(target = "latitude", expression = "java(java.math.BigDecimal.valueOf(gbPositionDataInfo.getLatitude()).divide(java.math.BigDecimal.valueOf(1000000)))")
    })
    GbPositionMpt fromDataInfo(GbPositionDataInfo gbPositionDataInfo);

}
