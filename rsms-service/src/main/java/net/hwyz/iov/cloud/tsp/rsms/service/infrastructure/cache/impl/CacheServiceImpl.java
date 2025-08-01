package net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.cache.impl;

import cn.hutool.core.util.ObjUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.cache.CacheService;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 缓存服务实现类
 *
 * @author hwyz_leo
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * Redis Key前缀：客户端平台连接状态
     */
    private static final String REDIS_KEY_PREFIX_CLIENT_PLATFORM_CONNECT_STATE = "rsms:clientPlatformConnectState:";
    /**
     * Redis Key前缀：客户端平台登录状态
     */
    private static final String REDIS_KEY_PREFIX_CLIENT_PLATFORM_LOGIN_STATE = "rsms:clientPlatformLoginState:";
    /**
     * Redis Key前缀：车辆报警
     */
    private static final String REDIS_KEY_PREFIX_VEHICLE_ALARM = "rsms:vehicleAlarm:";
    /**
     * Redis Key：车辆更新时间
     */
    private static final String REDIS_KEY_VEHICLE_TIME = "rsms:vehicleTime";
    /**
     * Redis Key：车辆位置
     */
    private static final String REDIS_KEY_VEHICLE_POSITION = "rsms:vehiclePosition";

    @Override
    public Map<String, Boolean> getClientPlatformConnectState(String clientPlatformUniqueKey) {
        logger.debug("获取客户端平台[{}]连接状态", clientPlatformUniqueKey);
        Map<String, Boolean> map = new HashMap<>();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(REDIS_KEY_PREFIX_CLIENT_PLATFORM_CONNECT_STATE + clientPlatformUniqueKey))) {
            redisTemplate.opsForHash().entries(REDIS_KEY_PREFIX_CLIENT_PLATFORM_CONNECT_STATE + clientPlatformUniqueKey)
                    .forEach((key, value) -> map.put(key.toString(), Boolean.valueOf(value.toString())));
        }
        return map;
    }

    @Override
    public Map<String, Boolean> getClientPlatformLoginState(String clientPlatformUniqueKey) {
        logger.debug("获取客户端平台[{}]登录状态", clientPlatformUniqueKey);
        Map<String, Boolean> map = new HashMap<>();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(REDIS_KEY_PREFIX_CLIENT_PLATFORM_LOGIN_STATE + clientPlatformUniqueKey))) {
            redisTemplate.opsForHash().entries(REDIS_KEY_PREFIX_CLIENT_PLATFORM_LOGIN_STATE + clientPlatformUniqueKey)
                    .forEach((key, value) -> map.put(key.toString(), Boolean.valueOf(value.toString())));
        }
        return map;
    }

    @Override
    public Map<Integer, Long> getVehicleAlarm(String vin) {
        logger.debug("获取车辆[{}]报警信息", vin);
        Map<Integer, Long> alarmMap = new HashMap<>();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(REDIS_KEY_PREFIX_VEHICLE_ALARM + vin))) {
            redisTemplate.opsForHash().entries(REDIS_KEY_PREFIX_VEHICLE_ALARM + vin)
                    .forEach((key, value) -> alarmMap.put(Integer.parseInt(key.toString()), Long.parseLong(value.toString())));
        }
        return alarmMap;
    }

    @Override
    public void setVehicleAlarm(String vin, Map<Integer, Long> alarmMap) {
        if (ObjUtil.isNotNull(alarmMap)) {
            logger.debug("设置车辆[{}]报警信息", vin);
            if (Boolean.TRUE.equals(redisTemplate.hasKey(REDIS_KEY_PREFIX_VEHICLE_ALARM + vin))) {
                redisTemplate.delete(REDIS_KEY_PREFIX_VEHICLE_ALARM + vin);
            }
            alarmMap.forEach((key, value) -> redisTemplate.opsForHash().put(REDIS_KEY_PREFIX_VEHICLE_ALARM + vin, key.toString(), value.toString()));
        }
    }

    @Override
    public void setVehicleStatus(String vin, Map<String, Object> vehicleStatus) {
        logger.debug("设置车辆[{}]状态", vin);
        Object messageTime = vehicleStatus.get("messageTime");
        if (ObjUtil.isNotNull(messageTime)) {
            redisTemplate.opsForZSet().add(REDIS_KEY_VEHICLE_TIME, vin, Double.parseDouble(messageTime.toString()));
        }
        Object latitude = vehicleStatus.get("latitude");
        Object longitude = vehicleStatus.get("longitude");
        if (ObjUtil.isAllNotEmpty(latitude, longitude)) {
            Point point = new Point(Double.parseDouble(longitude.toString()) / 1000000, Double.parseDouble(latitude.toString()) / 1000000);
            redisTemplate.opsForGeo().add(REDIS_KEY_VEHICLE_POSITION, point, vin);
        }
    }

    @Override
    public Optional<Date> getVehicleTime(String vin) {
        logger.debug("获取车辆[{}]更新时间", vin);
        Double timestamp = redisTemplate.opsForZSet().score(REDIS_KEY_VEHICLE_TIME, vin);
        if (ObjUtil.isNotNull(timestamp)) {
            return Optional.of(new Date(timestamp.longValue()));
        }
        return Optional.empty();
    }

    @Override
    public List<String> getVehiclesByTimeRange(Date endTime) {
        return getVehiclesByTimeRange(new Date(0), endTime);
    }

    @Override
    public List<String> getVehiclesByTimeRange(Date startTime, Date endTime) {
        Set<String> vehicleSet = redisTemplate.opsForZSet().rangeByScore(REDIS_KEY_VEHICLE_TIME, startTime.getTime(), endTime.getTime());
        if (ObjUtil.isNotNull(vehicleSet)) {
            return new ArrayList<>(vehicleSet);
        }
        return new ArrayList<>();
    }
}
