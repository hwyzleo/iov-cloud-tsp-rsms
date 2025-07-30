package net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler;

import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbInspectionReportMpt;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.GbInspectionReportPo;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 管理后台国标检测报告转换类
 *
 * @author hwyz_leo
 */
@Mapper
public interface GbInspectionReportMptAssembler {

    GbInspectionReportMptAssembler INSTANCE = Mappers.getMapper(GbInspectionReportMptAssembler.class);

    /**
     * 数据对象转数据传输对象
     *
     * @param gbInspectionReportPo 数据对象
     * @return 数据传输对象
     */
    @Mappings({})
    GbInspectionReportMpt fromPo(GbInspectionReportPo gbInspectionReportPo);

    /**
     * 数据传输对象转数据对象
     *
     * @param gbInspectionReportMpt 数据传输对象
     * @return 数据对象
     */
    @Mappings({})
    GbInspectionReportPo toPo(GbInspectionReportMpt gbInspectionReportMpt);

    /**
     * 数据对象列表转数据传输对象列表
     *
     * @param gbInspectionReportPoList 数据对象列表
     * @return 数据传输对象列表
     */
    List<GbInspectionReportMpt> fromPoList(List<GbInspectionReportPo> gbInspectionReportPoList);

}
