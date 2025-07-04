package net.hwyz.iov.cloud.tsp.rsms.api.contract.dataunit;

import cn.hutool.core.util.ArrayUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@EqualsAndHashCode(callSuper = true)
public class GbPlatformLoginAckDataUnit extends GbMessageDataUnit {

    @Override
    public void parse(byte[] dataUnitBytes) {
        if (dataUnitBytes == null || dataUnitBytes.length != 6) {
            logger.warn("国标平台登录应答数据单元[{}]异常", Arrays.toString(dataUnitBytes));
            return;
        }
        this.messageTime = dataUnitBytes;
    }

    @Override
    public byte[] toByteArray() {
        return ArrayUtil.addAll(this.messageTime);
    }
}
