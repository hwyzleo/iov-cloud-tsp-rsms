package net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler;

import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbDriveMotorMpt;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.datainfo.GbSingleDriveMotorDataInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 管理后台国标驱动电机数据转换类
 *
 * @author hwyz_leo
 */
@Mapper
public interface GbDriveMotorMptAssembler {

    GbDriveMotorMptAssembler INSTANCE = Mappers.getMapper(GbDriveMotorMptAssembler.class);

    /**
     * 国标数据信息转数据传输对象
     *
     * @param gbDriveMotorDataInfo 数据对象
     * @return 数据传输对象
     */
    @Mappings({
            @Mapping(target = "sn", source = "sn"),
            @Mapping(target = "state", source = "state.name"),
            @Mapping(target = "controllerTemperature", source = "controllerTemperature"),
            @Mapping(target = "speed", source = "speed"),
            @Mapping(target = "torque", expression = "java(java.math.BigDecimal.valueOf(gbDriveMotorDataInfo.getTorque()).divide(java.math.BigDecimal.TEN))"),
            @Mapping(target = "temperature", source = "temperature"),
            @Mapping(target = "controllerInputVoltage", expression = "java(java.math.BigDecimal.valueOf(gbDriveMotorDataInfo.getControllerInputVoltage()).divide(java.math.BigDecimal.TEN))"),
            @Mapping(target = "controllerDcBusCurrent", expression = "java(java.math.BigDecimal.valueOf(gbDriveMotorDataInfo.getControllerDcBusCurrent()).divide(java.math.BigDecimal.TEN))")
    })
    GbDriveMotorMpt fromDataInfo(GbSingleDriveMotorDataInfo gbDriveMotorDataInfo);

    /**
     * 国标数据信息列表转数据传输对象列表
     *
     * @param gbDriveMotorDataInfoList 数据对象列表
     * @return 数据传输对象列表
     */
    List<GbDriveMotorMpt> fromDataInfoList(List<GbSingleDriveMotorDataInfo> gbDriveMotorDataInfoList);

}
