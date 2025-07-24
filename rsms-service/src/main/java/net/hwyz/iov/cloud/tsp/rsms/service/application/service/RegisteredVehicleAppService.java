package net.hwyz.iov.cloud.tsp.rsms.service.application.service;

import cn.hutool.core.util.ObjUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.ClientPlatformCmd;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.VehicleReportState;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.msg.ClientPlatformCmdProducer;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao.RegisteredVehicleDao;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao.ReportVehicleDao;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.RegisteredVehiclePo;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.ReportVehiclePo;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 服务端平台已注册车辆应用服务类
 *
 * @author hwyz_leo
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RegisteredVehicleAppService {

    private final ReportVehicleDao reportVehicleDao;
    private final RegisteredVehicleDao registeredVehicleDao;
    private final ClientPlatformCmdProducer clientPlatformCmdProducer;

    /**
     * 根据客户端平台ID获取已注册车辆列表
     *
     * @param clientPlatformId 客户端平台ID
     * @return 已注册车辆列表
     */
    public List<RegisteredVehiclePo> listByClientPlatformId(Long clientPlatformId) {
        return search(null, null, clientPlatformId, null, null);
    }

    /**
     * 查询已注册车辆
     *
     * @param vin              车架号
     * @param reportState      车辆上报状态
     * @param clientPlatformId 客户端平台ID
     * @param beginTime        开始时间
     * @param endTime          结束时间
     * @return 服务端平台列表
     */
    public List<RegisteredVehiclePo> search(String vin, Integer reportState, Long clientPlatformId, Date beginTime, Date endTime) {
        Map<String, Object> map = new HashMap<>();
        map.put("vin", vin);
        map.put("reportState", reportState);
        map.put("clientPlatformId", clientPlatformId);
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        return registeredVehicleDao.selectPoByMap(map);
    }

    /**
     * 检查已注册车辆在平台下是否唯一
     *
     * @param registeredVehicleId 已注册车辆ID
     * @param clientPlatformId    客户端平台ID
     * @param vin                 车架号
     * @return 结果
     */
    public Boolean checkCodeUnique(Long registeredVehicleId, Long clientPlatformId, String vin) {
        if (ObjUtil.isNull(registeredVehicleId)) {
            registeredVehicleId = -1L;
        }
        RegisteredVehiclePo registeredVehiclePo = getRegisteredVehicleByClientPlatformIdAndVin(clientPlatformId, vin);
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
     * 根据客户端平台ID和车架号获取已注册车辆
     *
     * @param clientPlatformId 客户端平台ID
     * @param vin              车架号
     * @return 已注册车辆
     */
    public RegisteredVehiclePo getRegisteredVehicleByClientPlatformIdAndVin(Long clientPlatformId, String vin) {
        return registeredVehicleDao.selectPoByClientPlatformIdAndVin(clientPlatformId, vin);
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

    /**
     * 修改车辆上报状态
     *
     * @param vin                车架号
     * @param vehicleReportState 车辆上报状态
     */
    public void changeReportState(String vin, VehicleReportState vehicleReportState) {
        Set<Long> clientPlatformIds = new HashSet<>();
        ReportVehiclePo reportVehiclePo = reportVehicleDao.selectPoByVin(vin);
        if (ObjUtil.isNotNull(reportVehiclePo) && vehicleReportState.getCode() != reportVehiclePo.getReportState()) {
            reportVehiclePo.setReportState(vehicleReportState.getCode());
            reportVehicleDao.updatePo(reportVehiclePo);
            registeredVehicleDao.selectPoByExample(RegisteredVehiclePo.builder().vin(vin).build()).forEach(vehicle -> {
                clientPlatformIds.add(vehicle.getClientPlatformId());
            });
        }
        clientPlatformIds.forEach(clientPlatformId -> clientPlatformCmdProducer.send(clientPlatformId, ClientPlatformCmd.SYNC_VEHICLE));
    }

}
