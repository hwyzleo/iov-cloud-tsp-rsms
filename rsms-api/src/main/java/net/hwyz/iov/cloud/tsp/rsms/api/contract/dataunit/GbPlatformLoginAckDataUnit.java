package net.hwyz.iov.cloud.tsp.rsms.api.contract.dataunit;

import cn.hutool.core.util.ArrayUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessageDataUnit;

import java.util.Arrays;

/**
 * 国标平台登录应答数据单元
 *
 * @author hwyz_leo
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GbPlatformLoginAckDataUnit extends GbMessageDataUnit {

    /**
     * 登入时间
     */
    private byte[] loginTime;

    @Override
    public void parse(byte[] dataUnitBytes) {
        if (dataUnitBytes == null || dataUnitBytes.length != 6) {
            logger.warn("国标平台登录应答数据单元[{}]异常", Arrays.toString(dataUnitBytes));
            return;
        }
        this.loginTime = dataUnitBytes;
    }

    @Override
    public byte[] toByteArray() {
        return ArrayUtil.addAll(this.loginTime);
    }
}
