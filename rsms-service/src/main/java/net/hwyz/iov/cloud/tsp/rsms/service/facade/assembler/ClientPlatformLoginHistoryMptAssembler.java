package net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler;

import net.hwyz.iov.cloud.tsp.rsms.api.contract.ClientPlatformLoginHistoryMpt;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.ClientPlatformLoginHistoryPo;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 管理后台客户端平台转换类
 *
 * @author hwyz_leo
 */
@Mapper
public interface ClientPlatformLoginHistoryMptAssembler {

    ClientPlatformLoginHistoryMptAssembler INSTANCE = Mappers.getMapper(ClientPlatformLoginHistoryMptAssembler.class);

    /**
     * 数据对象转数据传输对象
     *
     * @param clientPlatformLoginHistoryPo 数据对象
     * @return 数据传输对象
     */
    @Mappings({})
    ClientPlatformLoginHistoryMpt fromPo(ClientPlatformLoginHistoryPo clientPlatformLoginHistoryPo);

    /**
     * 数据传输对象转数据对象
     *
     * @param clientPlatformLoginHistoryMpt 数据传输对象
     * @return 数据对象
     */
    @Mappings({})
    ClientPlatformLoginHistoryPo toPo(ClientPlatformLoginHistoryMpt clientPlatformLoginHistoryMpt);

    /**
     * 数据对象列表转数据传输对象列表
     *
     * @param clientPlatformLoginHistoryPoList 数据对象列表
     * @return 数据传输对象列表
     */
    List<ClientPlatformLoginHistoryMpt> fromPoList(List<ClientPlatformLoginHistoryPo> clientPlatformLoginHistoryPoList);

}
