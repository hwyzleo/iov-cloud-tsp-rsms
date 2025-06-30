package net.hwyz.iov.cloud.tsp.rsms.api.contract.dataunit;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessageDataUnit;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbDataUnitEncryptType;
import net.hwyz.iov.cloud.tsp.rsms.api.util.GbUtil;

import java.util.Arrays;

/**
 * 国标车辆补发信息上报数据单元
 *
 * @author hwyz_leo
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GbReissueReportDataUnit extends GbMessageDataUnit {

    /**
     * 登入时间
     */
    private byte[] loginTime;
    /**
     * 登入流水号
     */
    private int loginSn;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 加密类型
     */
    private GbDataUnitEncryptType encryptType;

    @Override
    public void parse(byte[] dataUnitBytes) {
        if (dataUnitBytes == null || dataUnitBytes.length != 41) {
            logger.warn("国标平台登录数据单元[{}]异常", Arrays.toString(dataUnitBytes));
            return;
        }
        this.loginTime = Arrays.copyOfRange(dataUnitBytes, 0, 6);
        this.loginSn = GbUtil.bytesToWord(Arrays.copyOfRange(dataUnitBytes, 6, 8));
        this.username = GbUtil.bytesToString(Arrays.copyOfRange(dataUnitBytes, 8, 20));
        this.password = GbUtil.bytesToString(Arrays.copyOfRange(dataUnitBytes, 20, 40));
        this.encryptType = GbDataUnitEncryptType.valOf(dataUnitBytes[40]);
    }

    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }
}
