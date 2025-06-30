package net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler;

import net.hwyz.iov.cloud.tsp.rsms.api.contract.ServerPlatformMpt;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.ServerPlatformPo;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 管理后台服务端平台转换类
 *
 * @author hwyz_leo
 */
@Mapper
public interface ServerPlatformMptAssembler {

    ServerPlatformMptAssembler INSTANCE = Mappers.getMapper(ServerPlatformMptAssembler.class);

    /**
     * 数据对象转数据传输对象
     *
     * @param serverPlatformPo 数据对象
     * @return 数据传输对象
     */
    @Mappings({})
    ServerPlatformMpt fromPo(ServerPlatformPo serverPlatformPo);

    /**
     * 数据传输对象转数据对象
     *
     * @param serverPlatformMpt 数据传输对象
     * @return 数据对象
     */
    @Mappings({})
    ServerPlatformPo toPo(ServerPlatformMpt serverPlatformMpt);

    /**
     * 数据对象列表转数据传输对象列表
     *
     * @param serverPlatformPoList 数据对象列表
     * @return 数据传输对象列表
     */
    List<ServerPlatformMpt> fromPoList(List<ServerPlatformPo> serverPlatformPoList);

}
