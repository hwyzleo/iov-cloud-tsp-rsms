package net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler;

import net.hwyz.iov.cloud.tsp.rsms.api.contract.VehicleGbAlarmMpt;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.VehicleGbAlarmPo;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 管理后台车辆国标报警转换类
 *
 * @author hwyz_leo
 */
@Mapper
public interface VehicleGbAlarmMptAssembler {

    VehicleGbAlarmMptAssembler INSTANCE = Mappers.getMapper(VehicleGbAlarmMptAssembler.class);

    /**
     * 数据对象转数据传输对象
     *
     * @param vehicleGbAlarmPo 数据对象
     * @return 数据传输对象
     */
    @Mappings({})
    VehicleGbAlarmMpt fromPo(VehicleGbAlarmPo vehicleGbAlarmPo);

    /**
     * 数据传输对象转数据对象
     *
     * @param vehicleGbAlarmMpt 数据传输对象
     * @return 数据对象
     */
    @Mappings({})
    VehicleGbAlarmPo toPo(VehicleGbAlarmMpt vehicleGbAlarmMpt);

    /**
     * 数据对象列表转数据传输对象列表
     *
     * @param vehicleGbAlarmPoList 数据对象列表
     * @return 数据传输对象列表
     */
    List<VehicleGbAlarmMpt> fromPoList(List<VehicleGbAlarmPo> vehicleGbAlarmPoList);

}
