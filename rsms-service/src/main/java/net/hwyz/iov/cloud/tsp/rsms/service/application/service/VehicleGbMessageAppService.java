package net.hwyz.iov.cloud.tsp.rsms.service.application.service;

import cn.hutool.core.util.HexUtil;
import cn.hutool.json.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.*;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.datainfo.*;
import net.hwyz.iov.cloud.tsp.rsms.service.application.event.event.VehicleGbMessageEvent;
import net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler.*;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao.VehicleGbMessageDao;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.VehicleGbMessagePo;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

import static net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbDataInfoType.*;

/**
 * 车辆国标消息历史应用服务类
 *
 * @author hwyz_leo
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleGbMessageAppService {

    private final VehicleGbMessageDao vehicleGbMessageDao;

    /**
     * 查询车辆国标消息历史
     *
     * @param vin         车架号
     * @param commandFlag 命令标识
     * @param beginTime   开始时间
     * @param endTime     结束时间
     * @return 车辆国标消息列表
     */
    public List<VehicleGbMessagePo> search(String vin, String commandFlag, Date beginTime, Date endTime, String sort) {
        Map<String, Object> map = new HashMap<>();
        map.put("vin", vin);
        map.put("commandFlag", commandFlag);
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        map.put("sort", sort);
        return vehicleGbMessageDao.selectPoByMap(map);
    }

    /**
     * 根据主键ID获取车辆国标消息
     *
     * @param id 主键ID
     * @return 车辆国标消息
     */
    public VehicleGbMessagePo getVehicleGbMessageById(Long id) {
        return vehicleGbMessageDao.selectPoById(id);
    }

    /**
     * 组装参数
     *
     * @param dataInfo   数据信息
     * @param jsonObject json对象
     */
    public void assembleParams(GbMessageDataInfo dataInfo, JSONObject jsonObject) {
        switch (dataInfo.getDataInfoType()) {
            case VEHICLE -> {
                GbVehicleDataMpt vehicleData = GbVehicleDataMptAssembler.INSTANCE.fromDataInfo((GbVehicleDataDataInfo) dataInfo);
                jsonObject.set(VEHICLE.name(), vehicleData);
            }
            case DRIVE_MOTOR -> {
                LinkedList<GbSingleDriveMotorDataInfo> driveMotorList = ((GbDriveMotorDataInfo) dataInfo).getDriveMotorList();
                List<GbDriveMotorMpt> driveMotorMptList = GbDriveMotorMptAssembler.INSTANCE.fromDataInfoList(driveMotorList);
                jsonObject.set(DRIVE_MOTOR.name(), driveMotorMptList);
            }
            case FUEL_CELL -> {
                GbFuelCellMpt fuelCell = GbFuelCellMptAssembler.INSTANCE.fromDataInfo((GbFuelCellDataInfo) dataInfo);
                jsonObject.set(FUEL_CELL.name(), fuelCell);
            }
            case ENGINE -> {
                GbEngineMpt engine = GbEngineMptAssembler.INSTANCE.fromDataInfo((GbEngineDataInfo) dataInfo);
                jsonObject.set(ENGINE.name(), engine);
            }
            case POSITION -> {
                GbPositionMpt position = GbPositionMptAssembler.INSTANCE.fromDataInfo((GbPositionDataInfo) dataInfo);
                jsonObject.set(POSITION.name(), position);
            }
            case EXTREMUM -> {
                GbExtremumMpt extremum = GbExtremumMptAssembler.INSTANCE.fromDataInfo((GbExtremumDataInfo) dataInfo);
                jsonObject.set(EXTREMUM.name(), extremum);
            }
            case ALARM -> {
                GbAlarmMpt alarm = GbAlarmMptAssembler.INSTANCE.fromDataInfo((GbAlarmDataInfo) dataInfo);
                jsonObject.set(ALARM.name(), alarm);
            }
            case BATTERY_VOLTAGE -> {
                LinkedList<GbSingleBatteryVoltageDataInfo> batteryVoltageList = ((GbBatteryVoltageDataInfo) dataInfo).getBatteryVoltageList();
                List<GbBatteryVoltageMpt> batteryVoltageMptList = GbBatteryVoltageMptAssembler.INSTANCE.fromDataInfoList(batteryVoltageList);
                jsonObject.set(BATTERY_VOLTAGE.name(), batteryVoltageMptList);
            }
            case BATTERY_TEMPERATURE -> {
                LinkedList<GbSingleBatteryTemperatureDataInfo> batteryTemperatureList = ((GbBatteryTemperatureDataInfo) dataInfo).getBatteryTemperatureList();
                List<GbBatteryTemperatureMpt> batteryTemperatureMptList = GbBatteryTemperatureMptAssembler.INSTANCE.fromDataInfoList(batteryTemperatureList);
                jsonObject.set(BATTERY_TEMPERATURE.name(), batteryTemperatureMptList);
            }
        }
    }

    /**
     * 新增车辆国标消息
     *
     * @param vehicleGbMessage 车辆国标消息
     * @return 结果
     */
    public int createVehicleGbMessage(VehicleGbMessagePo vehicleGbMessage) {
        return vehicleGbMessageDao.insertPo(vehicleGbMessage);
    }

    /**
     * 修改车辆国标消息
     *
     * @param vehicleGbMessage 车辆国标消息
     * @return 结果
     */
    public int modifyVehicleGbMessage(VehicleGbMessagePo vehicleGbMessage) {
        return vehicleGbMessageDao.updatePo(vehicleGbMessage);
    }

    /**
     * 批量删除车辆国标消息
     *
     * @param ids 车辆国标消息ID数组
     * @return 结果
     */
    public int deleteVehicleGbMessageByIds(Long[] ids) {
        return vehicleGbMessageDao.batchPhysicalDeletePo(ids);
    }

    /**
     * 订阅车辆国标消息事件
     *
     * @param event 车辆国标消息事件
     */
    @Async
    @EventListener
    public void onVehicleGbMessageEvent(VehicleGbMessageEvent event) {
        GbMessage gbMessage = event.getGbMessage();
        vehicleGbMessageDao.insertPo(VehicleGbMessagePo.builder()
                .vin(event.getVin())
                .parseTime(new Date())
                .messageTime(gbMessage.getMessageTime())
                .commandFlag(gbMessage.getHeader().getCommandFlag().name())
                .messageData(HexUtil.encodeHexStr(gbMessage.toByteArray()))
                .build());
    }

}
