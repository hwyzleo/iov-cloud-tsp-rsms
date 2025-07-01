package net.hwyz.iov.cloud.tsp.rsms.api.feign.mpt;

import jakarta.servlet.http.HttpServletResponse;
import net.hwyz.iov.cloud.framework.common.web.domain.AjaxResult;
import net.hwyz.iov.cloud.framework.common.web.page.TableDataInfo;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.RegisteredVehicleMpt;

/**
 * 服务端平台已注册车辆相关管理后台接口
 *
 * @author hwyz_leo
 */
public interface RegisteredVehicleMptApi {

    /**
     * 分页查询已注册车辆
     *
     * @param registeredVehicle 已注册车辆
     * @return 已注册车辆列表
     */
    TableDataInfo list(RegisteredVehicleMpt registeredVehicle);

    /**
     * 导出已注册车辆
     *
     * @param response          响应
     * @param registeredVehicle 已注册车辆
     */
    void export(HttpServletResponse response, RegisteredVehicleMpt registeredVehicle);

    /**
     * 根据已注册车辆ID获取已注册车辆
     *
     * @param registeredVehicleId 已注册车辆ID
     * @return 已注册车辆
     */
    AjaxResult getInfo(Long registeredVehicleId);

    /**
     * 新增已注册车辆
     *
     * @param registeredVehicle 已注册车辆
     * @return 结果
     */
    AjaxResult add(RegisteredVehicleMpt registeredVehicle);

    /**
     * 修改保存已注册车辆
     *
     * @param registeredVehicle 已注册车辆
     * @return 结果
     */
    AjaxResult edit(RegisteredVehicleMpt registeredVehicle);

    /**
     * 删除已注册车辆
     *
     * @param registeredVehicleIds 已注册车辆ID数组
     * @return 结果
     */
    AjaxResult remove(Long[] registeredVehicleIds);

}
