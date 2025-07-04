package net.hwyz.iov.cloud.tsp.rsms.api.contract.dataunit;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessageDataUnit;
import net.hwyz.iov.cloud.tsp.rsms.api.util.GbUtil;

import java.util.Arrays;

/**
 * 国标平台登出数据单元
 *
 * @author hwyz_leo
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GbPlatformLogoutDataUnit extends GbMessageDataUnit {

    /**
     * 登出流水号
     * 与当次登入流水号一致
     */
    private int loginSn;

    @Override
    public void parse(byte[] dataUnitBytes) {
        if (dataUnitBytes == null || dataUnitBytes.length != 8) {
            logger.warn("国标平台登出数据单元[{}]异常", Arrays.toString(dataUnitBytes));
            return;
        }
        this.messageTime = Arrays.copyOfRange(dataUnitBytes, 0, 6);
        this.loginSn = GbUtil.bytesToWord(Arrays.copyOfRange(dataUnitBytes, 6, 8));
    }

    @Override
    public byte[] toByteArray() {
        return ArrayUtil.addAll(this.messageTime, GbUtil.wordToBytes(this.loginSn));
    }
}
