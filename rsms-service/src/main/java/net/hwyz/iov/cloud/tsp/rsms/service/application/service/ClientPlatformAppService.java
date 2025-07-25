package net.hwyz.iov.cloud.tsp.rsms.service.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao.ClientPlatformAccountDao;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao.ClientPlatformDao;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao.RegisteredVehicleDao;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.ClientPlatformAccountPo;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.ClientPlatformPo;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.RegisteredVehiclePo;
import org.springframework.stereotype.Service;

import java.util.*;

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
    private final RegisteredVehicleDao registeredVehicleDao;
    private final ClientPlatformAccountDao clientPlatformAccountDao;

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
     * 根据客户端平台ID查询客户端平台账号
     *
     * @param clientPlatformId 客户端平台ID
     * @return 客户端平台账号列表
     */
    public List<ClientPlatformAccountPo> listAccount(Long clientPlatformId) {
        return clientPlatformAccountDao.selectPoByExample(ClientPlatformAccountPo.builder()
                .clientPlatformId(clientPlatformId)
                .build());
    }

    /**
     * 根据客户端平台ID查询已注册车辆
     *
     * @param clientPlatformId 客户端平台ID
     * @return 已注册车辆列表
     */
    public List<RegisteredVehiclePo> listRegisteredVehicle(Long clientPlatformId) {
        return registeredVehicleDao.selectPoByExample(RegisteredVehiclePo.builder()
                .clientPlatformId(clientPlatformId)
                .build());
    }

    /**
     * 根据主键ID获取客户端平台
     *
     * @param id 主键ID
     * @return 客户端平台
     */
    public Optional<ClientPlatformPo> getClientPlatformById(Long id) {
        return Optional.ofNullable(clientPlatformDao.selectPoById(id));
    }

    /**
     * 根据主键ID获取客户端平台账号
     *
     * @param id 主键ID
     * @return 客户端平台账号
     */
    public Optional<ClientPlatformAccountPo> getClientPlatformAccountById(Long id) {
        return Optional.ofNullable(clientPlatformAccountDao.selectPoById(id));
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
     * 新增客户端平台账号
     *
     * @param clientPlatformAccount 客户端平台账号
     * @return 结果
     */
    public int createClientPlatformAccount(ClientPlatformAccountPo clientPlatformAccount) {
        return clientPlatformAccountDao.insertPo(clientPlatformAccount);
    }

    /**
     * 新增注册车辆
     *
     * @param registeredVehicle 注册车辆
     * @return 结果
     */
    public int createRegisteredVehicle(RegisteredVehiclePo registeredVehicle) {
        return registeredVehicleDao.insertPo(registeredVehicle);
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
     * 修改客户端平台账号
     *
     * @param clientPlatformAccount 客户端平台账号
     * @return 结果
     */
    public int modifyClientPlatformAccount(ClientPlatformAccountPo clientPlatformAccount) {
        return clientPlatformAccountDao.updatePo(clientPlatformAccount);
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

    /**
     * 批量删除客户端平台账号
     *
     * @param ids 客户端平台账号ID数组
     * @return 结果
     */
    public int deleteClientPlatformAccountByIds(Long[] ids) {
        return clientPlatformAccountDao.batchPhysicalDeletePo(ids);
    }

    /**
     * 批量删除注册车辆
     *
     * @param ids 注册车辆ID数组
     * @return 结果
     */
    public int deleteRegisteredVehicleByIds(Long[] ids) {
        return registeredVehicleDao.batchPhysicalDeletePo(ids);
    }

}
