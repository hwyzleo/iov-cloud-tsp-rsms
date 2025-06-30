package net.hwyz.iov.cloud.tsp.rsms.api.contract.constant;

/**
 * 国标常量类
 *
 * @author hwyz_leo
 */
public class GbConstants {

    /**
     * 国标起始符
     */
    public static final byte[] GB_DATA_STARTING_SYMBOLS = new byte[]{0x23, 0x23};
    /**
     * 国标数据头长度
     */
    public static final int GB_DATA_HEADER_LENGTH = 22;
    /**
     * 国标数据头命令标识符索引
     */
    public static final int GB_DATA_HEADER_COMMAND_FLAG_INDEX = 0;
    /**
     * 国标数据头应答标识符索引
     */
    public static final int GB_DATA_HEADER_ACK_FLAG_INDEX = 1;
    /**
     * 国标数据头唯一标识符索引
     */
    public static final int GB_DATA_HEADER_UNIQUE_CODE_INDEX = 2;
    /**
     * 国标数据头唯一标识符长度
     */
    public static final int GB_DATA_HEADER_UNIQUE_CODE_LENGTH = 17;
    /**
     * 国标数据头数据单元加密类型索引
     */
    public static final int GB_DATA_HEADER_DATA_UNIT_ENCRYPT_TYPE_INDEX = 19;
    /**
     * 国标数据头数据单元长度索引
     */
    public static final int GB_DATA_HEADER_DATA_UNIT_LENGTH_INDEX = 20;
    /**
     * 国标数据头数据单元长度长度
     */
    public static final int GB_DATA_HEADER_DATA_UNIT_LENGTH_LENGTH = 2;

}
