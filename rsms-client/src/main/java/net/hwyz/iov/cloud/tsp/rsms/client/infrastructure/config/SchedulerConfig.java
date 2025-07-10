package net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.client.application.service.ServerPlatformAppService;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 定时任务配置类
 *
 * @author hwyz_leo
 */
@Slf4j
@RefreshScope
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig {

    private final ServerPlatformAppService serverPlatformAppService;

    /**
     * 定时同步已注册车辆
     * 每日
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void syncRegisteredVehicle() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        logger.info("定时[{}]同步所有平台已注册车辆集合", sdf.format(now));
        serverPlatformAppService.syncVehicleSet();
    }

}
