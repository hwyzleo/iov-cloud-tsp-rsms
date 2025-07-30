package net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.checker;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.AbstractChecker;

import java.util.Date;
import java.util.List;

/**
 * 连续值范围检查器
 *
 * @author hwyz_leo
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ContinuousRangeValueChecker extends AbstractChecker {

    private Integer minValue;
    private Integer maxValue;
    private Date startTime;
    private Date endTime;
    private Long continuousCount;
    private List<String> errors;

    public ContinuousRangeValueChecker(String vin, String category, String type, String item, Integer minValue, Integer maxValue) {
        this.vin = vin;
        this.category = category;
        this.type = type;
        this.item = item;
        this.count = 0L;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.errorCount = 0L;
    }

    /**
     * 检查
     *
     * @param object 1-待比较值，2-消息时间
     */
    @Override
    public void check(Object... object) {
        Object value = object[0];
        Date messageTime = (Date) object[1];
        if (Integer.parseInt(value.toString()) < this.minValue || Integer.parseInt(value.toString()) > this.maxValue) {
            this.continuousCount++;
            if (this.startTime == null) {
                this.count++;
                this.errorCount++;
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
    }
}
