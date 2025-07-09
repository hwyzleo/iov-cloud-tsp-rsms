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
import net.hwyz.iov.cloud.tsp.rsms.api.contract.VehicleGbMessageMpt;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.datainfo.GbVehicleDataDataInfo;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.dataunit.GbRealtimeReportDataUnit;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.dataunit.GbReissueReportDataUnit;
import net.hwyz.iov.cloud.tsp.rsms.api.feign.mpt.VehicleGbMessageMptApi;
import net.hwyz.iov.cloud.tsp.rsms.api.util.GbUtil;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.VehicleGbMessageAppService;
import net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler.GbVehicleDataMptAssembler;
import net.hwyz.iov.cloud.tsp.rsms.service.facade.assembler.VehicleGbMessageMptAssembler;
import net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po.VehicleGbMessagePo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbDataInfoType.VEHICLE;

/**
 * 车辆国标消息历史相关管理接口实现类
 *
 * @author hwyz_leo
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/mpt/vehicleGbMessage")
public class VehicleGbMessageMptController extends BaseController implements VehicleGbMessageMptApi {

    private final VehicleGbMessageAppService vehicleGbMessageAppService;

    /**
     * 分页查询车辆国标消息历史
     *
     * @param vehicleGbMessage 车辆国标消息
     * @return 车辆国标消息列表
     */
    @RequiresPermissions("iov:rsms:vehicleGbMessage:list")
    @Override
    @GetMapping(value = "/list")
    public TableDataInfo list(VehicleGbMessageMpt vehicleGbMessage) {
        logger.info("管理后台用户[{}]分页查询车辆国标消息历史", SecurityUtils.getUsername());
        startPage();
        List<VehicleGbMessagePo> vehicleGbMessagePoList = vehicleGbMessageAppService.search(vehicleGbMessage.getVin(),
                vehicleGbMessage.getCommandFlag(), getBeginTime(vehicleGbMessage), getEndTime(vehicleGbMessage));
        List<VehicleGbMessageMpt> vehicleGbMessageMptList = VehicleGbMessageMptAssembler.INSTANCE.fromPoList(vehicleGbMessagePoList);
        return getDataTable(vehicleGbMessagePoList, vehicleGbMessageMptList);
    }

    /**
     * 导出车辆国标消息历史
     *
     * @param response         响应
     * @param vehicleGbMessage 车辆国标消息
     */
    @Log(title = "车辆国标消息管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("iov:rsms:vehicleGbMessage:export")
    @Override
    @PostMapping("/export")
    public void export(HttpServletResponse response, VehicleGbMessageMpt vehicleGbMessage) {
        logger.info("管理后台用户[{}]导出车辆国标消息历史", SecurityUtils.getUsername());
    }

    /**
     * 根据车辆国标消息ID获取车辆国标消息
     *
     * @param vehicleGbMessageId 车辆国标消息ID
     * @return 车辆国标消息
     */
    @RequiresPermissions("iov:rsms:vehicleGbMessage:query")
    @Override
    @GetMapping(value = "/{vehicleGbMessageId}")
    public AjaxResult getInfo(@PathVariable Long vehicleGbMessageId) {
        logger.info("管理后台用户[{}]根据车辆国标消息ID[{}]获取车辆国标消息", SecurityUtils.getUsername(), vehicleGbMessageId);
        VehicleGbMessagePo vehicleGbMessagePo = vehicleGbMessageAppService.getVehicleGbMessageById(vehicleGbMessageId);
        return success(VehicleGbMessageMptAssembler.INSTANCE.fromPo(vehicleGbMessagePo));
    }

    /**
     * 解析车辆国标消息
     *
     * @param vehicleGbMessageId 车辆国标消息ID
     * @return 车辆国标消息
     */
    @RequiresPermissions("iov:rsms:vehicleGbMessage:query")
    @Override
    @PostMapping(value = "/{vehicleGbMessageId}/action/parse")
    public AjaxResult parse(@PathVariable Long vehicleGbMessageId) {
        logger.info("管理后台用户[{}]解析车辆国标消息[{}]", SecurityUtils.getUsername(), vehicleGbMessageId);
        VehicleGbMessagePo vehicleGbMessagePo = vehicleGbMessageAppService.getVehicleGbMessageById(vehicleGbMessageId);
        JSONObject jsonObject = new JSONObject();
        GbUtil.parseMessage(HexUtil.decodeHex(vehicleGbMessagePo.getMessageData()), vehicleGbMessagePo.getVin(), true).ifPresent(gbMessage -> {
            switch (gbMessage.getHeader().getCommandFlag()) {
                case REALTIME_REPORT -> {
                    GbRealtimeReportDataUnit dataUnit = (GbRealtimeReportDataUnit) gbMessage.getDataUnit();
                    dataUnit.getDataInfoList().forEach(dataInfo -> {
                        switch (dataInfo.getDataInfoType()) {
                            case VEHICLE ->
                                    jsonObject.set(VEHICLE.name(), GbVehicleDataMptAssembler.INSTANCE.fromDataInfo((GbVehicleDataDataInfo) dataInfo));
                        }
                    });
                }
                case REISSUE_REPORT -> {
                    GbReissueReportDataUnit dataUnit = (GbReissueReportDataUnit) gbMessage.getDataUnit();
                    dataUnit.getDataInfoList().forEach(dataInfo -> {
                        switch (dataInfo.getDataInfoType()) {
                            case VEHICLE ->
                                    jsonObject.set(VEHICLE.name(), GbVehicleDataMptAssembler.INSTANCE.fromDataInfo((GbVehicleDataDataInfo) dataInfo));
                        }
                    });
                }
            }
        });
        return success(jsonObject);
    }

    /**
     * 新增车辆国标消息
     *
     * @param vehicleGbMessage 车辆国标消息
     * @return 结果
     */
    @Log(title = "车辆国标消息管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("iov:rsms:vehicleGbMessage:add")
    @Override
    @PostMapping
    public AjaxResult add(@Validated @RequestBody VehicleGbMessageMpt vehicleGbMessage) {
        logger.info("管理后台用户[{}]新增车辆[{}]国标消息[{}]", SecurityUtils.getUsername(), vehicleGbMessage.getVin(), vehicleGbMessage.getCommandFlag());
        VehicleGbMessagePo vehicleGbMessagePo = VehicleGbMessageMptAssembler.INSTANCE.toPo(vehicleGbMessage);
        vehicleGbMessagePo.setCreateBy(SecurityUtils.getUserId().toString());
        return toAjax(vehicleGbMessageAppService.createVehicleGbMessage(vehicleGbMessagePo));
    }

    /**
     * 修改保存车辆国标消息
     *
     * @param vehicleGbMessage 车辆国标消息
     * @return 结果
     */
    @Log(title = "车辆国标消息管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("iov:rsms:vehicleGbMessage:edit")
    @Override
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody VehicleGbMessageMpt vehicleGbMessage) {
        logger.info("管理后台用户[{}]修改保存车辆[{}]国标消息[{}]", SecurityUtils.getUsername(), vehicleGbMessage.getVin(), vehicleGbMessage.getCommandFlag());
        VehicleGbMessagePo vehicleGbMessagePo = VehicleGbMessageMptAssembler.INSTANCE.toPo(vehicleGbMessage);
        vehicleGbMessagePo.setModifyBy(SecurityUtils.getUserId().toString());
        return toAjax(vehicleGbMessageAppService.modifyVehicleGbMessage(vehicleGbMessagePo));
    }

    /**
     * 删除车辆国标消息
     *
     * @param vehicleGbMessageIds 车辆国标消息ID数组
     * @return 结果
     */
    @Log(title = "车辆国标消息管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("iov:rsms:vehicleGbMessage:remove")
    @Override
    @DeleteMapping("/{vehicleGbMessageIds}")
    public AjaxResult remove(@PathVariable Long[] vehicleGbMessageIds) {
        logger.info("管理后台用户[{}]删除车辆国标消息[{}]", SecurityUtils.getUsername(), vehicleGbMessageIds);
        return toAjax(vehicleGbMessageAppService.deleteVehicleGbMessageByIds(vehicleGbMessageIds));
    }

}
