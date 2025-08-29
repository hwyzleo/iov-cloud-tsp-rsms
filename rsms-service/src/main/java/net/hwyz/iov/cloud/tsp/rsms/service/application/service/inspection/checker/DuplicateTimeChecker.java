package net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.checker;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.AbstractChecker;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 重复时间检查器
 *
 * @author hwyz_leo
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DuplicateTimeChecker extends AbstractChecker {

    private Set<Long> timestamps;

    public DuplicateTimeChecker(String vin, String category, String type, String item) {
        this.vin = vin;
        this.category = category;
        this.type = type;
        this.item = item;
        this.count = 0L;
        this.errorCount = 0L;
        this.timestamps = new HashSet<>();
    }

    /**
     * 检查
     *
     * @param object 待比较值
     */
    @Override
    public int check(Object... object) {
        Date value = (Date) object[0];
        int isError = 0;
        this.count++;
        if (this.timestamps.contains(value.getTime())) {
            this.errorCount++;
            isError = 1;
        }
        this.timestamps.add(value.getTime());
        return isError;
    }


}
