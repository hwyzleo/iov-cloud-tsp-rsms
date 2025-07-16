package net.hwyz.iov.cloud.tsp.rsms.client.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.client.model.ClientPlatformDo;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.dao.ReissueTimePeriodDao;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.po.ReissueTimePeriodPo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 补发时间段应用服务类
 *
 * @author hwyz_leo
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReissueTimePeriodAppService {

    private final ReissueTimePeriodDao reissueTimePeriodDao;

    /**
     * 补发状态：未补发
     */
    private final static int REISSUE_STATE_INIT = 0;

    /**
     * 记录补发时间段开始
     *
     * @param clientPlatform 客户端平台
     * @param manual         是否手动登出
     */
    public void recordTimePeriodStart(ClientPlatformDo clientPlatform, Boolean manual) {
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

    /**
     * 记录补发时间段结束
     *
     * @param clientPlatform 客户端平台
     */
    public void recordTimePeriodEnd(ClientPlatformDo clientPlatform) {
        List<ReissueTimePeriodPo> reissueTimePeriodList = reissueTimePeriodDao.selectLastInitReissueTimePeriod(clientPlatform.getId());
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

}
