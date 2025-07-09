package net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler;

import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbAlarmMpt;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.datainfo.GbAlarmDataInfo;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbAlarmFlag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 管理后台国标报警数据转换类
 *
 * @author hwyz_leo
 */
@Mapper
public interface GbAlarmMptAssembler {

    GbAlarmMptAssembler INSTANCE = Mappers.getMapper(GbAlarmMptAssembler.class);

    /**
     * 国标数据信息转数据传输对象
     *
     * @param gbAlarmDataInfo 数据对象
     * @return 数据传输对象
     */
    @Mappings({
            @Mapping(target = "maxAlarmLevel", source = "maxAlarmLevel.name"),
            @Mapping(target = "alarmFlagMap", expression = "java(net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler.GbAlarmMptAssembler.alarmFlagConvert(gbAlarmDataInfo.getAlarmFlagMap()))"),
            @Mapping(target = "batteryFaultCount", source = "batteryFaultCount"),
            @Mapping(target = "batteryFaultList", source = "batteryFaultList"),
            @Mapping(target = "driveMotorFaultCount", source = "driveMotorFaultCount"),
            @Mapping(target = "driveMotorFaultList", source = "driveMotorFaultList"),
            @Mapping(target = "engineFaultCount", source = "engineFaultCount"),
            @Mapping(target = "engineFaultList", source = "engineFaultList"),
            @Mapping(target = "otherFaultCount", source = "otherFaultCount"),
            @Mapping(target = "otherFaultList", source = "otherFaultList")
    })
    GbAlarmMpt fromDataInfo(GbAlarmDataInfo gbAlarmDataInfo);

    static Map<Integer, String> alarmFlagConvert(Map<Integer, Boolean> alarmFlagMap) {
        Map<Integer, String> alarmFlag = new LinkedHashMap<>();
        alarmFlagMap.forEach((key, value) -> {
            if (value) {
                alarmFlag.put(key, GbAlarmFlag.valOf(key).getName());
            }
        });
        return alarmFlag;
    }

}
