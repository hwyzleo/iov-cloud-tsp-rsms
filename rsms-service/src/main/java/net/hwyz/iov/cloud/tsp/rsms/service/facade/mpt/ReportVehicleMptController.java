package net.hwyz.iov.cloud.tsp.rsms.service.facade.mpt;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.framework.audit.annotation.Log;
import net.hwyz.iov.cloud.framework.audit.enums.BusinessType;
import net.hwyz.iov.cloud.framework.common.web.controller.BaseController;
import net.hwyz.iov.cloud.framework.common.web.domain.AjaxResult;
import net.hwyz.iov.cloud.framework.common.web.page.TableDataInfo;
import net.hwyz.iov.cloud.framework.security.annotation.RequiresPermissions;
import net.hwyz.iov.cloud.framework.security.util.SecurityUtils;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.ReportVehicleMpt;
import net.hwyz.iov.cloud.tsp.rsms.api.feign.mpt.ReportVehicleMptApi;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.ClientPlatformAppService;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.ReportVehicleAppService;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.ServerPlatformAppService;
import net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler.ReportVehicleMptAssembler;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.ReportVehiclePo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 上报车辆相关管理接口实现类
 *
 * @author hwyz_leo
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/mpt/reportVehicle")
public class ReportVehicleMptController extends BaseController implements ReportVehicleMptApi {

    private final ReportVehicleAppService reportVehicleAppService;
    private final ClientPlatformAppService clientPlatformAppService;
    private final ServerPlatformAppService serverPlatformAppService;

    /**
     * 分页查询上报车辆
     *
     * @param reportVehicle 上报车辆
     * @return 上报车辆列表
     */
    @RequiresPermissions("iov:rsms:reportVehicle:list")
    @Override
    @GetMapping(value = "/list")
    public TableDataInfo list(ReportVehicleMpt reportVehicle) {
        logger.info("管理后台用户[{}]分页查询上报车辆", SecurityUtils.getUsername());
        startPage();
        List<ReportVehiclePo> reportVehiclePoList = reportVehicleAppService.search(reportVehicle.getVin(),
                reportVehicle.getReportState(), getBeginTime(reportVehicle), getEndTime(reportVehicle));
        List<ReportVehicleMpt> reportVehicleMptList = ReportVehicleMptAssembler.INSTANCE.fromPoList(reportVehiclePoList);
        return getDataTable(reportVehiclePoList, reportVehicleMptList);
    }

    /**
     * 导出上报车辆
     *
     * @param response      响应
     * @param reportVehicle 上报车辆
     */
    @Log(title = "上报车辆管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("iov:rsms:reportVehicle:export")
    @Override
    @PostMapping("/export")
    public void export(HttpServletResponse response, ReportVehicleMpt reportVehicle) {
        logger.info("管理后台用户[{}]导出上报车辆", SecurityUtils.getUsername());
    }

    /**
     * 根据上报车辆ID获取上报车辆
     *
     * @param reportVehicleId 上报车辆ID
     * @return 上报车辆
     */
    @RequiresPermissions("iov:rsms:reportVehicle:query")
    @Override
    @GetMapping(value = "/{reportVehicleId}")
    public AjaxResult getInfo(@PathVariable Long reportVehicleId) {
        logger.info("管理后台用户[{}]根据上报车辆ID[{}]获取上报车辆", SecurityUtils.getUsername(), reportVehicleId);
        ReportVehiclePo reportVehiclePo = reportVehicleAppService.getReportVehicleById(reportVehicleId);
        return success(ReportVehicleMptAssembler.INSTANCE.fromPo(reportVehiclePo));
    }

    /**
     * 新增上报车辆
     *
     * @param reportVehicle 上报车辆
     * @return 结果
     */
    @Log(title = "上报车辆管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("iov:rsms:reportVehicle:add")
    @Override
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ReportVehicleMpt reportVehicle) {
        logger.info("管理后台用户[{}]新增上报车辆[{}]", SecurityUtils.getUsername(), reportVehicle.getVin());
        if (!reportVehicleAppService.checkCodeUnique(reportVehicle.getId(), reportVehicle.getVin())) {
            return error("新增上报车辆'" + reportVehicle.getVin() + "'失败，该车辆已存在");
        }
        ReportVehiclePo reportVehiclePo = ReportVehicleMptAssembler.INSTANCE.toPo(reportVehicle);
        reportVehiclePo.setCreateBy(SecurityUtils.getUserId().toString());
        return toAjax(reportVehicleAppService.createReportVehicle(reportVehiclePo));
    }

    /**
     * 修改保存上报车辆
     *
     * @param reportVehicle 上报车辆
     * @return 结果
     */
    @Log(title = "上报车辆管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("iov:rsms:reportVehicle:edit")
    @Override
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ReportVehicleMpt reportVehicle) {
        logger.info("管理后台用户[{}]修改保存上报车辆[{}]", SecurityUtils.getUsername(), reportVehicle.getVin());
        if (!reportVehicleAppService.checkCodeUnique(reportVehicle.getId(), reportVehicle.getVin())) {
            return error("修改保存上报车辆'" + reportVehicle.getVin() + "'失败，该车辆已存在");
        }
        ReportVehiclePo reportVehiclePo = ReportVehicleMptAssembler.INSTANCE.toPo(reportVehicle);
        reportVehiclePo.setModifyBy(SecurityUtils.getUserId().toString());
        return toAjax(reportVehicleAppService.modifyReportVehicle(reportVehiclePo));
    }

    /**
     * 删除上报车辆
     *
     * @param reportVehicleIds 上报车辆ID数组
     * @return 结果
     */
    @Log(title = "上报车辆管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("iov:rsms:reportVehicle:remove")
    @Override
    @DeleteMapping("/{reportVehicleIds}")
    public AjaxResult remove(@PathVariable Long[] reportVehicleIds) {
        logger.info("管理后台用户[{}]删除服务端平台[{}]", SecurityUtils.getUsername(), reportVehicleIds);
        return toAjax(reportVehicleAppService.deleteReportVehicleByIds(reportVehicleIds));
    }

}
