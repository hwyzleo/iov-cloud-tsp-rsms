package net.hwyz.iov.cloud.tsp.rsms.api.feign.mpt;

import jakarta.servlet.http.HttpServletResponse;
import net.hwyz.iov.cloud.framework.common.web.domain.AjaxResult;
import net.hwyz.iov.cloud.framework.common.web.page.TableDataInfo;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.VehicleGbMessageMpt;

/**
 * 车辆国标消息历史相关管理后台接口
 *
 * @author hwyz_leo
 */
public interface VehicleGbMessageMptApi {

    /**
     * 分页查询车辆国标消息历史
     *
     * @param vehicleGbMessage 车辆国标消息
     * @return 车辆国标消息列表
     */
    TableDataInfo list(VehicleGbMessageMpt vehicleGbMessage);

    /**
     * 导出车辆国标消息历史
     *
     * @param response         响应
     * @param vehicleGbMessage 车辆国标消息
     */
    void export(HttpServletResponse response, VehicleGbMessageMpt vehicleGbMessage);

    /**
     * 根据车辆国标消息ID获取车辆国标消息
     *
     * @param vehicleGbMessageId 车辆国标消息ID
     * @return 车辆国标消息
     */
    AjaxResult getInfo(Long vehicleGbMessageId);

    /**
     * 解析车辆国标消息
     *
     * @param vehicleGbMessageId 车辆国标消息ID
     * @return 车辆国标消息
     */
    AjaxResult parse(Long vehicleGbMessageId);

    /**
     * 新增车辆国标消息
     *
     * @param vehicleGbMessage 车辆国标消息
     * @return 结果
     */
    AjaxResult add(VehicleGbMessageMpt vehicleGbMessage);

    /**
     * 修改保存车辆国标消息
     *
     * @param vehicleGbMessage 车辆国标消息
     * @return 结果
     */
    AjaxResult edit(VehicleGbMessageMpt vehicleGbMessage);

    /**
     * 删除车辆国标消息
     *
     * @param vehicleGbMessageIds 车辆国标消息ID数组
     * @return 结果
     */
    AjaxResult remove(Long[] vehicleGbMessageIds);

}
