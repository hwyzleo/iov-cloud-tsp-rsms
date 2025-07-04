package net.hwyz.iov.cloud.tsp.rsms.api.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ByteUtil;
import cn.hutool.core.util.ObjUtil;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.framework.common.util.StrUtil;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessage;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessageDataUnit;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessageHeader;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.dataunit.*;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbAckFlag;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbCommandFlag;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbDataUnitEncryptType;

import java.io.ByteArrayOutputStream;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static net.hwyz.iov.cloud.tsp.rsms.api.contract.constant.GbConstants.*;

/**
 * 国标工具类
 *
 * @author hwyz_leo
 */
@Slf4j
public class GbUtil {

    private static final Charset GB18030 = Charset.forName("GB18030");
    private static final byte NULL_TERMINATOR = 0;

    /**
     * 获取国标日期时间字节数组
     *
     * @return 国标日期时间字节数组
     */
    public static byte[] getGbDateTimeBytes(Long timestamp) {
        DateTime dateTime = DateUtil.date(timestamp);
        byte[] bytes = new byte[6];
        bytes[0] = (byte) Integer.parseInt(Convert.toStr(dateTime.year()).substring(2, 4));
        bytes[1] = (byte) (dateTime.month() + 1);
        bytes[2] = (byte) dateTime.dayOfMonth();
        bytes[3] = (byte) dateTime.hour(true);
        bytes[4] = (byte) dateTime.minute();
        bytes[5] = (byte) dateTime.second();
        return bytes;
    }

    /**
     * 日期时间字节数组转字符串
     *
     * @param bytes 日期时间字节数组
     * @return 日期时间字符串
     */
    public static String dateTimeBytesToString(byte[] bytes) {
        if (ArrayUtil.isEmpty(bytes) && bytes.length != 6) {
            return "";
        }
        StringBuilder sb = new StringBuilder("20");
        sb.append(bytes[0]);
        for (int i = 1; i < 5; i++) {
            if (bytes[i] < 10) {
                sb.append("0");
            }
            sb.append(bytes[i]);
        }
        return sb.toString();
    }

    /**
     * 日期时间字节数组转换成Date对象
     *
     * @param bytes 日期时间字节数组(6位)
     * @return Date对象
     */
    public static Date dateTimeBytesToDate(byte[] bytes) {
        if (ArrayUtil.isEmpty(bytes) || bytes.length != 6) {
            return null;
        }
        int year = 2000 + (bytes[0] & 0xFF);
        int month = bytes[1] & 0xFF;
        int day = bytes[2] & 0xFF;
        int hour = bytes[3] & 0xFF;
        int minute = bytes[4] & 0xFF;
        int second = bytes[5] & 0xFF;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, minute, second);
        return calendar.getTime();
    }

    /**
     * 无符号双字节整型转字节数组
     *
     * @param word 无符号双字节整型
     * @return 字节数组
     */
    public static byte[] wordToBytes(int word) {
        return ByteUtil.shortToBytes((short) word, ByteOrder.BIG_ENDIAN);
    }

    /**
     * 双字节整型转无符号双字节整型
     *
     * @param bytes 字节数组
     * @return 无符号双字节整型
     */
    public static int bytesToWord(byte[] bytes) {
        return ByteUtil.bytesToShort(bytes, ByteOrder.BIG_ENDIAN);
    }

    /**
     * 无符号四字节整型转字节数组
     *
     * @param dword 无符号四字节整型
     * @return 字节数组
     */
    public static byte[] dwordToBytes(int dword) {
        return ByteUtil.intToBytes(dword, ByteOrder.BIG_ENDIAN);
    }

    /**
     * 四字节整型转无符号四字节整型
     *
     * @param bytes 字节数组
     * @return 无符号四字节整型
     */
    public static int bytesToDword(byte[] bytes) {
        return ByteUtil.bytesToInt(bytes, ByteOrder.BIG_ENDIAN);
    }

    /**
     * 字符串转字节数组
     * ASCⅡ字符码，若无数据则放一个0终结符，编码表示见GB/T1988所述，含汉字时，采用区位码编码，占用2个字节，编码表示见GB18030所述
     *
     * @param str         字符串
     * @param bytesLength 数组长度
     * @return 字节数组
     */
    public static byte[] stringToBytes(String str, int bytesLength) {
        if (str == null || str.isEmpty()) {
            return new byte[]{NULL_TERMINATOR};
        }
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            for (char c : str.toCharArray()) {
                if (c < 128) {
                    out.write(c);
                } else {
                    byte[] bytes = String.valueOf(c).getBytes(GB18030);
                    out.write(bytes);
                }
            }
            byte[] result = out.toByteArray();
            // 检查是否需要补0
            if (result.length < bytesLength) {
                byte[] padded = new byte[bytesLength];
                System.arraycopy(result, 0, padded, 0, result.length);
                return padded;
            } else if (result.length > bytesLength) {
                return Arrays.copyOf(result, bytesLength);
            }

            return result;
        } catch (Exception e) {
            logger.warn("字符串转字节数组异常", e);
            return new byte[]{NULL_TERMINATOR};
        }
    }

    /**
     * 字节数组转字符串
     * 按ASCⅡ字符码和GB18030区位码规则解码，遇到0终结符或数组末尾时停止
     *
     * @param bytes 字节数组
     * @return 解码后的字符串
     */
    public static String bytesToString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }

        // 查找第一个0终结符的位置
        int terminatorIndex = -1;
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == NULL_TERMINATOR) {
                terminatorIndex = i;
                break;
            }
        }

        // 提取有效数据（到0终结符之前或整个数组）
        byte[] effectiveBytes;
        if (terminatorIndex >= 0) {
            effectiveBytes = Arrays.copyOf(bytes, terminatorIndex);
        } else {
            effectiveBytes = bytes;
        }

        // 处理空数据
        if (effectiveBytes.length == 0) {
            return "";
        }

        try {
            StringBuilder result = new StringBuilder();
            int i = 0;

            while (i < effectiveBytes.length) {
                byte b = effectiveBytes[i];

                // ASCII字符（0-127）
                if ((b & 0x80) == 0) {
                    result.append((char) b);
                    i++;
                }
                // GB18030多字节字符
                else {
                    // 尝试解析多字节字符
                    int codePoint = decodeMultiByte(effectiveBytes, i);
                    if (codePoint != -1) {
                        result.appendCodePoint(codePoint);
                        // 根据实际解码的字节数移动指针
                        i += getByteLengthForCodePoint(codePoint);
                    } else {
                        // 解码失败，当作单字节处理（容错）
                        result.append('?');
                        i++;
                    }
                }
            }

            return result.toString();
        } catch (Exception e) {
            logger.warn("字节数组转字符串异常", e);
            return "";
        }
    }

    /**
     * 解析GB18030多字节字符
     * 返回Unicode码点，如果解析失败返回-1
     */
    private static int decodeMultiByte(byte[] bytes, int offset) {
        try {
            // 使用GB18030解码
            int length = Math.min(4, bytes.length - offset); // GB18030最长4字节
            byte[] temp = Arrays.copyOfRange(bytes, offset, offset + length);
            String str = new String(temp, GB18030);

            // 如果成功解码出多个字符，取第一个
            if (str.length() > 0) {
                return str.codePointAt(0);
            }

            return -1;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 根据Unicode码点确定GB18030编码的字节长度
     */
    private static int getByteLengthForCodePoint(int codePoint) {
        if (codePoint <= 0x7F) {
            return 1;  // ASCII
        } else if (codePoint <= 0x7FF) {
            return 2;  // 2字节
        } else if (codePoint <= 0xFFFF) {
            return 3;  // 3字节
        } else {
            return 4;  // 4字节
        }
    }

    /**
     * 计算校验码
     * 采用BCC(异或校验)法，校验范围从命令单元的第一个字节开始，同后一字节异或，直到校验码前一字节为止，校验码占用一个字节
     *
     * @param bytes 待计算字节数组
     * @return 校验码
     */
    public static byte calculateCheckCode(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return 0;
        }
        byte checkCode = 0;
        for (byte b : bytes) {
            checkCode ^= b;  // 连续异或操作
        }
        return checkCode;
    }

    /**
     * 验证校验码
     *
     * @param bytes     待计算字节数组
     * @param checkCode 校验码
     * @return 校验结果
     */
    public static boolean verifyCheckCode(byte[] bytes, byte checkCode) {
        byte actualCheckCode = calculateCheckCode(bytes);
        return actualCheckCode == checkCode;
    }

    /**
     * 解析数据头
     *
     * @param headerBytes 数据头字节数组
     * @return 数据头对象
     */
    public static GbMessageHeader parseHeader(byte[] headerBytes) {
        return GbMessageHeader.builder()
                .commandFlag(GbCommandFlag.valOf(headerBytes[GB_DATA_HEADER_COMMAND_FLAG_INDEX]))
                .ackFlag(GbAckFlag.valOf(headerBytes[GB_DATA_HEADER_ACK_FLAG_INDEX]))
                .uniqueCode(GbUtil.bytesToString(ArrayUtil.sub(headerBytes, GB_DATA_HEADER_UNIQUE_CODE_INDEX,
                        GB_DATA_HEADER_UNIQUE_CODE_INDEX + GB_DATA_HEADER_UNIQUE_CODE_LENGTH)))
                .dataUnitEncryptType(GbDataUnitEncryptType.valOf(headerBytes[GB_DATA_HEADER_DATA_UNIT_ENCRYPT_TYPE_INDEX]))
                .dataUnitLength(GbUtil.bytesToWord(ArrayUtil.sub(headerBytes, GB_DATA_HEADER_DATA_UNIT_LENGTH_INDEX,
                        GB_DATA_HEADER_DATA_UNIT_LENGTH_INDEX + GB_DATA_HEADER_DATA_UNIT_LENGTH_LENGTH)))
                .build();
    }

    /**
     * 解析数据单元
     *
     * @param commandFlag   命令标志
     * @param dataUnitBytes 数据单元字节数组
     * @return 数据单元对象
     */
    public static GbMessageDataUnit parseDataUnit(GbCommandFlag commandFlag, byte[] dataUnitBytes) {
        GbMessageDataUnit dataUnit = null;
        switch (commandFlag) {
            case VEHICLE_LOGIN -> dataUnit = new GbVehicleLoginDataUnit();
            case VEHICLE_LOGOUT -> dataUnit = new GbVehicleLogoutDataUnit();
            case REALTIME_REPORT -> dataUnit = new GbRealtimeReportDataUnit();
            case REISSUE_REPORT -> dataUnit = new GbReissueReportDataUnit();
            case PLATFORM_LOGIN -> dataUnit = new GbPlatformLoginDataUnit();
            case PLATFORM_LOGOUT -> dataUnit = new GbPlatformLogoutDataUnit();
        }
        if (ObjUtil.isNotNull(dataUnit)) {
            dataUnit.parse(dataUnitBytes);
        }
        return dataUnit;
    }

    /**
     * 解析国标消息
     *
     * @param bytes 国标报文数据
     * @return 国标消息
     */
    public static Optional<GbMessage> parseMessage(byte[] bytes) {
        return parseMessage(bytes, null, true);
    }

    /**
     * 解析国标消息
     *
     * @param bytes         国标报文数据
     * @param vin           车架号
     * @param parseDataUnit 是否解析数据单元
     * @return 国标消息
     */
    public static Optional<GbMessage> parseMessage(byte[] bytes, String vin, boolean parseDataUnit) {
        // 识别起始符
        int startPos = 0;
        if (!Arrays.equals(ArrayUtil.sub(bytes, startPos, GB_DATA_STARTING_SYMBOLS.length), GB_DATA_STARTING_SYMBOLS)) {
            return Optional.empty();
        }
        // 解析报文头
        startPos += GB_DATA_STARTING_SYMBOLS.length;
        GbMessage gbMessage = new GbMessage();
        gbMessage.setVin(vin);
        gbMessage.setStartingSymbols(GB_DATA_STARTING_SYMBOLS);
        byte[] headerBytes = ArrayUtil.sub(bytes, startPos, startPos + GB_DATA_HEADER_LENGTH);
        gbMessage.parseHeader(headerBytes);
        if (ObjUtil.isNull(gbMessage.getHeader())) {
            return Optional.empty();
        }
        if (StrUtil.isNotBlank(vin) && !gbMessage.getHeader().getUniqueCode().equalsIgnoreCase(vin)) {
            logger.warn("解析国标消息车架号[{}]不一致[{}]", vin, gbMessage.getHeader().getUniqueCode());
        }
        // 解析数据单元
        startPos += GB_DATA_HEADER_LENGTH;
        int dataUnitLength = gbMessage.getHeader().getDataUnitLength();
        byte[] dataUnitBytes = ArrayUtil.sub(bytes, startPos, startPos + dataUnitLength);
        if (parseDataUnit) {
            gbMessage.parseDataUnit(dataUnitBytes);
        } else {
            gbMessage.parseDataUnitMessageTime(dataUnitBytes);
        }
        // 验证校验码
        startPos += dataUnitLength;
        byte checkCode = bytes[startPos];
        gbMessage.calculateCheckCode();
        if (checkCode != gbMessage.getCheckCode()) {
            logger.warn("校验失败，跳过当前数据包");
            return Optional.empty();
        }
        return Optional.of(gbMessage);
    }

    public static void main(String[] args) throws Exception {

    }

}
