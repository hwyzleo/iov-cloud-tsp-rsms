package net.hwyz.iov.cloud.tsp.rsms.api.contract.dataunit;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessageDataUnit;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbDataUnitEncryptType;
import net.hwyz.iov.cloud.tsp.rsms.api.util.GbUtil;

import java.util.Arrays;

/**
 * 国标平台登录数据单元
 *
 * @author hwyz_leo
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GbPlatformLoginDataUnit extends GbMessageDataUnit {

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
        this.messageTime = GbUtil.dateTimeBytesToDate(Arrays.copyOfRange(dataUnitBytes, 0, 6));
        this.loginSn = GbUtil.bytesToWord(Arrays.copyOfRange(dataUnitBytes, 6, 8));
        this.username = GbUtil.bytesToString(Arrays.copyOfRange(dataUnitBytes, 8, 20));
        this.password = GbUtil.bytesToString(Arrays.copyOfRange(dataUnitBytes, 20, 40));
        this.encryptType = GbDataUnitEncryptType.valOf(dataUnitBytes[40]);
    }

    @Override
    public byte[] toByteArray() {
        return ArrayUtil.addAll(GbUtil.getGbDateTimeBytes(this.messageTime.getTime()), GbUtil.wordToBytes(this.loginSn),
                GbUtil.stringToBytes(this.username, 12), GbUtil.stringToBytes(this.password, 20),
                new byte[]{this.encryptType.getCode()});
    }
}
