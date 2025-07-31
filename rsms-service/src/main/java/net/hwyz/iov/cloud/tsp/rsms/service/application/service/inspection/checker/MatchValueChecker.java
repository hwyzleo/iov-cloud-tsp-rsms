package net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.checker;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.AbstractChecker;

/**
 * 等值检查器
 *
 * @author hwyz_leo
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MatchValueChecker extends AbstractChecker {

    private Integer matchValue;

    public MatchValueChecker(String vin, String category, String type, String item, Integer matchValue) {
        this.vin = vin;
        this.category = category;
        this.type = type;
        this.item = item;
        this.count = 0L;
        this.matchValue = matchValue;
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
        if (Integer.parseInt(value.toString()) == this.matchValue) {
            this.errorCount++;
        }
        return this.errorCount;
    }


}
