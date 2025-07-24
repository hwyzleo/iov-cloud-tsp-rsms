package net.hwyz.iov.cloud.tsp.rsms.api.feign.mpt;

import jakarta.servlet.http.HttpServletResponse;
import net.hwyz.iov.cloud.framework.common.web.domain.AjaxResult;
import net.hwyz.iov.cloud.framework.common.web.page.TableDataInfo;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.ReportVehicleMpt;

/**
 * 上报车辆相关管理后台接口
 *
 * @author hwyz_leo
 */
public interface ReportVehicleMptApi {

    /**
     * 分页查询上报车辆
     *
     * @param reportVehicle 上报车辆
     * @return 上报车辆列表
     */
    TableDataInfo list(ReportVehicleMpt reportVehicle);

    /**
     * 导出上报车辆
     *
     * @param response      响应
     * @param reportVehicle 上报车辆
     */
    void export(HttpServletResponse response, ReportVehicleMpt reportVehicle);

    /**
     * 根据上报车辆ID获取上报车辆
     *
     * @param reportVehicleId 上报车辆ID
     * @return 上报车辆
     */
    AjaxResult getInfo(Long reportVehicleId);

    /**
     * 新增上报车辆
     *
     * @param reportVehicle 上报车辆
     * @return 结果
     */
    AjaxResult add(ReportVehicleMpt reportVehicle);

    /**
     * 修改保存上报车辆
     *
     * @param reportVehicle 上报车辆
     * @return 结果
     */
    AjaxResult edit(ReportVehicleMpt reportVehicle);

    /**
     * 删除上报车辆
     *
     * @param reportVehicleIds 上报车辆ID数组
     * @return 结果
     */
    AjaxResult remove(Long[] reportVehicleIds);

}
