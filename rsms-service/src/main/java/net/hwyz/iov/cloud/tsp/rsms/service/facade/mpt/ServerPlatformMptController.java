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
import net.hwyz.iov.cloud.tsp.rsms.api.contract.ServerPlatformMpt;
import net.hwyz.iov.cloud.tsp.rsms.api.feign.mpt.ServerPlatformMptApi;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.RegisteredVehicleAppService;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.ServerPlatformAppService;
import net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler.ServerPlatformMptAssembler;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.ServerPlatformPo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        ServerPlatformPo serverPlatformPo = serverPlatformAppService.getServerPlatformById(serverPlatformId);
        return success(ServerPlatformMptAssembler.INSTANCE.fromPo(serverPlatformPo));
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
            return error("新增服务端平台'" + serverPlatform.getCode() + "'失败，服务端平台代码已存在");
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
            return error("修改保存服务端平台'" + serverPlatform.getCode() + "'失败，服务端平台代码已存在");
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

}
