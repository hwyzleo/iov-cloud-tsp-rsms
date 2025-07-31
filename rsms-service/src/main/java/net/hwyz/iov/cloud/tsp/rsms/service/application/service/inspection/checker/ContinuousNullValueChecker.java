package net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.checker;

import cn.hutool.core.util.ObjUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.AbstractChecker;

import java.util.Date;
import java.util.List;

/**
 * 连续空值检查器
 *
 * @author hwyz_leo
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ContinuousNullValueChecker extends AbstractChecker {

    private Date startTime;
    private Date endTime;
    private Long continuousCount;
    private List<String> errors;

    public ContinuousNullValueChecker(String vin, String category, String type, String item) {
        this.vin = vin;
        this.category = category;
        this.type = type;
        this.item = item;
        this.count = 0L;
        this.errorCount = 0L;
    }

    /**
     * 检查
     *
     * @param object 1-待检查值，2-消息时间
     * @return 错误数量
     */
    @Override
    public int check(Object... object) {
        int isError = 0;
        Object value = object[0];
        Date messageTime = (Date) object[1];
        if (ObjUtil.isNull(value)) {
            this.continuousCount++;
            if (this.startTime == null) {
                this.count++;
                this.errorCount++;
                isError = 1;
                this.startTime = messageTime;
            }
            this.endTime = messageTime;
        } else {
            if (this.startTime != null && this.continuousCount > 1) {
                this.count++;
                this.errors.add(this.startTime.getTime() + "," + this.endTime.getTime() + "," + this.continuousCount);
            }
            this.startTime = null;
            this.endTime = null;
            this.continuousCount = 0L;
        }
        return isError;
    }


}
