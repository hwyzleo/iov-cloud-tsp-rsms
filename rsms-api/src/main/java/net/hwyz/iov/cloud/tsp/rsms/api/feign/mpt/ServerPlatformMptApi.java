package net.hwyz.iov.cloud.tsp.rsms.api.feign.mpt;

import jakarta.servlet.http.HttpServletResponse;
import net.hwyz.iov.cloud.framework.common.web.domain.AjaxResult;
import net.hwyz.iov.cloud.framework.common.web.page.TableDataInfo;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.ServerPlatformMpt;

import java.util.List;

/**
 * 服务端平台相关管理后台接口
 *
 * @author hwyz_leo
 */
public interface ServerPlatformMptApi {

    /**
     * 分页查询服务端平台
     *
     * @param serverPlatform 服务端平台
     * @return 服务端平台列表
     */
    TableDataInfo list(ServerPlatformMpt serverPlatform);

    /**
     * 获取所有服务端平台
     *
     * @return 服务端平台列表
     */
    List<ServerPlatformMpt> listAll();

    /**
     * 导出服务端平台
     *
     * @param response       响应
     * @param serverPlatform 服务端平台
     */
    void export(HttpServletResponse response, ServerPlatformMpt serverPlatform);

    /**
     * 根据服务端平台ID获取服务端平台
     *
     * @param serverPlatformId 服务端平台ID
     * @return 服务端平台
     */
    AjaxResult getInfo(Long serverPlatformId);

    /**
     * 新增服务端平台
     *
     * @param serverPlatform 服务端平台
     * @return 结果
     */
    AjaxResult add(ServerPlatformMpt serverPlatform);

    /**
     * 修改保存服务端平台
     *
     * @param serverPlatform 服务端平台
     * @return 结果
     */
    AjaxResult edit(ServerPlatformMpt serverPlatform);

    /**
     * 删除服务端平台
     *
     * @param serverPlatformIds 服务端平台ID数组
     * @return 结果
     */
    AjaxResult remove(Long[] serverPlatformIds);

    /**
     * 同步已注册车辆
     *
     * @param serverPlatformId 服务端平台ID
     * @return 结果
     */
    AjaxResult syncVehicle(Long serverPlatformId);

}
