package net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler;

import net.hwyz.iov.cloud.tsp.rsms.api.contract.ReportVehicleMpt;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.ReportVehiclePo;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 管理后台上报车辆转换类
 *
 * @author hwyz_leo
 */
@Mapper
public interface ReportVehicleMptAssembler {

    ReportVehicleMptAssembler INSTANCE = Mappers.getMapper(ReportVehicleMptAssembler.class);

    /**
     * 数据对象转数据传输对象
     *
     * @param reportVehiclePo 数据对象
     * @return 数据传输对象
     */
    @Mappings({})
    ReportVehicleMpt fromPo(ReportVehiclePo reportVehiclePo);

    /**
     * 数据传输对象转数据对象
     *
     * @param reportVehicleMpt 数据传输对象
     * @return 数据对象
     */
    @Mappings({})
    ReportVehiclePo toPo(ReportVehicleMpt reportVehicleMpt);

    /**
     * 数据对象列表转数据传输对象列表
     *
     * @param reportVehiclePoList 数据对象列表
     * @return 数据传输对象列表
     */
    List<ReportVehicleMpt> fromPoList(List<ReportVehiclePo> reportVehiclePoList);

}
