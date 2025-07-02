package net.hwyz.iov.cloud.tsp.rsms.service.domain.server.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.framework.common.domain.BaseDo;
import net.hwyz.iov.cloud.framework.common.domain.DomainObj;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbDataUnitEncryptType;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.ProtocolType;

import java.util.Set;

/**
 * 服务端平台领域对象
 *
 * @author hwyz_leo
 */
@Slf4j
@Getter
@SuperBuilder
public class ServerPlatformDo extends BaseDo<String> implements DomainObj<ServerPlatformDo> {

    /**
     * 平台代码
     */
    private String code;
    /**
     * 平台名称
     */
    private String name;
    /**
     * 平台类型：1-国标，2-地标，3-企标
     */
    private Integer type;
    /**
     * 平台地址
     */
    private String url;
    /**
     * 平台端口
     */
    private Integer port;
    /**
     * 平台协议
     */
    private ProtocolType protocol;
    /**
     * 采集频率
     */
    private Integer collectFrequency;
    /**
     * 上报频率
     */
    private Integer reportFrequency;
    /**
     * 是否读写同步
     */
    private Boolean readWriteSync;
    /**
     * 是否维持心跳
     */
    private Boolean heartbeat;
    /**
     * 数据单元加密类型
     */
    private GbDataUnitEncryptType encryptType;
    /**
     * 数据加密KEY
     */
    private String encryptKey;
    /**
     * 已注册车辆集合
     */
    private Set<String> vehicleSet;

    /**
     * 初始化
     */
    public void init() {
        stateInit();
    }

    /**
     * 同步已注册车辆集合
     *
     * @param vehicleSet 已注册车辆集合
     */
    public void syncVehicleSet(Set<String> vehicleSet) {
        this.vehicleSet = vehicleSet;
        stateChange();
    }

    /**
     * 判断车辆是否已注册
     *
     * @param vin 车架号
     * @return true-已注册，false-未注册
     */
    public boolean isVehicleRegistered(String vin) {
        return vehicleSet.contains(vin);
    }

}
