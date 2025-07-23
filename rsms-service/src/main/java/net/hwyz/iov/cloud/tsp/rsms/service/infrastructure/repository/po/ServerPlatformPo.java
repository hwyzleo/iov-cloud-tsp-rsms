package net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.repository.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import net.hwyz.iov.cloud.framework.mysql.po.BasePo;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 服务端平台 数据对象
 * </p>
 *
 * @author hwyz_leo
 * @since 2025-06-23
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_server_platform")
public class ServerPlatformPo extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 平台代码
     */
    @TableField("code")
    private String code;

    /**
     * 平台名称
     */
    @TableField("name")
    private String name;

    /**
     * 平台类型：1-国标，2-地标，3-企标
     */
    @TableField("type")
    private Integer type;

    /**
     * 平台地址
     */
    @TableField("url")
    private String url;

    /**
     * 平台端口
     */
    @TableField("port")
    private Integer port;

    /**
     * 平台协议
     */
    @TableField("protocol")
    private String protocol;

    /**
     * 是否读写同步
     */
    @TableField("read_write_sync")
    private Boolean readWriteSync;

    /**
     * 是否维持心跳
     */
    @TableField("heartbeat")
    private Boolean heartbeat;
}
