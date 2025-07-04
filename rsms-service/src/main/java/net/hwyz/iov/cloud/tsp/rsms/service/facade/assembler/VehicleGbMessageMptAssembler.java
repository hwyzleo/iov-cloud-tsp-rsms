package net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler;

import net.hwyz.iov.cloud.tsp.rsms.api.contract.VehicleGbMessageMpt;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.VehicleGbMessagePo;
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
public interface VehicleGbMessageMptAssembler {

    VehicleGbMessageMptAssembler INSTANCE = Mappers.getMapper(VehicleGbMessageMptAssembler.class);

    /**
     * 数据对象转数据传输对象
     *
     * @param vehicleGbMessagePo 数据对象
     * @return 数据传输对象
     */
    @Mappings({})
    VehicleGbMessageMpt fromPo(VehicleGbMessagePo vehicleGbMessagePo);

    /**
     * 数据传输对象转数据对象
     *
     * @param vehicleGbMessageMpt 数据传输对象
     * @return 数据对象
     */
    @Mappings({})
    VehicleGbMessagePo toPo(VehicleGbMessageMpt vehicleGbMessageMpt);

    /**
     * 数据对象列表转数据传输对象列表
     *
     * @param vehicleGbMessagePoList 数据对象列表
     * @return 数据传输对象列表
     */
    List<VehicleGbMessageMpt> fromPoList(List<VehicleGbMessagePo> vehicleGbMessagePoList);

}
