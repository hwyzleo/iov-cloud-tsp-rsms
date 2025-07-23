package net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler;

import net.hwyz.iov.cloud.tsp.rsms.api.contract.ClientPlatformAccountMpt;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.ClientPlatformAccountPo;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 管理后台客户端平台账号转换类
 *
 * @author hwyz_leo
 */
@Mapper
public interface ClientPlatformAccountMptAssembler {

    ClientPlatformAccountMptAssembler INSTANCE = Mappers.getMapper(ClientPlatformAccountMptAssembler.class);

    /**
     * 数据对象转数据传输对象
     *
     * @param clientPlatformAccountPo 数据对象
     * @return 数据传输对象
     */
    @Mappings({})
    ClientPlatformAccountMpt fromPo(ClientPlatformAccountPo clientPlatformAccountPo);

    /**
     * 数据传输对象转数据对象
     *
     * @param clientPlatformAccountMpt 数据传输对象
     * @return 数据对象
     */
    @Mappings({})
    ClientPlatformAccountPo toPo(ClientPlatformAccountMpt clientPlatformAccountMpt);

    /**
     * 数据对象列表转数据传输对象列表
     *
     * @param clientPlatformAccountPoList 数据对象列表
     * @return 数据传输对象列表
     */
    List<ClientPlatformAccountMpt> fromPoList(List<ClientPlatformAccountPo> clientPlatformAccountPoList);

}
