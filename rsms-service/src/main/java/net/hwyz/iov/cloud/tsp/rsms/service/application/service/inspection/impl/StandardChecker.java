package net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.impl;

import cn.hutool.json.JSONObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.inspection.AbstractChecker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 数据规范性检查器
 *
 * @author hwyz_leo
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StandardChecker extends AbstractChecker {

    private Long count;
    private Integer abnormalValue;
    private Long abnormalCount;
    private Long abnormalFrames;
    private Date abnormalStartTime;
    private Date abnormalEndTime;
    private List<JSONObject> abnormalContinuous;
    private Integer invalidValue;
    private Long invalidCount;
    private Long invalidFrames;
    private Date invalidStartTime;
    private Date invalidEndTime;
    private List<JSONObject> invalidContinuous;

    public StandardChecker(String vin, String item, Integer abnormalValue, Integer invalidValue) {
        this.vin = vin;
        this.category = "standard";
        this.item = item;
        this.count = 0L;
        this.abnormalValue = abnormalValue;
        this.abnormalCount = 0L;
        this.abnormalFrames = 0L;
        this.abnormalContinuous = new ArrayList<>();
        this.invalidValue = invalidValue;
        this.invalidCount = 0L;
        this.invalidFrames = 0L;
        this.invalidContinuous = new ArrayList<>();
    }

}
