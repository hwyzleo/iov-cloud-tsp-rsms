package net.hwyz.iov.cloud.tsp.rsms.api.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 国标挡位枚举类
 *
 * @author hwyz_leo
 */
@Getter
@AllArgsConstructor
public enum GbGear {

    /** 空挡 **/
    N(0, "空挡"),
    /** 1挡 **/
    GEAR1(1, "1挡"),
    /** 2挡 **/
    GEAR2(2, "2挡"),
    /** 3挡 **/
    GEAR3(3, "3挡"),
    /** 4挡 **/
    GEAR4(4, "4挡"),
    /** 5挡 **/
    GEAR5(5, "5挡"),
    /** 6挡 **/
    GEAR6(6, "6挡"),
    /** 倒挡 **/
    R(13, "倒挡"),
    /** 自动D挡 **/
    D(14, "自动D挡"),
    /** 停车P挡 **/
    P(15, "停车P挡");

    private final int code;
    private final String name;

    public static GbGear valOf(int val) {
        return Arrays.stream(GbGear.values())
                .filter(gbGear -> gbGear.code == val)
                .findFirst()
                .orElse(null);
    }

}
