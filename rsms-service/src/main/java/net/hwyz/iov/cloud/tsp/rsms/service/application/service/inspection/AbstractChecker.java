package net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection;

import lombok.Data;

import java.util.Date;

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
     * 消息时间
     */
    protected Date messageTime;
    /**
     * 大类
     */
    protected String category;
    /**
     * 检查项
     */
    protected String item;

}
