package net.hwyz.iov.cloud.tsp.rsms.client.domain.client.model;

import cn.hutool.core.date.DateUtil;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.framework.common.domain.BaseDo;
import net.hwyz.iov.cloud.framework.common.domain.DomainObj;
import net.hwyz.iov.cloud.framework.common.util.StrUtil;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.ProtocolMessage;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.CommandFlag;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.GbDataUnitEncryptType;
import net.hwyz.iov.cloud.tsp.rsms.client.application.service.ProtocolPackager;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.server.model.ServerPlatformDo;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.repository.po.ClientPlatformLoginHistoryPo;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.util.NettyClient;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 客户端平台领域对象
 *
 * @author hwyz_leo
 */
@Slf4j
@Getter
@SuperBuilder
public class ClientPlatformDo extends BaseDo<Long> implements DomainObj<ClientPlatformDo> {

    /**
     * 服务端平台
     */
    private ServerPlatformDo serverPlatform;
    /**
     * 客户端平台唯一识别码
     */
    private String uniqueCode;
    /**
     * 采集频率
     */
    private Integer collectFrequency;
    /**
     * 上报频率
     */
    private Integer reportFrequency;
    /**
     * 数据单元加密类型
     */
    private GbDataUnitEncryptType encryptType;
    /**
     * 数据加密KEY
     */
    private String encryptKey;
    /**
     * 客户端平台绑定主机名
     */
    private String hostname;
    /**
     * 当前客户端平台绑定主机名
     */
    private String currentHostname;
    /**
     * 可用账号列表
     */
    private List<ClientPlatformAccountDo> accounts;
    /**
     * 客户端平台用户名
     */
    private String username;
    /**
     * 客户端平台密码
     */
    private String password;
    /**
     * Netty客户端
     */
    private NettyClient client;
    /**
     * 连接状态
     */
    private AtomicBoolean connectState;
    /**
     * 登录状态
     */
    private AtomicBoolean loginState;
    /**
     * 登录流水号
     */
    private AtomicInteger loginSn;
    /**
     * 登录失败原因
     */
    private AtomicInteger failureReason;
    /**
     * 连续登录失败次数
     */
    private AtomicInteger failureCount;
    /**
     * 登录时间
     */
    private Date loginTime;
    /**
     * 登出时间
     */
    private Date logoutTime;
    /**
     * 已注册车辆集合
     */
    private Set<String> vehicleSet;
    /**
     * 协议处理器
     */
    private ProtocolPackager packager;

    /**
     * 初始化
     */
    public void init() {
        this.connectState = new AtomicBoolean(false);
        this.loginState = new AtomicBoolean(false);
        this.loginSn = new AtomicInteger(1);
        this.failureReason = new AtomicInteger(0);
        this.failureCount = new AtomicInteger(0);
        stateInit();
    }

    /**
     * 获取客户端平台唯一代码
     *
     * @return 客户端平台唯一代码
     */
    public String getUniqueKey() {
        return this.serverPlatform.getCode() + "-" + this.uniqueCode;
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

    /**
     * 校验当前主机名是否满足平台主机名绑定限制主机名范围
     *
     * @param hostname 主机名
     * @return true:满足,false:不满足
     */
    public boolean validateHostname(String hostname) {
        if (StrUtil.isBlank(this.hostname)) {
            this.currentHostname = hostname;
            stateChange();
            return true;
        }
        for (String h : this.hostname.split(",")) {
            if (h.trim().equalsIgnoreCase(hostname)) {
                this.currentHostname = hostname;
                stateChange();
                return true;
            }
        }
        return false;
    }

    /**
     * 检查传入主机名是否匹配当前主机名
     * this.hostname或hostname为空时说明无需匹配
     *
     * @param hostname 主机名
     * @return true:匹配成功,false:匹配失败
     */
    public boolean matchHostname(String hostname) {
        return StrUtil.isBlank(this.hostname) || StrUtil.isBlank(hostname) || this.currentHostname.equalsIgnoreCase(hostname);
    }

    /**
     * 分配账号
     *
     * @param accountState 当前全局账号状态
     * @return true:分配成功,false:分配失败
     */
    public boolean allocateAccount(Map<String, Integer> accountState) {
        for (ClientPlatformAccountDo account : this.accounts) {
            if (!accountState.containsKey(account.getUsername()) || accountState.get(account.getUsername()) < account.getUseLimit()) {
                this.username = account.getUsername();
                this.password = account.getPassword();
                stateChange();
                return true;
            }
        }
        return false;
    }

    /**
     * 加载登录历史
     *
     * @param loginHistory 登录历史
     */
    public void loadLoginHistory(ClientPlatformLoginHistoryPo loginHistory) {
        this.loginSn = new AtomicInteger(loginHistory.getLoginSn());
        this.logoutTime = loginHistory.getLoginTime();
        this.failureReason = new AtomicInteger(loginHistory.getFailureReason());
        this.failureCount = new AtomicInteger(loginHistory.getFailureCount());
        stateChange();
    }

    /**
     * 绑定客户端
     */
    public void bindClient(NettyClient client) {
        this.client = client;
        stateChange();
    }

    /**
     * 是否已连接服务端平台
     *
     * @return true:已连接,false:未连接
     */
    public boolean isConnect() {
        return this.connectState.get();
    }

    /**
     * 服务端平台连接成功
     */
    public void connectSuccess() {
        this.connectState.set(true);
        stateChange();
    }

    /**
     * 服务端平台连接失败
     */
    public void connectFailure() {
        this.connectState.set(false);
        stateChange();
    }

    /**
     * 是否已登录服务端平台
     *
     * @return true:已登录,false:未登录
     */
    public boolean isLogin() {
        return this.loginState.get();
    }

    /**
     * 获取登录流水号
     *
     * @return 登录流水号
     */
    public int getLoginSn() {
        return this.loginSn != null ? this.loginSn.get() : 1;
    }

    /**
     * 登录服务端平台
     */
    public void login() {
        logger.info("客户端平台[{}]登录服务端平台[{}]", getUniqueKey(), this.serverPlatform.getName());
        if (isNewLoginSn()) {
            this.loginSn = new AtomicInteger(1);
        } else {
            this.loginSn.incrementAndGet();
        }
        this.loginTime = new Date();
        send(packager.platformLogin(this));
        stateChange();
    }

    /**
     * 登录服务端平台成功
     */
    public void loginSuccess() {
        this.loginState.set(true);
        this.failureCount.set(0);
        this.failureReason.set(0);
        this.logoutTime = null;
        stateChange();
    }

    /**
     * 登录服务端平台失败
     */
    public void loginFailure() {
        this.failureCount.incrementAndGet();
        this.failureReason.set(2);
        stateChange();
    }

    /**
     * 重置登录状态并登录
     */
    public void resetLogin() {
        logger.info("客户端平台[{}]重置登录失败次数", getUniqueKey());
        this.failureCount.set(0);
        login();
    }

    /**
     * 登出服务端平台
     */
    public void logout() {
        logger.info("客户端平台[{}]登出服务端平台[{}]", getUniqueKey(), this.serverPlatform.getName());
        this.logoutTime = new Date();
        send(packager.platformLogout(this));
        stateChange();
    }

    /**
     * 登出服务端平台成功
     */
    public void logoutSuccess() {
        this.loginState.set(false);
        this.logoutTime = new Date();
        stateChange();
    }

    /**
     * 发送数据
     *
     * @param message 协议消息
     */
    public void send(ProtocolMessage message) {
        if (logger.isDebugEnabled()) {
            logger.debug("客户端平台[{}]向服务端平台[{}]发送[{}]消息", getUniqueKey(), this.serverPlatform.getName(), message.getCommandFlag());
        }
        if (isVehicleMessage(message) && !isVehicleRegistered(message.getVin())) {
            return;
        }
        this.client.send(message);
    }

    /**
     * 是否产生新的登录流水号
     *
     * @return true:是,false:否
     */
    private boolean isNewLoginSn() {
        return this.loginTime == null || !DateUtil.formatDate(this.loginTime).equals(DateUtil.today()) || this.loginSn.get() > 65531;
    }

    /**
     * 是否是车辆消息
     *
     * @param message 协议消息
     * @return true:是,false:否
     */
    private boolean isVehicleMessage(ProtocolMessage message) {
        return message.getCommandFlag() == CommandFlag.VEHICLE_LOGIN || message.getCommandFlag() == CommandFlag.VEHICLE_LOGOUT
                || message.getCommandFlag() == CommandFlag.REALTIME_REPORT || message.getCommandFlag() == CommandFlag.REISSUE_REPORT;
    }

}
