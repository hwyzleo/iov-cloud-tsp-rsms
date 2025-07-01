package net.hwyz.iov.cloud.tsp.rsms.service.application.service;

import cn.hutool.core.util.ObjUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao.RegisteredVehicleDao;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.RegisteredVehiclePo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务端平台已注册车辆应用服务类
 *
 * @author hwyz_leo
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RegisteredVehicleAppService {

    private final RegisteredVehicleDao registeredVehicleDao;

    /**
     * 查询已注册车辆
     *
     * @param vin                车架号
     * @param serverPlatformCode 服务端平台代码
     * @param beginTime          开始时间
     * @param endTime            结束时间
     * @return 服务端平台列表
     */
    public List<RegisteredVehiclePo> search(String vin, String serverPlatformCode, Date beginTime, Date endTime) {
        Map<String, Object> map = new HashMap<>();
        map.put("vin", vin);
        map.put("serverPlatformCode", serverPlatformCode);
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        return registeredVehicleDao.selectPoByMap(map);
    }

    /**
     * 检查已注册车辆在平台下是否唯一
     *
     * @param registeredVehicleId 服务端平台ID
     * @param serverPlatformCode  服务端平台代码
     * @param vin                 车架号
     * @return 结果
     */
    public Boolean checkCodeUnique(Long registeredVehicleId, String serverPlatformCode, String vin) {
        if (ObjUtil.isNull(registeredVehicleId)) {
            registeredVehicleId = -1L;
        }
        RegisteredVehiclePo registeredVehiclePo = getRegisteredVehicleByServerPlatformCodeAndVin(serverPlatformCode, vin);
        return !ObjUtil.isNotNull(registeredVehiclePo) || registeredVehiclePo.getId().longValue() == registeredVehicleId.longValue();
    }

    /**
     * 根据主键ID获取已注册车辆
     *
     * @param id 主键ID
     * @return 已注册车辆
     */
    public RegisteredVehiclePo getRegisteredVehicleById(Long id) {
        return registeredVehicleDao.selectPoById(id);
    }

    /**
     * 根据服务端平台代码和车架号获取已注册车辆
     *
     * @param serverPlatformCode 服务端平台代码
     * @param vin                车架号
     * @return 已注册车辆
     */
    public RegisteredVehiclePo getRegisteredVehicleByServerPlatformCodeAndVin(String serverPlatformCode, String vin) {
        return registeredVehicleDao.selectPoByServerPlatformCodeAndVin(serverPlatformCode, vin);
    }

    /**
     * 新增已注册车辆
     *
     * @param registeredVehicle 已注册车辆
     * @return 结果
     */
    public int createRegisteredVehicle(RegisteredVehiclePo registeredVehicle) {
        return registeredVehicleDao.insertPo(registeredVehicle);
    }

    /**
     * 修改已注册车辆
     *
     * @param registeredVehicle 已注册车辆
     * @return 结果
     */
    public int modifyRegisteredVehicle(RegisteredVehiclePo registeredVehicle) {
        return registeredVehicleDao.updatePo(registeredVehicle);
    }

    /**
     * 批量删除已注册车辆
     *
     * @param ids 已注册车辆ID数组
     * @return 结果
     */
    public int deleteRegisteredVehicleByIds(Long[] ids) {
        return registeredVehicleDao.batchPhysicalDeletePo(ids);
    }

}
