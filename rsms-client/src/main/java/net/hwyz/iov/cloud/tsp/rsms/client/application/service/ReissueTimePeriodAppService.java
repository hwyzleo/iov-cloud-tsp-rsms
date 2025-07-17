package net.hwyz.iov.cloud.tsp.rsms.client.application.service;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.ObjUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.framework.common.util.StrUtil;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbCommandFlag;
import net.hwyz.iov.cloud.tsp.rsms.api.util.GbUtil;
import net.hwyz.iov.cloud.tsp.rsms.client.application.event.event.ClientPlatformStateEvent;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.model.ClientPlatformDo;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.msg.GbReissueDataProducer;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.dao.ReissueTimePeriodDao;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.dao.VehicleGbMessageDao;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.po.ReissueTimePeriodPo;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.po.VehicleGbMessagePo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 补发时间段应用服务类
 *
 * @author hwyz_leo
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReissueTimePeriodAppService {

    private final VehicleGbMessageDao vehicleGbMessageDao;
    private final ReissueTimePeriodDao reissueTimePeriodDao;
    private final GbReissueDataProducer gbReissueDataProducer;

    /**
     * 补发状态：未补发
     */
    private final static int REISSUE_STATE_INIT = 0;
    /**
     * 补发状态：正在补发
     */
    private final static int REISSUE_STATE_REISSUING = 1;
    /**
     * 补发状态：补发完成
     */
    private final static int REISSUE_STATE_FINISHED = 2;

    /**
     * 补发间隔时间，单位：分钟
     */
    @Value("${biz.reissueIntervalMinutes:5}")
    private Integer reissueIntervalMinutes;

    /**
     * 记录补发时间段开始
     *
     * @param clientPlatform 客户端平台
     * @param manual         是否手动登出
     */
    public void recordTimePeriodStart(ClientPlatformDo clientPlatform, Boolean manual) {
        logger.info("记录客户端平台[{}:{}]补发时间段开始", clientPlatform.getUniqueKey(), clientPlatform.getCurrentHostname());
        boolean needInsert = true;
        for (ReissueTimePeriodPo timePeriod : reissueTimePeriodDao.selectLastNotEndReissueTimePeriod(clientPlatform.getId())) {
            if (StrUtil.isBlank(timePeriod.getHostname()) && timePeriod.getStartTime().getTime() < System.currentTimeMillis()) {
                // 存在主机名为空则是非手动登出，即为断连，多节点只记录一次
                needInsert = false;
                break;
            }
            if (clientPlatform.getCurrentHostname().equalsIgnoreCase(timePeriod.getHostname())) {
                // 同一主机重复记录则跳过
                needInsert = false;
                break;
            }
        }
        if (needInsert) {
            ReissueTimePeriodPo reissueTimePeriodPo = ReissueTimePeriodPo.builder()
                    .clientPlatformId(clientPlatform.getId())
                    .startTime(new Date())
                    .reissueState(REISSUE_STATE_INIT)
                    .build();
            if (manual) {
                reissueTimePeriodPo.setHostname(clientPlatform.getCurrentHostname());
            }
            reissueTimePeriodDao.insertPo(reissueTimePeriodPo);
        }
    }

    /**
     * 记录补发时间段结束
     *
     * @param clientPlatform 客户端平台
     */
    public void recordTimePeriodEnd(ClientPlatformDo clientPlatform) {
        logger.info("记录客户端平台[{}:{}]补发时间段结束", clientPlatform.getUniqueKey(), clientPlatform.getCurrentHostname());
        List<ReissueTimePeriodPo> reissueTimePeriodList = reissueTimePeriodDao.selectLastNotEndReissueTimePeriod(clientPlatform.getId());
        if (reissueTimePeriodList.isEmpty()) {
            return;
        }
        boolean isMatch = false;
        for (ReissueTimePeriodPo reissueTimePeriod : reissueTimePeriodList) {
            // 匹配到主机名则认为是手动登出
            if (clientPlatform.getCurrentHostname().equalsIgnoreCase(reissueTimePeriod.getHostname())) {
                reissueTimePeriod.setEndTime(new Date());
                reissueTimePeriodDao.updatePo(reissueTimePeriod);
                isMatch = true;
                break;
            }
        }
        // 未匹配到则大概率是自动断线登出则随机匹配一个
        if (!isMatch) {
            ReissueTimePeriodPo reissueTimePeriod = reissueTimePeriodList.get(0);
            reissueTimePeriod.setEndTime(new Date());
            reissueTimePeriodDao.updatePo(reissueTimePeriod);
        }
    }

    /**
     * 开始补发消息
     *
     * @param clientPlatform 客户端平台
     */
    @Async
    public void startReissueMessage(ClientPlatformDo clientPlatform) {
        getReadyReissueTimePeriod(clientPlatform).ifPresent(timePeriod -> {
            logger.info("客户端平台[{}:{}]开始补发[{}~{}]消息", clientPlatform.getUniqueKey(), clientPlatform.getCurrentHostname(),
                    timePeriod.getStartTime().getTime(), timePeriod.getEndTime().getTime());
            Date startTime = ObjUtil.isNotNull(timePeriod.getReissueTime()) ? timePeriod.getReissueTime() : timePeriod.getStartTime();
            while (startTime.before(timePeriod.getEndTime())) {
                Date endTime = new Date(startTime.getTime() + reissueIntervalMinutes * 60 * 1000);
                if (endTime.after(timePeriod.getEndTime())) {
                    endTime = timePeriod.getEndTime();
                }
                reissueMessages(timePeriod, startTime, endTime);
                startTime = endTime;
            }
            timePeriod.setReissueTime(timePeriod.getEndTime());
            timePeriod.setReissueState(REISSUE_STATE_FINISHED);
            reissueTimePeriodDao.updatePo(timePeriod);
            startReissueMessage(clientPlatform);
        });
    }

    /**
     * 获取待补发时间段
     *
     * @param clientPlatform 客户端平台
     * @return 待补发时间段
     */
    private Optional<ReissueTimePeriodPo> getReadyReissueTimePeriod(ClientPlatformDo clientPlatform) {
        List<ReissueTimePeriodPo> timePeriodList = reissueTimePeriodDao.selectLastReadyReissueTimePeriod(clientPlatform.getId());
        ReissueTimePeriodPo timePeriod = null;
        for (ReissueTimePeriodPo reissueTimePeriod : timePeriodList) {
            if (ObjUtil.isNull(timePeriod)) {
                // 默认以最早的未补发消息开始补发
                timePeriod = reissueTimePeriod;
            }
            if (clientPlatform.getCurrentHostname().equalsIgnoreCase(reissueTimePeriod.getHostname())) {
                // 如果匹配到对应主机名，则以该主机名开始补发
                timePeriod = reissueTimePeriod;
                break;
            }
        }
        return Optional.ofNullable(timePeriod);
    }

    /**
     * 补发时间段内消息
     *
     * @param timePeriod 补发时间段
     * @param startTime  开始时间
     * @param endTime    结束时间
     */
    private void reissueMessages(ReissueTimePeriodPo timePeriod, Date startTime, Date endTime) {
        vehicleGbMessageDao.selectByStartAndEndTime(startTime, endTime).forEach(message -> reissueMessage(message, timePeriod.getHostname()));
        if (REISSUE_STATE_INIT == timePeriod.getReissueState()) {
            timePeriod.setReissueState(REISSUE_STATE_REISSUING);
        }
        timePeriod.setReissueTime(startTime);
        reissueTimePeriodDao.updatePo(timePeriod);
    }

    /**
     * 补发消息
     *
     * @param message  国标消息
     * @param hostname 主机名
     */
    private void reissueMessage(VehicleGbMessagePo message, String hostname) {
        GbUtil.parseMessage(HexUtil.decodeHex(message.getMessageData())).ifPresent(gbMessage -> {
            gbMessage.getHeader().setCommandFlag(GbCommandFlag.REISSUE_REPORT);
            gbMessage.calculateCheckCode();
            gbReissueDataProducer.send(message.getVin(), gbMessage, hostname);
        });
    }

    /**
     * 订阅客户端平台状态事件
     *
     * @param event 客户端平台状态事件
     */
    @EventListener
    public void onClientPlatformStateEvent(ClientPlatformStateEvent event) {
        ClientPlatformDo clientPlatform = event.getClientPlatform();
        switch (event.getState()) {
            case CONNECT_FAILURE -> recordTimePeriodStart(clientPlatform, false);
            case LOGIN_SUCCESS -> {
                recordTimePeriodEnd(clientPlatform);
                startReissueMessage(clientPlatform);
            }
            case LOGOUT_SUCCESS -> recordTimePeriodStart(clientPlatform, true);
        }
    }

}
