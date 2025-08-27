package net.hwyz.iov.cloud.tsp.rsms.service.application.service;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessage;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessageDataInfo;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.datainfo.GbAlarmDataInfo;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.dataunit.GbRealtimeReportDataUnit;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbAlarmFlag;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbAlarmLevel;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbDataInfoType;
import net.hwyz.iov.cloud.tsp.rsms.service.application.event.event.VehicleGbRealtimeMessageEvent;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.cache.CacheService;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao.VehicleGbAlarmDao;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.VehicleGbAlarmPo;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 车辆国标报警历史应用服务类
 *
 * @author hwyz_leo
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleGbAlarmAppService {

    private final CacheService cacheService;
    private final VehicleGbAlarmDao vehicleGbAlarmDao;

    /**
     * 查询车辆国标报警历史
     *
     * @param vin        车架号
     * @param alarmFlag  报警标识位
     * @param alarmLevel 报警级别
     * @param beginTime  开始时间
     * @param endTime    结束时间
     * @return 车辆国标消息列表
     */
    public List<VehicleGbAlarmPo> search(String vin, Integer alarmFlag, Integer alarmLevel, Date beginTime, Date endTime) {
        Map<String, Object> map = new HashMap<>();
        map.put("vin", vin);
        map.put("alarmFlag", alarmFlag);
        map.put("alarmLevel", alarmLevel);
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        return vehicleGbAlarmDao.selectPoByMap(map);
    }

    /**
     * 根据主键ID获取车辆国标报警
     *
     * @param id 主键ID
     * @return 车辆国标报警
     */
    public VehicleGbAlarmPo getVehicleGbAlarmById(Long id) {
        return vehicleGbAlarmDao.selectPoById(id);
    }

    /**
     * 新增车辆国标报警
     *
     * @param vehicleGbAlarm 车辆国标报警
     * @return 结果
     */
    public int createVehicleGbAlarm(VehicleGbAlarmPo vehicleGbAlarm) {
        return vehicleGbAlarmDao.insertPo(vehicleGbAlarm);
    }

    /**
     * 修改车辆国标报警
     *
     * @param vehicleGbAlarm 车辆国标报警
     * @return 结果
     */
    public int modifyVehicleGbAlarm(VehicleGbAlarmPo vehicleGbAlarm) {
        return vehicleGbAlarmDao.updatePo(vehicleGbAlarm);
    }

    /**
     * 批量删除车辆国标报警
     *
     * @param ids 车辆国标报警ID数组
     * @return 结果
     */
    public int deleteVehicleGbMessageByIds(Long[] ids) {
        return vehicleGbAlarmDao.batchPhysicalDeletePo(ids);
    }

    /**
     * 解析车辆国标消息中通用报警信息
     *
     * @param gbMessage 国标实时消息
     */
    private void parseGeneralAlarm(GbMessage gbMessage) {
        logger.debug("解析车辆[{}]国标消息中通用报警信息", gbMessage.getVin());
        gbMessage.parseDataUnit(gbMessage.getDataUnitBytes());
        GbRealtimeReportDataUnit dataUnit = (GbRealtimeReportDataUnit) gbMessage.getDataUnit();
        if (dataUnit == null) {
            logger.warn("车辆[{}]国标实时信号[{}]消息数据单元为空", gbMessage.getVin(), JSONUtil.toJsonStr(gbMessage));
            return;
        }
        List<GbAlarmLevel> alarmRange = ListUtil.of(GbAlarmLevel.LEVEL1, GbAlarmLevel.LEVEL2, GbAlarmLevel.LEVEL2);
        for (GbMessageDataInfo dataInfo : dataUnit.getDataInfoList()) {
            if (dataInfo.getDataInfoType() == GbDataInfoType.ALARM) {
                GbAlarmDataInfo alarmDataInfo = (GbAlarmDataInfo) dataInfo;
                if (alarmRange.contains(alarmDataInfo.getMaxAlarmLevel())) {
                    handleGeneralAlarm(gbMessage.getHeader().getUniqueCode(), gbMessage.getMessageTime(),
                            alarmDataInfo.getAlarmFlagMap(), HexUtil.encodeHexStr(gbMessage.toByteArray()));
                }
                break;
            }
        }
    }

    /**
     * 处理车辆通用报警信息
     *
     * @param vin          车架号
     * @param messageTime  消息时间
     * @param alarmFlagMap 报警标识位
     * @param messageData  消息数据
     */
    private void handleGeneralAlarm(String vin, Date messageTime, Map<Integer, Boolean> alarmFlagMap, String messageData) {
        Map<Integer, Long> alarmMap = cacheService.getVehicleAlarm(vin);
        AtomicBoolean isChanged = new AtomicBoolean(false);
        Integer lastAlarmTimeKey = 999;
        long lastAlarmTime = alarmMap.containsKey(lastAlarmTimeKey) ? alarmMap.get(lastAlarmTimeKey) : 0;
        if (messageTime.getTime() < lastAlarmTime) {
            // 乱序数据直接抛弃
            return;
        }
        alarmFlagMap.forEach((alarmFlag, isAlarm) -> {
            if (isAlarm) {
                if (!alarmMap.containsKey(alarmFlag)) {
                    vehicleGbAlarmDao.insertPo(VehicleGbAlarmPo.builder()
                            .vin(vin)
                            .alarmTime(messageTime)
                            .alarmFlag(alarmFlag)
                            .alarmLevel(GbAlarmFlag.valOf(alarmFlag).getLevel())
                            .messageData(messageData)
                            .build());
                    alarmMap.put(alarmFlag, messageTime.getTime());
                    isChanged.set(true);
                }
            } else {
                if (alarmMap.containsKey(alarmFlag)) {
                    VehicleGbAlarmPo alarmPo = vehicleGbAlarmDao.selectLastNotRestorationByVinAndAlarmFlag(vin, alarmFlag);
                    if (ObjUtil.isNotNull(alarmPo)) {
                        alarmPo.setRestorationTime(messageTime);
                        vehicleGbAlarmDao.updatePo(alarmPo);
                    }
                    alarmMap.remove(alarmFlag);
                    isChanged.set(true);
                }
            }
        });
        if (isChanged.get()) {
            alarmMap.put(lastAlarmTimeKey, messageTime.getTime());
            cacheService.setVehicleAlarm(vin, alarmMap);
        }
    }

    /**
     * 订阅车辆国标实时消息事件
     *
     * @param event 车辆国标实时消息事件
     */
    @Async
    @EventListener
    public void onVehicleGbRealtimeMessageEvent(VehicleGbRealtimeMessageEvent event) {
        parseGeneralAlarm(event.getGbMessage());
    }

}
