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
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbInspectionReportMpt;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbInspectionReportScene;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbInspectionReportState;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbInspectionReportType;
import net.hwyz.iov.cloud.tsp.rsms.api.feign.mpt.GbInspectionReportMptApi;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.GbInspectionReportAppService;
import net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler.GbInspectionReportMptAssembler;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.GbInspectionReportPo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 国标检测报告相关管理接口实现类
 *
 * @author hwyz_leo
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/mpt/gbInspectionReport")
public class GbInspectionReportMptController extends BaseController implements GbInspectionReportMptApi {

    private final GbInspectionReportAppService gbInspectionReportAppService;

    /**
     * 分页查询国标检测报告
     *
     * @param gbInspectionReport 国标检测报告
     * @return 国标检测报告列表
     */
    @RequiresPermissions("iov:rsms:gbInspectionReport:list")
    @Override
    @GetMapping(value = "/list")
    public TableDataInfo list(GbInspectionReportMpt gbInspectionReport) {
        logger.info("管理后台用户[{}]分页查询国标检测报告", SecurityUtils.getUsername());
        startPage();
        List<GbInspectionReportPo> gbInspectionReportPoList = gbInspectionReportAppService.search(gbInspectionReport.getVehicle(),
                gbInspectionReport.getReportState(), getBeginTime(gbInspectionReport), getEndTime(gbInspectionReport));
        List<GbInspectionReportMpt> gbInspectionReportMptList = GbInspectionReportMptAssembler.INSTANCE.fromPoList(gbInspectionReportPoList);
        return getDataTable(gbInspectionReportPoList, gbInspectionReportMptList);
    }

    /**
     * 获取国标检测报告类型列表
     *
     * @return 国标检测报告类型列表
     */
    @RequiresPermissions("iov:rsms:gbInspectionReport:listGbInspectionReportType")
    @Override
    @GetMapping(value = "/listGbInspectionReportType")
    public AjaxResult listGbInspectionReportType() {
        logger.info("管理后台用户[{}]获取国标检测报告类型列表", SecurityUtils.getUsername());
        List<Map<String, Object>> list = new ArrayList<>();
        Arrays.stream(GbInspectionReportType.values()).forEach(type -> {
            Map<String, Object> map = new HashMap<>();
            map.put("code", type.getCode());
            map.put("name", type.getName());
            list.add(map);
        });
        return success(list);
    }

    /**
     * 获取国标检测报告状态列表
     *
     * @return 国标检测报告状态列表
     */
    @RequiresPermissions("iov:rsms:gbInspectionReport:listGbInspectionReportState")
    @Override
    @GetMapping(value = "/listGbInspectionReportState")
    public AjaxResult listGbInspectionReportState() {
        logger.info("管理后台用户[{}]获取国标检测报告状态列表", SecurityUtils.getUsername());
        List<Map<String, Object>> list = new ArrayList<>();
        Arrays.stream(GbInspectionReportState.values()).forEach(type -> {
            Map<String, Object> map = new HashMap<>();
            map.put("code", type.getCode());
            map.put("name", type.getName());
            list.add(map);
        });
        return success(list);
    }

    /**
     * 获取国标检测报告场景列表
     *
     * @return 国标检测报告场景列表
     */
    @RequiresPermissions("iov:rsms:gbInspectionReport:listGbInspectionReportScene")
    @Override
    @GetMapping(value = "/listGbInspectionReportScene")
    public AjaxResult listGbInspectionReportScene() {
        logger.info("管理后台用户[{}]获取国标检测报告场景列表", SecurityUtils.getUsername());
        List<Map<String, Object>> list = new ArrayList<>();
        Arrays.stream(GbInspectionReportScene.values()).forEach(type -> {
            Map<String, Object> map = new HashMap<>();
            map.put("code", type.getCode());
            map.put("name", type.getName());
            list.add(map);
        });
        return success(list);
    }

    /**
     * 导出国标检测报告
     *
     * @param response           响应
     * @param gbInspectionReport 国标检测报告
     */
    @Log(title = "国标检测报告管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("iov:rsms:gbInspectionReport:export")
    @Override
    @PostMapping("/export")
    public void export(HttpServletResponse response, GbInspectionReportMpt gbInspectionReport) {
        logger.info("管理后台用户[{}]导出国标检测报告", SecurityUtils.getUsername());
    }

    /**
     * 根据国标检测报告ID获取国标检测报告
     *
     * @param gbInspectionReportId 国标检测报告ID
     * @return 国标检测报告
     */
    @RequiresPermissions("iov:rsms:gbInspectionReport:query")
    @Override
    @GetMapping(value = "/{gbInspectionReportId}")
    public AjaxResult getInfo(@PathVariable Long gbInspectionReportId) {
        logger.info("管理后台用户[{}]根据国标检测报告ID[{}]获取国标检测报告", SecurityUtils.getUsername(), gbInspectionReportId);
        GbInspectionReportPo gbInspectionReportPo = gbInspectionReportAppService.getGbInspectionReportById(gbInspectionReportId);
        return success(GbInspectionReportMptAssembler.INSTANCE.fromPo(gbInspectionReportPo));
    }

    /**
     * 新增国标检测报告
     *
     * @param gbInspectionReport 国标检测报告
     * @return 结果
     */
    @Log(title = "国标检测报告管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("iov:rsms:gbInspectionReport:add")
    @Override
    @PostMapping
    public AjaxResult add(@Validated @RequestBody GbInspectionReportMpt gbInspectionReport) {
        logger.info("管理后台用户[{}]新增国标检测报告[{}]", SecurityUtils.getUsername(), gbInspectionReport.getVehicle());
        GbInspectionReportPo gbInspectionReportPo = GbInspectionReportMptAssembler.INSTANCE.toPo(gbInspectionReport);
        gbInspectionReportPo.setCreateBy(SecurityUtils.getUserId().toString());
        int result = gbInspectionReportAppService.createGbInspectionReport(gbInspectionReportPo);
        gbInspectionReportAppService.handleReport(gbInspectionReportPo);
        return toAjax(result);
    }

    /**
     * 修改保存国标检测报告
     *
     * @param gbInspectionReport 国标检测报告
     * @return 结果
     */
    @Log(title = "国标检测报告管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("iov:rsms:gbInspectionReport:edit")
    @Override
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody GbInspectionReportMpt gbInspectionReport) {
        logger.info("管理后台用户[{}]修改保存国标检测报告[{}]", SecurityUtils.getUsername(), gbInspectionReport.getVehicle());
        GbInspectionReportPo gbInspectionReportPo = GbInspectionReportMptAssembler.INSTANCE.toPo(gbInspectionReport);
        gbInspectionReportPo.setModifyBy(SecurityUtils.getUserId().toString());
        return toAjax(gbInspectionReportAppService.modifyGbInspectionReport(gbInspectionReportPo));
    }

    /**
     * 删除国标检测报告
     *
     * @param gbInspectionReportIds 国标检测报告ID数组
     * @return 结果
     */
    @Log(title = "国标检测报告管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("iov:rsms:gbInspectionReport:remove")
    @Override
    @DeleteMapping("/{gbInspectionReportIds}")
    public AjaxResult remove(@PathVariable Long[] gbInspectionReportIds) {
        logger.info("管理后台用户[{}]删除国标检测报告[{}]", SecurityUtils.getUsername(), gbInspectionReportIds);
        return toAjax(gbInspectionReportAppService.deleteGbInspectionReportByIds(gbInspectionReportIds));
    }

}
