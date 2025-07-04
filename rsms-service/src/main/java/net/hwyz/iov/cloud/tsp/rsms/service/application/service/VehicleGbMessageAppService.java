package net.hwyz.iov.cloud.tsp.rsms.service.application.service;

import cn.hutool.core.util.HexUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessage;
import net.hwyz.iov.cloud.tsp.rsms.api.util.GbUtil;
import net.hwyz.iov.cloud.tsp.rsms.service.application.event.event.VehicleGbMessageEvent;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao.VehicleGbMessageDao;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.VehicleGbMessagePo;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<VehicleGbMessagePo> search(String vin, String commandFlag, Date beginTime, Date endTime) {
        Map<String, Object> map = new HashMap<>();
        map.put("vin", vin);
        map.put("commandFlag", commandFlag);
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
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
                .messageTime(GbUtil.dateTimeBytesToDate(gbMessage.getMessageTime()))
                .commandFlag(gbMessage.getCommandFlag().name())
                .messageData(HexUtil.encodeHexStr(gbMessage.toByteArray()))
                .build());
    }

}
