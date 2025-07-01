package net.hwyz.iov.cloud.tsp.rsms.api.contract.dataunit;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessageDataUnit;

import java.util.Arrays;

/**
 * 国标平台登出应答数据单元
 *
 * @author hwyz_leo
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GbPlatformLogoutAckDataUnit extends GbMessageDataUnit {

    /**
     * 登出时间
     */
    private byte[] logoutTime;

    @Override
    public void parse(byte[] dataUnitBytes) {
        if (dataUnitBytes == null || dataUnitBytes.length != 6) {
            logger.warn("国标平台登出应答数据单元[{}]异常", Arrays.toString(dataUnitBytes));
            return;
        }
        this.logoutTime = dataUnitBytes;
    }

    @Override
    public byte[] toByteArray() {
        return ArrayUtil.addAll(this.logoutTime);
    }
}
