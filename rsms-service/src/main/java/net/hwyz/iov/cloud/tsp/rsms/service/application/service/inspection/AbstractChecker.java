package net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection;

import lombok.Data;

/**
 * 检查器抽象类
 *
 * @author hwyz_leo
 */
@Data
public abstract class AbstractChecker {

    /**
     * 车架号
     */
    protected String vin;
    /**
     * 大类
     */
    protected String category;
    /**
     * 小类
     */
    protected String type;
    /**
     * 检查项
     */
    protected String item;
    /**
     * 总数
     */
    protected Long count;
    /**
     * 错误数
     */
    protected Long errorCount;

    /**
     * 进行检查
     *
     * @param object 入参
     * @return 错误数
     */
    public abstract long check(Object... object);

}
