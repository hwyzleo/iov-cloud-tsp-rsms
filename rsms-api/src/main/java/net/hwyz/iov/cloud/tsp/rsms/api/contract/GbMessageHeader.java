package net.hwyz.iov.cloud.tsp.rsms.api.contract;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbAckFlag;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbCommandFlag;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbDataUnitEncryptType;
import net.hwyz.iov.cloud.tsp.rsms.api.util.GbUtil;

/**
 * 国标消息头
 *
 * @author hwyz_leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GbMessageHeader {

    /**
     * 命令标志
     */
    private GbCommandFlag commandFlag;
    /**
     * 应答标志
     */
    private GbAckFlag ackFlag;
    /**
     * 唯一识别码
     */
    private String uniqueCode;
    /**
     * 数据单元加密类型
     */
    private GbDataUnitEncryptType dataUnitEncryptType;
    /**
     * 数据单元长度
     */
    private Integer dataUnitLength;

    public byte[] toByteArray() {
        return ArrayUtil.addAll(new byte[]{commandFlag.getCode()}, new byte[]{ackFlag.getCode()},
                GbUtil.stringToBytes(uniqueCode, 17), new byte[]{dataUnitEncryptType.getCode()},
                GbUtil.wordToBytes(dataUnitLength));
    }

}
