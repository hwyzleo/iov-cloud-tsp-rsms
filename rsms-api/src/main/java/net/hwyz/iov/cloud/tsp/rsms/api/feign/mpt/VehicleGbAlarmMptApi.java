package net.hwyz.iov.cloud.tsp.rsms.api.feign.mpt;

import jakarta.servlet.http.HttpServletResponse;
import net.hwyz.iov.cloud.framework.common.web.domain.AjaxResult;
import net.hwyz.iov.cloud.framework.common.web.page.TableDataInfo;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.VehicleGbAlarmMpt;

/**
 * 车辆国标报警历史相关管理后台接口
 *
 * @author hwyz_leo
 */
public interface VehicleGbAlarmMptApi {

    /**
     * 分页查询车辆国标报警历史
     *
     * @param vehicleGbAlarm 车辆国标报警
     * @return 车辆国标报警列表
     */
    TableDataInfo list(VehicleGbAlarmMpt vehicleGbAlarm);

    /**
     * 导出车辆国标报警历史
     *
     * @param response       响应
     * @param vehicleGbAlarm 车辆国标报警
     */
    void export(HttpServletResponse response, VehicleGbAlarmMpt vehicleGbAlarm);

    /**
     * 根据车辆国标消息ID获取车辆报警消息
     *
     * @param vehicleGbAlarmId 车辆国标报警ID
     * @return 车辆国标报警
     */
    AjaxResult getInfo(Long vehicleGbAlarmId);

    /**
     * 解析车辆国标报警
     *
     * @param vehicleGbAlarmId 车辆国标报警ID
     * @return 车辆国标报警
     */
    AjaxResult parse(Long vehicleGbAlarmId);

    /**
     * 新增车辆国标报警
     *
     * @param vehicleGbAlarm 车辆国标报警
     * @return 结果
     */
    AjaxResult add(VehicleGbAlarmMpt vehicleGbAlarm);

    /**
     * 修改保存车辆国标报警
     *
     * @param vehicleGbAlarm 车辆国标报警
     * @return 结果
     */
    AjaxResult edit(VehicleGbAlarmMpt vehicleGbAlarm);

    /**
     * 删除车辆国标报警
     *
     * @param vehicleGbAlarmIds 车辆国标报警ID数组
     * @return 结果
     */
    AjaxResult remove(Long[] vehicleGbAlarmIds);

}
