package net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler;

import net.hwyz.iov.cloud.tsp.rsms.api.contract.ClientPlatformMpt;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.ClientPlatformPo;
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
public interface ClientPlatformMptAssembler {

    ClientPlatformMptAssembler INSTANCE = Mappers.getMapper(ClientPlatformMptAssembler.class);

    /**
     * 数据对象转数据传输对象
     *
     * @param clientPlatformPo 数据对象
     * @return 数据传输对象
     */
    @Mappings({})
    ClientPlatformMpt fromPo(ClientPlatformPo clientPlatformPo);

    /**
     * 数据传输对象转数据对象
     *
     * @param clientPlatformMpt 数据传输对象
     * @return 数据对象
     */
    @Mappings({})
    ClientPlatformPo toPo(ClientPlatformMpt clientPlatformMpt);

    /**
     * 数据对象列表转数据传输对象列表
     *
     * @param clientPlatformPoList 数据对象列表
     * @return 数据传输对象列表
     */
    List<ClientPlatformMpt> fromPoList(List<ClientPlatformPo> clientPlatformPoList);

}
