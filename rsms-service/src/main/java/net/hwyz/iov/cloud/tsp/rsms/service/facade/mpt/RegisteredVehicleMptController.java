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
import net.hwyz.iov.cloud.tsp.rsms.api.contract.RegisteredVehicleMpt;
import net.hwyz.iov.cloud.tsp.rsms.api.feign.mpt.RegisteredVehicleMptApi;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.RegisteredVehicleAppService;
import net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler.RegisteredVehicleMptAssembler;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.RegisteredVehiclePo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 服务端平台已注册车辆相关管理接口实现类
 *
 * @author hwyz_leo
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/mpt/registeredVehicle")
public class RegisteredVehicleMptController extends BaseController implements RegisteredVehicleMptApi {

    private final RegisteredVehicleAppService registeredVehicleAppService;

    /**
     * 分页查询已注册车辆
     *
     * @param registeredVehicle 已注册车辆
     * @return 已注册车辆列表
     */
    @RequiresPermissions("iov:rsms:registeredVehicle:list")
    @Override
    @GetMapping(value = "/list")
    public TableDataInfo list(RegisteredVehicleMpt registeredVehicle) {
        logger.info("管理后台用户[{}]分页查询已注册车辆", SecurityUtils.getUsername());
        startPage();
        List<RegisteredVehiclePo> registeredVehiclePoList = registeredVehicleAppService.search(registeredVehicle.getVin(),
                registeredVehicle.getReportState(), registeredVehicle.getServerPlatformCode(), getBeginTime(registeredVehicle),
                getEndTime(registeredVehicle));
        List<RegisteredVehicleMpt> registeredVehicleMptList = RegisteredVehicleMptAssembler.INSTANCE.fromPoList(registeredVehiclePoList);
        return getDataTable(registeredVehiclePoList, registeredVehicleMptList);
    }

    /**
     * 导出已注册车辆
     *
     * @param response          响应
     * @param registeredVehicle 已注册车辆
     */
    @Log(title = "已注册车辆管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("iov:rsms:registeredVehicle:export")
    @Override
    @PostMapping("/export")
    public void export(HttpServletResponse response, RegisteredVehicleMpt registeredVehicle) {
        logger.info("管理后台用户[{}]导出已注册车辆", SecurityUtils.getUsername());
    }

    /**
     * 根据已注册车辆ID获取已注册车辆
     *
     * @param registeredVehicleId 已注册车辆ID
     * @return 已注册车辆
     */
    @RequiresPermissions("iov:rsms:registeredVehicle:query")
    @Override
    @GetMapping(value = "/{registeredVehicleId}")
    public AjaxResult getInfo(@PathVariable Long registeredVehicleId) {
        logger.info("管理后台用户[{}]根据已注册车辆ID[{}]获取已注册车辆", SecurityUtils.getUsername(), registeredVehicleId);
        RegisteredVehiclePo registeredVehiclePo = registeredVehicleAppService.getRegisteredVehicleById(registeredVehicleId);
        return success(RegisteredVehicleMptAssembler.INSTANCE.fromPo(registeredVehiclePo));
    }

    /**
     * 新增已注册车辆
     *
     * @param registeredVehicle 已注册车辆
     * @return 结果
     */
    @Log(title = "已注册车辆管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("iov:rsms:registeredVehicle:add")
    @Override
    @PostMapping
    public AjaxResult add(@Validated @RequestBody RegisteredVehicleMpt registeredVehicle) {
        logger.info("管理后台用户[{}]新增平台[{}]已注册车辆[{}]", SecurityUtils.getUsername(), registeredVehicle.getServerPlatformCode(), registeredVehicle.getVin());
        if (!registeredVehicleAppService.checkCodeUnique(registeredVehicle.getId(), registeredVehicle.getServerPlatformCode(), registeredVehicle.getVin())) {
            return error("新增平台'" + registeredVehicle.getServerPlatformCode() + "'已注册车辆'" + registeredVehicle.getVin() + "'失败，该平台下车辆已存在");
        }
        RegisteredVehiclePo registeredVehiclePo = RegisteredVehicleMptAssembler.INSTANCE.toPo(registeredVehicle);
        registeredVehiclePo.setCreateBy(SecurityUtils.getUserId().toString());
        return toAjax(registeredVehicleAppService.createRegisteredVehicle(registeredVehiclePo));
    }

    /**
     * 修改保存已注册车辆
     *
     * @param registeredVehicle 已注册车辆
     * @return 结果
     */
    @Log(title = "已注册车辆管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("iov:rsms:registeredVehicle:edit")
    @Override
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody RegisteredVehicleMpt registeredVehicle) {
        logger.info("管理后台用户[{}]修改保存平台[{}]已注册车辆[{}]", SecurityUtils.getUsername(), registeredVehicle.getServerPlatformCode(), registeredVehicle.getVin());
        if (!registeredVehicleAppService.checkCodeUnique(registeredVehicle.getId(), registeredVehicle.getServerPlatformCode(), registeredVehicle.getVin())) {
            return error("修改保存平台'" + registeredVehicle.getServerPlatformCode() + "'已注册车辆'" + registeredVehicle.getVin() + "'失败，该平台下车辆已存在");
        }
        RegisteredVehiclePo registeredVehiclePo = RegisteredVehicleMptAssembler.INSTANCE.toPo(registeredVehicle);
        registeredVehiclePo.setModifyBy(SecurityUtils.getUserId().toString());
        return toAjax(registeredVehicleAppService.modifyRegisteredVehicle(registeredVehiclePo));
    }

    /**
     * 删除已注册车辆
     *
     * @param registeredVehicleIds 已注册车辆ID数组
     * @return 结果
     */
    @Log(title = "已注册车辆管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("iov:rsms:registeredVehicle:remove")
    @Override
    @DeleteMapping("/{registeredVehicleIds}")
    public AjaxResult remove(@PathVariable Long[] registeredVehicleIds) {
        logger.info("管理后台用户[{}]删除服务端平台[{}]", SecurityUtils.getUsername(), registeredVehicleIds);
        return toAjax(registeredVehicleAppService.deleteRegisteredVehicleByIds(registeredVehicleIds));
    }

}
