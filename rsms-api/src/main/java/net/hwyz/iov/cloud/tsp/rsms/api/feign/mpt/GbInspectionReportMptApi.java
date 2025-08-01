package net.hwyz.iov.cloud.tsp.rsms.api.feign.mpt;

import jakarta.servlet.http.HttpServletResponse;
import net.hwyz.iov.cloud.framework.common.web.domain.AjaxResult;
import net.hwyz.iov.cloud.framework.common.web.page.TableDataInfo;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbInspectionReportMpt;

import java.util.Map;

/**
 * 国标检测报告相关管理后台接口
 *
 * @author hwyz_leo
 */
public interface GbInspectionReportMptApi {

    /**
     * 分页查询国标检测报告
     *
     * @param gbInspectionReport 国标检测报告
     * @return 国标检测报告列表
     */
    TableDataInfo list(GbInspectionReportMpt gbInspectionReport);

    /**
     * 获取国标检测报告类型列表
     *
     * @return 国标检测报告类型列表
     */
    AjaxResult listGbInspectionReportType();

    /**
     * 获取国标检测报告状态列表
     *
     * @return 国标检测报告状态列表
     */
    AjaxResult listGbInspectionReportState();

    /**
     * 获取国标检测报告场景列表
     *
     * @return 国标检测报告场景列表
     */
    AjaxResult listGbInspectionReportScene();

    /**
     * 导出国标检测报告
     *
     * @param response           响应
     * @param gbInspectionReport 国标检测报告
     */
    void export(HttpServletResponse response, GbInspectionReportMpt gbInspectionReport);

    /**
     * 根据国标检测报告ID获取国标检测报告
     *
     * @param gbInspectionReportId 国标检测报告ID
     * @return 国标检测报告
     */
    AjaxResult getInfo(Long gbInspectionReportId);

    /**
     * 新增国标检测报告
     *
     * @param gbInspectionReport 国标检测报告
     * @return 结果
     */
    AjaxResult add(GbInspectionReportMpt gbInspectionReport);

    /**
     * 修改保存国标检测报告
     *
     * @param gbInspectionReport 国标检测报告
     * @return 结果
     */
    AjaxResult edit(GbInspectionReportMpt gbInspectionReport);

    /**
     * 删除国标检测报告
     *
     * @param gbInspectionReportIds 国标检测报告ID数组
     * @return 结果
     */
    AjaxResult remove(Long[] gbInspectionReportIds);

}
