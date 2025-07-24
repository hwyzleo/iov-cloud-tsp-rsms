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
 * 上报车辆应用服务类
 *
 * @author hwyz_leo
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReportVehicleAppService {

    private final ReportVehicleDao reportVehicleDao;
    private final RegisteredVehicleDao registeredVehicleDao;
    private final ClientPlatformCmdProducer clientPlatformCmdProducer;

    /**
     * 查询上报车辆
     *
     * @param vin         车架号
     * @param reportState 车辆上报状态
     * @param beginTime   开始时间
     * @param endTime     结束时间
     * @return 上报车辆列表
     */
    public List<ReportVehiclePo> search(String vin, Integer reportState, Date beginTime, Date endTime) {
        Map<String, Object> map = new HashMap<>();
        map.put("vin", vin);
        map.put("reportState", reportState);
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        return reportVehicleDao.selectPoByMap(map);
    }

    /**
     * 检查上报车辆是否唯一
     *
     * @param reportVehicleId 上报车辆ID
     * @param vin             车架号
     * @return 结果
     */
    public Boolean checkCodeUnique(Long reportVehicleId, String vin) {
        if (ObjUtil.isNull(reportVehicleId)) {
            reportVehicleId = -1L;
        }
        ReportVehiclePo reportVehiclePo = getReportVehicleByVin(vin);
        return !ObjUtil.isNotNull(reportVehiclePo) || reportVehiclePo.getId().longValue() == reportVehicleId.longValue();
    }

    /**
     * 根据主键ID获取上报车辆
     *
     * @param id 主键ID
     * @return 上报车辆
     */
    public ReportVehiclePo getReportVehicleById(Long id) {
        return reportVehicleDao.selectPoById(id);
    }

    /**
     * 根据车架号获取上报车辆
     *
     * @param vin 车架号
     * @return 上报车辆
     */
    public ReportVehiclePo getReportVehicleByVin(String vin) {
        return reportVehicleDao.selectPoByVin(vin);
    }

    /**
     * 新增上报车辆
     *
     * @param reportVehiclePo 上报车辆
     * @return 结果
     */
    public int createReportVehicle(ReportVehiclePo reportVehiclePo) {
        return reportVehicleDao.insertPo(reportVehiclePo);
    }

    /**
     * 修改上报车辆
     *
     * @param reportVehiclePo 上报车辆
     * @return 结果
     */
    public int modifyReportVehicle(ReportVehiclePo reportVehiclePo) {
        return reportVehicleDao.updatePo(reportVehiclePo);
    }

    /**
     * 批量删除上报车辆
     *
     * @param ids 上报车辆ID数组
     * @return 结果
     */
    public int deleteReportVehicleByIds(Long[] ids) {
        return reportVehicleDao.batchPhysicalDeletePo(ids);
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
