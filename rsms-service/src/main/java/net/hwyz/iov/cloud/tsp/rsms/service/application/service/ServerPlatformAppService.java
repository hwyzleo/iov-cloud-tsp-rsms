package net.hwyz.iov.cloud.tsp.rsms.service.application.service;

import cn.hutool.core.util.ObjUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.framework.common.util.ParamHelper;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao.ServerPlatformDao;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.ServerPlatformPo;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 服务端平台应用服务类
 *
 * @author hwyz_leo
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ServerPlatformAppService {

    private final ServerPlatformDao serverPlatformDao;

    /**
     * 查询服务端平台
     *
     * @param code      平台代码
     * @param name      平台名称
     * @param type      平台类型
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 服务端平台列表
     */
    public List<ServerPlatformPo> search(String code, String name, Integer type, Date beginTime, Date endTime) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("name", ParamHelper.fuzzyQueryParam(name));
        map.put("type", type);
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        return serverPlatformDao.selectPoByMap(map);
    }

    /**
     * 检查服务端平台代码是否唯一
     *
     * @param serverPlatformId 服务端平台ID
     * @param code             服务端平台代码
     * @return 结果
     */
    public Boolean checkCodeUnique(Long serverPlatformId, String code) {
        if (ObjUtil.isNull(serverPlatformId)) {
            serverPlatformId = -1L;
        }
        Optional<ServerPlatformPo> serverPlatformPo = getServerPlatformByCode(code);
        return serverPlatformPo.isEmpty() || serverPlatformPo.get().getId().longValue() == serverPlatformId.longValue();
    }

    /**
     * 根据主键ID获取服务端平台
     *
     * @param id 主键ID
     * @return 服务端平台
     */
    public Optional<ServerPlatformPo> getServerPlatformById(Long id) {
        return Optional.ofNullable(serverPlatformDao.selectPoById(id));
    }

    /**
     * 根据服务端平台代码获取服务端平台
     *
     * @param code 服务端平台代码
     * @return 服务端平台
     */
    public Optional<ServerPlatformPo> getServerPlatformByCode(String code) {
        return Optional.ofNullable(serverPlatformDao.selectPoByCode(code));
    }

    /**
     * 新增服务端平台
     *
     * @param serverPlatform 服务端平台
     * @return 结果
     */
    public int createServerPlatform(ServerPlatformPo serverPlatform) {
        return serverPlatformDao.insertPo(serverPlatform);
    }

    /**
     * 修改服务端平台
     *
     * @param serverPlatform 服务端平台
     * @return 结果
     */
    public int modifyServerPlatform(ServerPlatformPo serverPlatform) {
        return serverPlatformDao.updatePo(serverPlatform);
    }

    /**
     * 批量删除服务端平台
     *
     * @param ids 服务端平台ID数组
     * @return 结果
     */
    public int deleteServerPlatformByIds(Long[] ids) {
        return serverPlatformDao.batchPhysicalDeletePo(ids);
    }

}
