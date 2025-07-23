package net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

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

}
