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
import net.hwyz.iov.cloud.tsp.rsms.api.contract.ClientPlatformAccountMpt;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.ClientPlatformLoginHistoryMpt;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.ClientPlatformMpt;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.ClientPlatformCmd;
import net.hwyz.iov.cloud.tsp.rsms.api.feign.mpt.ClientPlatformMptApi;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.ClientPlatformAppService;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.ClientPlatformLoginHistoryAppService;
import net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler.ClientPlatformAccountMptAssembler;
import net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler.ClientPlatformLoginHistoryMptAssembler;
import net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler.ClientPlatformMptAssembler;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.cache.CacheService;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.msg.ClientPlatformCmdProducer;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.ClientPlatformAccountPo;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.ClientPlatformLoginHistoryPo;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.ClientPlatformPo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 客户端平台相关管理接口实现类
 *
 * @author hwyz_leo
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/mpt/clientPlatform")
public class ClientPlatformMptController extends BaseController implements ClientPlatformMptApi {

    private final CacheService cacheService;
    private final ClientPlatformAppService clientPlatformAppService;
    private final ClientPlatformCmdProducer clientPlatformCmdProducer;
    private final ClientPlatformLoginHistoryAppService clientPlatformLoginHistoryAppService;

    /**
     * 分页查询客户端平台
     *
     * @param clientPlatform 客户端平台
     * @return 客户端平台列表
     */
    @RequiresPermissions("iov:rsms:clientPlatform:list")
    @Override
    @GetMapping(value = "/list")
    public TableDataInfo list(ClientPlatformMpt clientPlatform) {
        logger.info("管理后台用户[{}]分页查询客户端平台", SecurityUtils.getUsername());
        startPage();
        List<ClientPlatformPo> clientPlatformPoList = clientPlatformAppService.search(clientPlatform.getUniqueCode(),
                clientPlatform.getServerPlatformCode(), getBeginTime(clientPlatform), getEndTime(clientPlatform));
        List<ClientPlatformMpt> clientPlatformMptList = ClientPlatformMptAssembler.INSTANCE.fromPoList(clientPlatformPoList);
        clientPlatformMptList.forEach(clientPlatformMpt -> {
            String uniqueKey = clientPlatformMpt.getServerPlatformCode() + "-" + clientPlatformMpt.getUniqueCode();
            Map<String, Boolean> connectState = cacheService.getClientPlatformConnectState(uniqueKey);
            clientPlatformMpt.setConnectState(connectState);
            clientPlatformMpt.setConnectStat(connectState.values().stream().filter(Boolean::booleanValue).toList().size() + " / " + connectState.size());
            Map<String, Boolean> loginState = cacheService.getClientPlatformLoginState(uniqueKey);
            clientPlatformMpt.setLoginState(loginState);
            clientPlatformMpt.setLoginStat(loginState.values().stream().filter(Boolean::booleanValue).toList().size() + " / " + loginState.size());
        });
        return getDataTable(clientPlatformPoList, clientPlatformMptList);
    }

    /**
     * 列出客户端平台下账号
     *
     * @param clientPlatformId 客户端平台ID
     * @return 客户端平台账号列表
     */
    @RequiresPermissions("iov:rsms:clientPlatform:listAccount")
    @Override
    @GetMapping(value = "/{clientPlatformId}/account")
    public List<ClientPlatformAccountMpt> listAccount(@PathVariable Long clientPlatformId) {
        logger.info("管理后台用户[{}]列出客户端平台[{}]下账号", SecurityUtils.getUsername(), clientPlatformId);
        List<ClientPlatformAccountPo> clientPlatformAccountPoList = clientPlatformAppService.listAccount(clientPlatformId);
        return ClientPlatformAccountMptAssembler.INSTANCE.fromPoList(clientPlatformAccountPoList);
    }

    /**
     * 分页查询客户端平台登录历史
     *
     * @param clientPlatformId 客户端平台ID
     * @return 客户端平台登录历史列表
     */
    @RequiresPermissions("iov:rsms:clientPlatform:listLoginHistory")
    @Override
    @GetMapping(value = "/{clientPlatformId}/loginHistory")
    public TableDataInfo listLoginHistory(@PathVariable Long clientPlatformId) {
        logger.info("管理后台用户[{}]分页查询客户端平台登录历史", SecurityUtils.getUsername());
        startPage();
        List<ClientPlatformLoginHistoryPo> loginHistoryPoList = clientPlatformLoginHistoryAppService.listLoginHistory(clientPlatformId);
        List<ClientPlatformLoginHistoryMpt> clientPlatformLoginHistoryMptList = ClientPlatformLoginHistoryMptAssembler.INSTANCE.fromPoList(loginHistoryPoList);
        return getDataTable(loginHistoryPoList, clientPlatformLoginHistoryMptList);
    }

    /**
     * 导出客户端平台
     *
     * @param response       响应
     * @param clientPlatform 客户端平台
     */
    @Log(title = "客户端平台管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("iov:rsms:clientPlatform:export")
    @Override
    @PostMapping("/export")
    public void export(HttpServletResponse response, ClientPlatformMpt clientPlatform) {
        logger.info("管理后台用户[{}]导出客户端平台", SecurityUtils.getUsername());
    }

    /**
     * 根据客户端平台ID获取客户端平台
     *
     * @param clientPlatformId 客户端平台ID
     * @return 客户端平台
     */
    @RequiresPermissions("iov:rsms:clientPlatform:query")
    @Override
    @GetMapping(value = "/{clientPlatformId}")
    public AjaxResult getInfo(@PathVariable Long clientPlatformId) {
        logger.info("管理后台用户[{}]根据客户端平台ID[{}]获取客户端平台", SecurityUtils.getUsername(), clientPlatformId);
        ClientPlatformPo clientPlatformPo = clientPlatformAppService.getClientPlatformById(clientPlatformId);
        return success(ClientPlatformMptAssembler.INSTANCE.fromPo(clientPlatformPo));
    }

    /**
     * 新增客户端平台
     *
     * @param clientPlatform 客户端平台
     * @return 结果
     */
    @Log(title = "客户端平台管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("iov:rsms:clientPlatform:add")
    @Override
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ClientPlatformMpt clientPlatform) {
        logger.info("管理后台用户[{}]新增客户端平台[{}]", SecurityUtils.getUsername(), clientPlatform.getUniqueCode());
        ClientPlatformPo clientPlatformPo = ClientPlatformMptAssembler.INSTANCE.toPo(clientPlatform);
        clientPlatformPo.setCreateBy(SecurityUtils.getUserId().toString());
        return toAjax(clientPlatformAppService.createClientPlatform(clientPlatformPo));
    }

    /**
     * 新增客户端平台账号
     *
     * @param clientPlatformAccount 客户端平台账号
     * @return 结果
     */
    @Log(title = "客户端平台账号管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("iov:rsms:clientPlatform:addAccount")
    @Override
    @PostMapping("/{clientPlatformId}")
    public AjaxResult addAccount(@PathVariable Long clientPlatformId, @Validated @RequestBody ClientPlatformAccountMpt clientPlatformAccount) {
        logger.info("管理后台用户[{}]新增客户端平台[{}]账号[{}]", SecurityUtils.getUsername(), clientPlatformId, clientPlatformAccount.getUsername());
        ClientPlatformAccountPo clientPlatformAccountPo = ClientPlatformAccountMptAssembler.INSTANCE.toPo(clientPlatformAccount);
        clientPlatformAccountPo.setCreateBy(SecurityUtils.getUserId().toString());
        return toAjax(clientPlatformAppService.createClientPlatformAccount(clientPlatformAccountPo));
    }

    /**
     * 修改保存客户端平台
     *
     * @param clientPlatform 客户端平台
     * @return 结果
     */
    @Log(title = "客户端平台管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("iov:rsms:clientPlatform:edit")
    @Override
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ClientPlatformMpt clientPlatform) {
        logger.info("管理后台用户[{}]修改保存客户端平台[{}]", SecurityUtils.getUsername(), clientPlatform.getUniqueCode());
        ClientPlatformPo clientPlatformPo = ClientPlatformMptAssembler.INSTANCE.toPo(clientPlatform);
        clientPlatformPo.setModifyBy(SecurityUtils.getUserId().toString());
        return toAjax(clientPlatformAppService.modifyClientPlatform(clientPlatformPo));
    }

    /**
     * 修改保存客户端平台账号
     *
     * @param clientPlatformId      客户端平台ID
     * @param clientPlatformAccount 客户端平台账号
     * @return 结果
     */
    @Log(title = "客户端平台账号管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("iov:rsms:clientPlatform:editAccount")
    @Override
    @PutMapping("/{clientPlatformId}")
    public AjaxResult editAccount(@PathVariable Long clientPlatformId, @Validated @RequestBody ClientPlatformAccountMpt clientPlatformAccount) {
        logger.info("管理后台用户[{}]修改保存客户端平台[{}]账号[{}]", SecurityUtils.getUsername(), clientPlatformId, clientPlatformAccount.getUsername());
        ClientPlatformAccountPo clientPlatformAccountPo = ClientPlatformAccountMptAssembler.INSTANCE.toPo(clientPlatformAccount);
        clientPlatformAccountPo.setModifyBy(SecurityUtils.getUserId().toString());
        return toAjax(clientPlatformAppService.modifyClientPlatformAccount(clientPlatformAccountPo));
    }

    /**
     * 删除客户端平台
     *
     * @param clientPlatformIds 客户端平台ID数组
     * @return 结果
     */
    @Log(title = "客户端平台管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("iov:rsms:clientPlatform:remove")
    @Override
    @DeleteMapping("/{clientPlatformIds}")
    public AjaxResult remove(@PathVariable Long[] clientPlatformIds) {
        logger.info("管理后台用户[{}]删除客户端平台[{}]", SecurityUtils.getUsername(), clientPlatformIds);
        return toAjax(clientPlatformAppService.deleteClientPlatformByIds(clientPlatformIds));
    }

    /**
     * 删除客户端平台账号
     *
     * @param clientPlatformId         客户端平台ID
     * @param clientPlatformAccountIds 客户端平台账号ID数组
     * @return 结果
     */
    @Log(title = "客户端平台账号管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("iov:rsms:clientPlatform:removeAccount")
    @Override
    @DeleteMapping("/{clientPlatformId}/{clientPlatformAccountIds}")
    public AjaxResult removeAccount(@PathVariable Long clientPlatformId, @PathVariable Long[] clientPlatformAccountIds) {
        logger.info("管理后台用户[{}]删除客户端平台[{}]下账号[{}]", SecurityUtils.getUsername(), clientPlatformId, clientPlatformAccountIds);
        return toAjax(clientPlatformAppService.deleteClientPlatformAccountByIds(clientPlatformAccountIds));
    }

    /**
     * 同步平台信息
     *
     * @param clientPlatformId 客户端平台ID
     * @return 结果
     */
    @RequiresPermissions("iov:rsms:clientPlatform:syncPlatform")
    @Override
    @PostMapping(value = "/{clientPlatformId}/action/syncPlatform")
    public AjaxResult syncPlatform(@PathVariable Long clientPlatformId) {
        logger.info("管理后台用户[{}]同步客户端平台[{}]平台信息", SecurityUtils.getUsername(), clientPlatformId);
        clientPlatformCmdProducer.send(clientPlatformId, ClientPlatformCmd.SYNC_PLATFORM);
        return toAjax(1);
    }

    /**
     * 客户端平台登录
     *
     * @param clientPlatformId 客户端平台ID
     * @param hostname         主机名
     * @return 结果
     */
    @Log(title = "客户端平台管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("iov:rsms:clientPlatform:login")
    @Override
    @PostMapping("/{clientPlatformId}/action/login")
    public AjaxResult login(@PathVariable Long clientPlatformId, @RequestParam String hostname) {
        logger.info("管理后台用户[{}]操作客户端平台[{}:{}]登录", SecurityUtils.getUsername(), clientPlatformId, hostname);
        clientPlatformCmdProducer.send(clientPlatformId, hostname, ClientPlatformCmd.LOGIN);
        return toAjax(1);
    }

    /**
     * 客户端平台登出
     *
     * @param clientPlatformId 客户端平台ID
     * @param hostname         主机名
     * @return 结果
     */
    @Log(title = "客户端平台管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("iov:rsms:clientPlatform:logout")
    @Override
    @PostMapping("/{clientPlatformId}/action/logout")
    public AjaxResult logout(@PathVariable Long clientPlatformId, @RequestParam String hostname) {
        logger.info("管理后台用户[{}]操作客户端平台[{}:{}]登出", SecurityUtils.getUsername(), clientPlatformId, hostname);
        clientPlatformCmdProducer.send(clientPlatformId, hostname, ClientPlatformCmd.LOGOUT);
        return toAjax(1);
    }
}
