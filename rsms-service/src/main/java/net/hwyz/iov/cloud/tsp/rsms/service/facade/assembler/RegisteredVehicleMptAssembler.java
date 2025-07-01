package net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler;

import net.hwyz.iov.cloud.tsp.rsms.api.contract.RegisteredVehicleMpt;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.RegisteredVehiclePo;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 管理后台服务端平台已注册车辆转换类
 *
 * @author hwyz_leo
 */
@Mapper
public interface RegisteredVehicleMptAssembler {

    RegisteredVehicleMptAssembler INSTANCE = Mappers.getMapper(RegisteredVehicleMptAssembler.class);

    /**
     * 数据对象转数据传输对象
     *
     * @param registeredVehiclePo 数据对象
     * @return 数据传输对象
     */
    @Mappings({})
    RegisteredVehicleMpt fromPo(RegisteredVehiclePo registeredVehiclePo);

    /**
     * 数据传输对象转数据对象
     *
     * @param registeredVehicleMpt 数据传输对象
     * @return 数据对象
     */
    @Mappings({})
    RegisteredVehiclePo toPo(RegisteredVehicleMpt registeredVehicleMpt);

    /**
     * 数据对象列表转数据传输对象列表
     *
     * @param registeredVehiclePoList 数据对象列表
     * @return 数据传输对象列表
     */
    List<RegisteredVehicleMpt> fromPoList(List<RegisteredVehiclePo> registeredVehiclePoList);

}
