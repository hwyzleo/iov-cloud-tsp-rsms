package net.hwyz.iov.cloud.tsp.rsms.service.application.service;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.ObjUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessage;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbInspectionReportState;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbInspectionReportType;
import net.hwyz.iov.cloud.tsp.rsms.api.util.GbUtil;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.handler.*;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao.GbInspectionItemDao;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.dao.GbInspectionReportDao;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.GbInspectionReportPo;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.VehicleGbMessagePo;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 国标检测报告应用服务类
 *
 * @author hwyz_leo
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GbInspectionReportAppService {

    private final ApplicationContext ctx;
    private final GbInspectionItemDao gbInspectionItemDao;
    private final GbInspectionReportDao gbInspectionReportDao;
    private final VehicleGbMessageAppService vehicleGbMessageAppService;

    /**
     * 查询国标检测报告
     *
     * @param vehicle     车型或车架号
     * @param reportState 报告状态
     * @param beginTime   开始时间
     * @param endTime     结束时间
     * @return 上报车辆列表
     */
    public List<GbInspectionReportPo> search(String vehicle, Integer reportState, Date beginTime, Date endTime) {
        Map<String, Object> map = new HashMap<>();
        map.put("vehicle", vehicle);
        map.put("reportState", reportState);
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        return gbInspectionReportDao.selectPoByMap(map);
    }

    /**
     * 根据主键ID获取国标检测报告
     *
     * @param id 主键ID
     * @return 国标检测报告
     */
    public GbInspectionReportPo getGbInspectionReportById(Long id) {
        return gbInspectionReportDao.selectPoById(id);
    }

    /**
     * 新增国标检测报告
     *
     * @param gbInspectionReport 国标检测报告
     * @return 结果
     */
    public int createGbInspectionReport(GbInspectionReportPo gbInspectionReport) {
        gbInspectionReport.setReportState(GbInspectionReportState.PROCESSING.getCode());
        return gbInspectionReportDao.insertPo(gbInspectionReport);
    }

    /**
     * 修改国标检测报告
     *
     * @param gbInspectionReport 国标检测报告
     * @return 结果
     */
    public int modifyGbInspectionReport(GbInspectionReportPo gbInspectionReport) {
        return gbInspectionReportDao.updatePo(gbInspectionReport);
    }

    /**
     * 批量删除国标检测报告
     *
     * @param ids 上报车辆ID数组
     * @return 结果
     */
    public int deleteGbInspectionReportByIds(Long[] ids) {
        return gbInspectionReportDao.batchPhysicalDeletePo(ids);
    }

    /**
     * 处理国标检测报告
     *
     * @param gbInspectionReport 国标检测报告
     */
    @Async
    public void handleReport(GbInspectionReportPo gbInspectionReport) {
        List<GbMessage> gbMessages = findGbMessages(gbInspectionReport);
        if (gbMessages.isEmpty()) {
            gbInspectionReport.setVehicleCount(0L);
            gbInspectionReport.setMessageCount(0L);
            gbInspectionReport.setDataCount(0L);
            gbInspectionReport.setReportState(GbInspectionReportState.COMPLETED.getCode());
            gbInspectionReportDao.updatePo(gbInspectionReport);
            return;
        }
        StandardInspectionHandler standardInspectionHandler = ctx.getBean(StandardInspectionHandler.class);
        IntegrityInspectionHandler integrityInspectionHandler = ctx.getBean(IntegrityInspectionHandler.class);
        AccuracyInspectionHandler accuracyInspectionHandler = ctx.getBean(AccuracyInspectionHandler.class);
        ConsistencyInspectionHandler consistencyInspectionHandler = ctx.getBean(ConsistencyInspectionHandler.class);
        TimelinessInspectionHandler timelinessInspectionHandler = ctx.getBean(TimelinessInspectionHandler.class);
        SummaryInspectionHandler summaryInspectionHandler = ctx.getBean(SummaryInspectionHandler.class);
        gbInspectionReport.setItems(new ArrayList<>());
        standardInspectionHandler.inspect(gbInspectionReport, gbMessages);
        integrityInspectionHandler.inspect(gbInspectionReport, gbMessages);
        accuracyInspectionHandler.inspect(gbInspectionReport, gbMessages);
        consistencyInspectionHandler.inspect(gbInspectionReport, gbMessages);
        timelinessInspectionHandler.inspect(gbInspectionReport, gbMessages);
        summaryInspectionHandler.inspect(gbInspectionReport, gbMessages);
        gbInspectionReport.getItems().forEach(gbInspectionItemDao::insertPo);
        gbInspectionReport.setReportState(GbInspectionReportState.COMPLETED.getCode());
        gbInspectionReportDao.updatePo(gbInspectionReport);
    }

    /**
     * 查找匹配报告的国标消息
     *
     * @param gbInspectionReport 国标检测报告
     * @return 国标消息列表
     */
    private List<GbMessage> findGbMessages(GbInspectionReportPo gbInspectionReport) {
        List<GbMessage> gbMessages = new ArrayList<>();
        List<VehicleGbMessagePo> messages = null;
        if (GbInspectionReportType.VEHICLE.getCode() == gbInspectionReport.getReportType()) {
            messages = vehicleGbMessageAppService.search(gbInspectionReport.getVehicle(), null,
                    gbInspectionReport.getStartTime(), gbInspectionReport.getEndTime());
        }
        if (ObjUtil.isNotNull(messages)) {
            messages.forEach(message -> {
                Optional<GbMessage> gbMessageOptional = GbUtil.parseMessage(HexUtil.decodeHex(message.getMessageData()));
                gbMessageOptional.ifPresent(gbMessages::add);
            });
        }
        return gbMessages;
    }

}
