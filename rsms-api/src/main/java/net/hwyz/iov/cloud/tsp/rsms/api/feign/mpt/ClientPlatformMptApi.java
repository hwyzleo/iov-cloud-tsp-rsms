package net.hwyz.iov.cloud.tsp.rsms.api.feign.mpt;

import jakarta.servlet.http.HttpServletResponse;
import net.hwyz.iov.cloud.framework.common.web.domain.AjaxResult;
import net.hwyz.iov.cloud.framework.common.web.page.TableDataInfo;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.ClientPlatformMpt;

/**
 * 客户端平台相关管理后台接口
 *
 * @author hwyz_leo
 */
public interface ClientPlatformMptApi {

    /**
     * 分页查询客户端平台
     *
     * @param clientPlatform 客户端平台
     * @return 客户端平台列表
     */
    TableDataInfo list(ClientPlatformMpt clientPlatform);

    /**
     * 分页查询客户端平台登录历史
     *
     * @param clientPlatformId 客户端平台ID
     * @return 客户端平台登录历史列表
     */
    TableDataInfo listLoginHistory(Long clientPlatformId);

    /**
     * 导出客户端平台
     *
     * @param response       响应
     * @param clientPlatform 客户端平台
     */
    void export(HttpServletResponse response, ClientPlatformMpt clientPlatform);

    /**
     * 根据客户端平台ID获取客户端平台
     *
     * @param clientPlatformId 客户端平台ID
     * @return 客户端平台
     */
    AjaxResult getInfo(Long clientPlatformId);

    /**
     * 新增客户端平台
     *
     * @param clientPlatform 客户端平台
     * @return 结果
     */
    AjaxResult add(ClientPlatformMpt clientPlatform);

    /**
     * 修改保存客户端平台
     *
     * @param clientPlatform 客户端平台
     * @return 结果
     */
    AjaxResult edit(ClientPlatformMpt clientPlatform);

    /**
     * 删除客户端平台
     *
     * @param clientPlatformIds 客户端平台ID数组
     * @return 结果
     */
    AjaxResult remove(Long[] clientPlatformIds);

    /**
     * 客户端平台登录
     *
     * @param clientPlatformId 客户端平台ID
     * @param hostname         主机名
     * @return 结果
     */
    AjaxResult login(Long clientPlatformId, String hostname);

    /**
     * 客户端平台登出
     *
     * @param clientPlatformId 客户端平台ID
     * @param hostname         主机名
     * @return 结果
     */
    AjaxResult logout(Long clientPlatformId, String hostname);

}
