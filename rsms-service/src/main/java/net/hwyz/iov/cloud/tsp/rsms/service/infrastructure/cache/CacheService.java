package net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.cache;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 缓存服务接口
 *
 * @author hwyz_leo
 */
public interface CacheService {

    /**
     * 获取客户端平台连接状态
     *
     * @param clientPlatformUniqueKey 客户端平台唯一值
     */
    Map<String, Boolean> getClientPlatformConnectState(String clientPlatformUniqueKey);

    /**
     * 获取客户端平台登录状态
     *
     * @param clientPlatformUniqueKey 客户端平台唯一值
     */
    Map<String, Boolean> getClientPlatformLoginState(String clientPlatformUniqueKey);

    /**
     * 获取车辆报警信息
     *
     * @param vin 车架号
     * @return 车辆报警信息
     */
    Map<Integer, Long> getVehicleAlarm(String vin);

    /**
     * 设置车辆报警信息
     *
     * @param vin      车架号
     * @param alarmMap 车辆报警信息
     */
    void setVehicleAlarm(String vin, Map<Integer, Long> alarmMap);

    /**
     * 设置车辆状态
     *
     * @param vin           车架号
     * @param vehicleStatus 车辆状态
     */
    void setVehicleStatus(String vin, Map<String, Object> vehicleStatus);

    /**
     * 获取车辆更新时间
     *
     * @param vin 车架号
     * @return 车辆时间
     */
    Optional<Date> getVehicleTime(String vin);

    /**
     * 获取车辆更新时间范围内的车架号
     *
     * @param endTime 结束时间
     * @return 车架号列表
     */
    List<String> getVehiclesByTimeRange(Date endTime);

    /**
     * 获取车辆更新时间范围内的车架号
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 车架号列表
     */
    List<String> getVehiclesByTimeRange(Date startTime, Date endTime);

}
