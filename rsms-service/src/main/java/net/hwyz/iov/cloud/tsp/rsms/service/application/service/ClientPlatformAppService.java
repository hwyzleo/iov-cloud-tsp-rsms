package net.hwyz.iov.cloud.tsp.rsms.service.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao.ClientPlatformDao;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.ClientPlatformPo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户端平台应用服务类
 *
 * @author hwyz_leo
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClientPlatformAppService {

    private final ClientPlatformDao clientPlatformDao;

    /**
     * 根据服务端平台编码查询客户端平台
     *
     * @param serverPlatformCode 服务端平台编码
     * @return 客户端平台列表
     */
    public List<ClientPlatformPo> listByServerPlatformCode(String serverPlatformCode) {
        return search(null, serverPlatformCode, null, null);
    }

    /**
     * 查询客户端平台
     *
     * @param uniqueCode         唯一识别码
     * @param serverPlatformCode 服务端平台编码
     * @param beginTime          开始时间
     * @param endTime            结束时间
     * @return 客户端平台列表
     */
    public List<ClientPlatformPo> search(String uniqueCode, String serverPlatformCode, Date beginTime, Date endTime) {
        Map<String, Object> map = new HashMap<>();
        map.put("uniqueCode", uniqueCode);
        map.put("serverPlatformCode", serverPlatformCode);
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        return clientPlatformDao.selectPoByMap(map);
    }

    /**
     * 根据主键ID获取客户端平台
     *
     * @param id 主键ID
     * @return 客户端平台
     */
    public ClientPlatformPo getClientPlatformById(Long id) {
        return clientPlatformDao.selectPoById(id);
    }

    /**
     * 新增客户端平台
     *
     * @param clientPlatform 客户端平台
     * @return 结果
     */
    public int createClientPlatform(ClientPlatformPo clientPlatform) {
        return clientPlatformDao.insertPo(clientPlatform);
    }

    /**
     * 修改客户端平台
     *
     * @param clientPlatform 客户端平台
     * @return 结果
     */
    public int modifyClientPlatform(ClientPlatformPo clientPlatform) {
        return clientPlatformDao.updatePo(clientPlatform);
    }

    /**
     * 批量删除客户端平台
     *
     * @param ids 客户端平台ID数组
     * @return 结果
     */
    public int deleteClientPlatformByIds(Long[] ids) {
        return clientPlatformDao.batchPhysicalDeletePo(ids);
    }

}
