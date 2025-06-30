package net.hwyz.iov.cloud.tsp.rsms.api.contract.dataunit;

import cn.hutool.core.util.ArrayUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessageDataUnit;
import net.hwyz.iov.cloud.tsp.rsms.api.util.GbUtil;

import java.util.Arrays;

/**
 * 国标车辆登出数据单元
 *
 * @author hwyz_leo
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GbVehicleLogoutDataUnit extends GbMessageDataUnit {

    /**
     * 登出时间
     */
    private byte[] logoutTime;
    /**
     * 登出流水号
     * 与当次登入流水号一致
     */
    private int loginSn;

    @Override
    public void parse(byte[] dataUnitBytes) {
        if (dataUnitBytes == null || dataUnitBytes.length != 8) {
            logger.warn("国标车辆登出数据单元[{}]异常", Arrays.toString(dataUnitBytes));
            return;
        }
        this.logoutTime = Arrays.copyOfRange(dataUnitBytes, 0, 6);
        this.loginSn = GbUtil.bytesToWord(Arrays.copyOfRange(dataUnitBytes, 6, 8));
    }

    @Override
    public byte[] toByteArray() {
        return ArrayUtil.addAll(this.logoutTime, GbUtil.wordToBytes(this.loginSn));
    }
}
