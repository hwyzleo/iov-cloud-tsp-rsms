package net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.po;

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
 * 客户端平台 数据对象
 * </p>
 *
 * @author hwyz_leo
 * @since 2025-07-10
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_client_platform")
public class ClientPlatformPo extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 服务端平台代码
     */
    @TableField("server_platform_code")
    private String serverPlatformCode;

    /**
     * 唯一识别码
     */
    @TableField("unique_code")
    private String uniqueCode;

    /**
     * 数据加密方式
     */
    @TableField("encrypt_type")
    private Integer encryptType;

    /**
     * 数据加密KEY
     */
    @TableField("encrypt_key")
    private String encryptKey;

    /**
     * 是否启用
     */
    @TableField("enable")
    private Boolean enable;
}
