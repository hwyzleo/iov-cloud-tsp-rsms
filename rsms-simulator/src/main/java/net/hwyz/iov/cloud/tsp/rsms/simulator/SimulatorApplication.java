package net.hwyz.iov.cloud.tsp.rsms.simulator;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 启动类
 *
 * @author hwyz_leo
 */
@Slf4j
@EnableAsync
@EnableDiscoveryClient
@SpringBootApplication
public class SimulatorApplication {

    public static void main(String[] args) {
        System.setProperty("nacos.logging.default.config.enabled", "false");
        SpringApplication.run(SimulatorApplication.class, args);
        logger.info("应用启动完成");
    }

}
