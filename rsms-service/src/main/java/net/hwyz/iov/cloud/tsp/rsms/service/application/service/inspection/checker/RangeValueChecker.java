package net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.checker;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.AbstractChecker;

/**
 * 值范围检查器
 *
 * @author hwyz_leo
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RangeValueChecker extends AbstractChecker {

    private Integer minValue;
    private Integer maxValue;

    public RangeValueChecker(String vin, String category, String type, String item, Integer minValue, Integer maxValue) {
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
     * @param object 待比较值
     */
    @Override
    public long check(Object... object) {
        Object value = object[0];
        this.count++;
        if (Integer.parseInt(value.toString()) < this.minValue || Integer.parseInt(value.toString()) > this.maxValue) {
            this.errorCount++;
        }
        return this.errorCount;
    }


}
