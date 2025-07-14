package net.hwyz.iov.cloud.tsp.rsms.service.facade.mpt;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.framework.audit.annotation.Log;
import net.hwyz.iov.cloud.framework.audit.enums.BusinessType;
import net.hwyz.iov.cloud.framework.common.util.StrUtil;
import net.hwyz.iov.cloud.framework.common.web.controller.BaseController;
import net.hwyz.iov.cloud.framework.common.web.domain.AjaxResult;
import net.hwyz.iov.cloud.framework.common.web.page.TableDataInfo;
import net.hwyz.iov.cloud.framework.security.annotation.RequiresPermissions;
import net.hwyz.iov.cloud.framework.security.util.SecurityUtils;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.ServerPlatformMpt;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.ClientPlatformCmd;
import net.hwyz.iov.cloud.tsp.rsms.api.feign.mpt.ServerPlatformMptApi;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.ClientPlatformAppService;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.RegisteredVehicleAppService;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.ServerPlatformAppService;
import net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler.ServerPlatformMptAssembler;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.msg.ClientPlatformCmdProducer;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.ClientPlatformPo;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.ServerPlatformPo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 服务端平台相关管理接口实现类
 *
 * @author hwyz_leo
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/mpt/serverPlatform")
public class ServerPlatformMptController extends BaseController implements ServerPlatformMptApi {

    private final ServerPlatformAppService serverPlatformAppService;
    private final ClientPlatformAppService clientPlatformAppService;
    private final ClientPlatformCmdProducer clientPlatformCmdProducer;
    private final RegisteredVehicleAppService registeredVehicleAppService;

    /**
     * 分页查询服务端平台
     *
     * @param serverPlatform 服务端平台
     * @return 服务端平台列表
     */
    @RequiresPermissions("iov:rsms:serverPlatform:list")
    @Override
    @GetMapping(value = "/list")
    public TableDataInfo list(ServerPlatformMpt serverPlatform) {
        logger.info("管理后台用户[{}]分页查询服务端平台", SecurityUtils.getUsername());
        startPage();
        List<ServerPlatformPo> serverPlatformPoList = serverPlatformAppService.search(serverPlatform.getCode(), serverPlatform.getName(),
                serverPlatform.getType(), getBeginTime(serverPlatform), getEndTime(serverPlatform));
        List<ServerPlatformMpt> serverPlatformMptList = ServerPlatformMptAssembler.INSTANCE.fromPoList(serverPlatformPoList);
        serverPlatformMptList.forEach(serverPlatformMpt -> {
            serverPlatformMpt.setVehicleCount(registeredVehicleAppService.search(null, serverPlatformMpt.getCode(), null, null).size());
        });
        return getDataTable(serverPlatformPoList, serverPlatformMptList);
    }

    /**
     * 获取所有服务端平台
     *
     * @return 服务端平台列表
     */
    @RequiresPermissions("iov:rsms:serverPlatform:list")
    @Override
    @GetMapping(value = "/listAll")
    public List<ServerPlatformMpt> listAll() {
        logger.info("管理后台用户[{}]获取所有服务端平台", SecurityUtils.getUsername());
        List<ServerPlatformPo> serverPlatformPoList = serverPlatformAppService.search(null, null, null, null, null);
        return ServerPlatformMptAssembler.INSTANCE.fromPoList(serverPlatformPoList);
    }

    /**
     * 导出服务端平台
     *
     * @param response       响应
     * @param serverPlatform 服务端平台
     */
    @Log(title = "服务端平台管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("iov:rsms:serverPlatform:export")
    @Override
    @PostMapping("/export")
    public void export(HttpServletResponse response, ServerPlatformMpt serverPlatform) {
        logger.info("管理后台用户[{}]导出服务端平台", SecurityUtils.getUsername());
    }

    /**
     * 根据服务端平台ID获取服务端平台
     *
     * @param serverPlatformId 服务端平台ID
     * @return 服务端平台
     */
    @RequiresPermissions("iov:rsms:serverPlatform:query")
    @Override
    @GetMapping(value = "/{serverPlatformId}")
    public AjaxResult getInfo(@PathVariable Long serverPlatformId) {
        logger.info("管理后台用户[{}]根据服务端平台ID[{}]获取服务端平台", SecurityUtils.getUsername(), serverPlatformId);
        Optional<ServerPlatformPo> serverPlatformOptional = serverPlatformAppService.getServerPlatformById(serverPlatformId);
        if (serverPlatformOptional.isEmpty()) {
            return error(StrUtil.format("服务端平台[{}]不存在", serverPlatformId));
        }
        return success(ServerPlatformMptAssembler.INSTANCE.fromPo(serverPlatformOptional.get()));
    }

    /**
     * 新增服务端平台
     *
     * @param serverPlatform 服务端平台
     * @return 结果
     */
    @Log(title = "服务端平台管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("iov:rsms:serverPlatform:add")
    @Override
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ServerPlatformMpt serverPlatform) {
        logger.info("管理后台用户[{}]新增服务端平台[{}]", SecurityUtils.getUsername(), serverPlatform.getCode());
        if (!serverPlatformAppService.checkCodeUnique(serverPlatform.getId(), serverPlatform.getCode())) {
            return error(StrUtil.format("新增服务端平台[{}]失败，服务端平台代码已存在", serverPlatform.getCode()));
        }
        ServerPlatformPo serverPlatformPo = ServerPlatformMptAssembler.INSTANCE.toPo(serverPlatform);
        serverPlatformPo.setCreateBy(SecurityUtils.getUserId().toString());
        return toAjax(serverPlatformAppService.createServerPlatform(serverPlatformPo));
    }

    /**
     * 修改保存服务端平台
     *
     * @param serverPlatform 服务端平台
     * @return 结果
     */
    @Log(title = "服务端平台管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("iov:rsms:serverPlatform:edit")
    @Override
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ServerPlatformMpt serverPlatform) {
        logger.info("管理后台用户[{}]修改保存服务端平台[{}]", SecurityUtils.getUsername(), serverPlatform.getCode());
        if (!serverPlatformAppService.checkCodeUnique(serverPlatform.getId(), serverPlatform.getCode())) {
            return error(StrUtil.format("修改保存服务端平台[{}]失败，服务端平台代码已存在", serverPlatform.getCode()));
        }
        ServerPlatformPo serverPlatformPo = ServerPlatformMptAssembler.INSTANCE.toPo(serverPlatform);
        serverPlatformPo.setModifyBy(SecurityUtils.getUserId().toString());
        return toAjax(serverPlatformAppService.modifyServerPlatform(serverPlatformPo));
    }

    /**
     * 删除服务端平台
     *
     * @param serverPlatformIds 服务端平台ID数组
     * @return 结果
     */
    @Log(title = "服务端平台管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("iov:rsms:serverPlatform:remove")
    @Override
    @DeleteMapping("/{serverPlatformIds}")
    public AjaxResult remove(@PathVariable Long[] serverPlatformIds) {
        logger.info("管理后台用户[{}]删除服务端平台[{}]", SecurityUtils.getUsername(), serverPlatformIds);
        return toAjax(serverPlatformAppService.deleteServerPlatformByIds(serverPlatformIds));
    }

    /**
     * 同步平台信息
     *
     * @param serverPlatformId 服务端平台ID
     * @return 结果
     */
    @RequiresPermissions("iov:rsms:serverPlatform:syncPlatform")
    @Override
    @PostMapping("/{serverPlatformId}/action/syncPlatform")
    public AjaxResult syncPlatform(@PathVariable Long serverPlatformId) {
        logger.info("管理后台用户[{}]同步服务端平台[{}]平台信息", SecurityUtils.getUsername(), serverPlatformId);
        Optional<ServerPlatformPo> serverPlatformOptional = serverPlatformAppService.getServerPlatformById(serverPlatformId);
        if (serverPlatformOptional.isEmpty()) {
            return error(StrUtil.format("服务端平台[{}]不存在", serverPlatformId));
        }
        List<ClientPlatformPo> clientPlatformList = clientPlatformAppService.listByServerPlatformCode(serverPlatformOptional.get().getCode());
        clientPlatformList.forEach(clientPlatformPo -> clientPlatformCmdProducer.send(clientPlatformPo.getId(), ClientPlatformCmd.SYNC_PLATFORM));
        return toAjax(clientPlatformList.size());
    }

    /**
     * 同步已注册车辆
     *
     * @param serverPlatformId 服务端平台ID
     * @return 结果
     */
    @RequiresPermissions("iov:rsms:serverPlatform:syncVehicle")
    @Override
    @PostMapping("/{serverPlatformId}/action/syncVehicle")
    public AjaxResult syncVehicle(@PathVariable Long serverPlatformId) {
        logger.info("管理后台用户[{}]同步服务端平台[{}]已注册车辆", SecurityUtils.getUsername(), serverPlatformId);
        Optional<ServerPlatformPo> serverPlatformOptional = serverPlatformAppService.getServerPlatformById(serverPlatformId);
        if (serverPlatformOptional.isEmpty()) {
            return error(StrUtil.format("服务端平台[{}]不存在", serverPlatformId));
        }
        List<ClientPlatformPo> clientPlatformList = clientPlatformAppService.listByServerPlatformCode(serverPlatformOptional.get().getCode());
        clientPlatformList.forEach(clientPlatformPo -> clientPlatformCmdProducer.send(clientPlatformPo.getId(), ClientPlatformCmd.SYNC_VEHICLE));
        return toAjax(clientPlatformList.size());
    }
}
