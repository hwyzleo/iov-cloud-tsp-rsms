package net.hwyz.iov.cloud.tsp.rsms.service.facade.mpt;

import cn.hutool.core.util.HexUtil;
import cn.hutool.json.JSONObject;
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
import net.hwyz.iov.cloud.tsp.rsms.api.contract.VehicleGbAlarmMpt;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.dataunit.GbRealtimeReportDataUnit;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbCommandFlag;
import net.hwyz.iov.cloud.tsp.rsms.api.feign.mpt.VehicleGbAlarmMptApi;
import net.hwyz.iov.cloud.tsp.rsms.api.util.GbUtil;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.VehicleGbAlarmAppService;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.VehicleGbMessageAppService;
import net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler.VehicleGbAlarmMptAssembler;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.VehicleGbAlarmPo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * 车辆国标报警历史相关管理接口实现类
 *
 * @author hwyz_leo
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/mpt/vehicleGbAlarm")
public class VehicleGbAlarmMptController extends BaseController implements VehicleGbAlarmMptApi {

    private final VehicleGbAlarmAppService vehicleGbAlarmAppService;
    private final VehicleGbMessageAppService vehicleGbMessageAppService;

    /**
     * 分页查询车辆国标报警历史
     *
     * @param vehicleGbAlarm 车辆国标报警
     * @return 车辆国标报警列表
     */
    @RequiresPermissions("iov:rsms:vehicleGbAlarm:list")
    @Override
    @GetMapping(value = "/list")
    public TableDataInfo list(VehicleGbAlarmMpt vehicleGbAlarm) {
        logger.info("管理后台用户[{}]分页查询车辆国标报警历史", SecurityUtils.getUsername());
        startPage();
        List<VehicleGbAlarmPo> vehicleGbAlarmPoList = vehicleGbAlarmAppService.search(vehicleGbAlarm.getVin(),
                vehicleGbAlarm.getAlarmFlag(), vehicleGbAlarm.getAlarmLevel(), getBeginTime(vehicleGbAlarm), getEndTime(vehicleGbAlarm));
        List<VehicleGbAlarmMpt> vehicleGbAlarmMptList = VehicleGbAlarmMptAssembler.INSTANCE.fromPoList(vehicleGbAlarmPoList);
        return getDataTable(vehicleGbAlarmPoList, vehicleGbAlarmMptList);
    }

    /**
     * 导出车辆国标报警历史
     *
     * @param response       响应
     * @param vehicleGbAlarm 车辆国标报警
     */
    @Log(title = "车辆国标报警管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("iov:rsms:vehicleGbAlarm:export")
    @Override
    @PostMapping("/export")
    public void export(HttpServletResponse response, VehicleGbAlarmMpt vehicleGbAlarm) {
        logger.info("管理后台用户[{}]导出车辆国标报警历史", SecurityUtils.getUsername());
    }

    /**
     * 根据车辆国标报警ID获取车辆国标报警
     *
     * @param vehicleGbAlarmId 车辆国标报警ID
     * @return 车辆国标报警
     */
    @RequiresPermissions("iov:rsms:vehicleGbAlarm:query")
    @Override
    @GetMapping(value = "/{vehicleGbAlarmId}")
    public AjaxResult getInfo(@PathVariable Long vehicleGbAlarmId) {
        logger.info("管理后台用户[{}]根据车辆国标报警ID[{}]获取车辆国标报警", SecurityUtils.getUsername(), vehicleGbAlarmId);
        VehicleGbAlarmPo vehicleGbAlarmPo = vehicleGbAlarmAppService.getVehicleGbAlarmById(vehicleGbAlarmId);
        return success(VehicleGbAlarmMptAssembler.INSTANCE.fromPo(vehicleGbAlarmPo));
    }

    /**
     * 解析车辆国标报警
     *
     * @param vehicleGbAlarmId 车辆国标报警ID
     * @return 车辆国标报警
     */
    @RequiresPermissions("iov:rsms:vehicleGbAlarm:query")
    @Override
    @PostMapping(value = "/{vehicleGbAlarmId}/action/parse")
    public AjaxResult parse(@PathVariable Long vehicleGbAlarmId) {
        logger.info("管理后台用户[{}]解析车辆国标报警[{}]", SecurityUtils.getUsername(), vehicleGbAlarmId);
        VehicleGbAlarmPo vehicleGbAlarmPo = vehicleGbAlarmAppService.getVehicleGbAlarmById(vehicleGbAlarmId);
        JSONObject jsonObject = new JSONObject();
        GbUtil.parseMessage(HexUtil.decodeHex(vehicleGbAlarmPo.getMessageData())).ifPresent(gbMessage -> {
            if (Objects.requireNonNull(gbMessage.getHeader().getCommandFlag()) == GbCommandFlag.REALTIME_REPORT) {
                GbRealtimeReportDataUnit dataUnit = (GbRealtimeReportDataUnit) gbMessage.getDataUnit();
                dataUnit.getDataInfoList().forEach(dataInfo -> vehicleGbMessageAppService.assembleParams(dataInfo, jsonObject));
            }
        });
        return success(jsonObject);
    }

    /**
     * 新增车辆国标报警
     *
     * @param vehicleGbAlarm 车辆国标报警
     * @return 结果
     */
    @Log(title = "车辆国标报警管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("iov:rsms:vehicleGbAlarm:add")
    @Override
    @PostMapping
    public AjaxResult add(@Validated @RequestBody VehicleGbAlarmMpt vehicleGbAlarm) {
        logger.info("管理后台用户[{}]新增车辆[{}]国标报警[{}]", SecurityUtils.getUsername(), vehicleGbAlarm.getVin(), vehicleGbAlarm.getAlarmFlag());
        VehicleGbAlarmPo vehicleGbAlarmPo = VehicleGbAlarmMptAssembler.INSTANCE.toPo(vehicleGbAlarm);
        vehicleGbAlarmPo.setCreateBy(SecurityUtils.getUserId().toString());
        return toAjax(vehicleGbAlarmAppService.createVehicleGbAlarm(vehicleGbAlarmPo));
    }

    /**
     * 修改保存车辆国标报警
     *
     * @param vehicleGbAlarm 车辆国标报警
     * @return 结果
     */
    @Log(title = "车辆国标报警管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("iov:rsms:vehicleGbAlarm:edit")
    @Override
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody VehicleGbAlarmMpt vehicleGbAlarm) {
        logger.info("管理后台用户[{}]修改保存车辆[{}]国标报警[{}]", SecurityUtils.getUsername(), vehicleGbAlarm.getVin(), vehicleGbAlarm.getAlarmFlag());
        VehicleGbAlarmPo vehicleGbAlarmPo = VehicleGbAlarmMptAssembler.INSTANCE.toPo(vehicleGbAlarm);
        vehicleGbAlarmPo.setModifyBy(SecurityUtils.getUserId().toString());
        return toAjax(vehicleGbAlarmAppService.modifyVehicleGbAlarm(vehicleGbAlarmPo));
    }

    /**
     * 删除车辆国标报警
     *
     * @param vehicleGbAlarmIds 车辆国标报警ID数组
     * @return 结果
     */
    @Log(title = "车辆国标报警管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("iov:rsms:vehicleGbAlarm:remove")
    @Override
    @DeleteMapping("/{vehicleGbAlarmIds}")
    public AjaxResult remove(@PathVariable Long[] vehicleGbAlarmIds) {
        logger.info("管理后台用户[{}]删除车辆国标报警[{}]", SecurityUtils.getUsername(), vehicleGbAlarmIds);
        return toAjax(vehicleGbAlarmAppService.deleteVehicleGbMessageByIds(vehicleGbAlarmIds));
    }

}
