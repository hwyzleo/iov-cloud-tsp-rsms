package net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.checker;

import cn.hutool.core.util.ObjUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.AbstractChecker;

/**
 * 空值检查器
 *
 * @author hwyz_leo
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NullValueChecker extends AbstractChecker {

    private Integer matchValue;

    public NullValueChecker(String vin, String category, String type, String item) {
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
     * @param object 待检查值
     */
    @Override
    public int check(Object... object) {
        int isError = 0;
        Object value = object[0];
        this.count++;
        if (ObjUtil.isNull(value)) {
            this.errorCount++;
            isError = 1;
        }
        return isError;
    }


}
