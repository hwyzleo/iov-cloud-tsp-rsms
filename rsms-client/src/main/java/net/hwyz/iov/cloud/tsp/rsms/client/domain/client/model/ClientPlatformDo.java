package net.hwyz.iov.cloud.tsp.rsms.client.domain.client.model;

import cn.hutool.core.date.DateUtil;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.framework.common.domain.BaseDo;
import net.hwyz.iov.cloud.framework.common.domain.DomainObj;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.ProtocolMessage;
import net.hwyz.iov.cloud.tsp.rsms.api.contract.enums.CommandFlag;
import net.hwyz.iov.cloud.tsp.rsms.client.application.service.ProtocolPackager;
import net.hwyz.iov.cloud.tsp.rsms.client.domain.server.model.ServerPlatformDo;
import net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.util.NettyClient;

import java.util.Date;
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
     * 客户端平台绑定主机名
     */
    private String hostname;
    /**
     * 当前客户端平台绑定主机名
     */
    private String currentHostname;
    /**
     * 客户端平台用户名
     */
    private String username;
    /**
     * 客户端平台密码
     */
    private String password;
    /**
     * 客户端平台唯一识别码
     */
    private String uniqueCode;
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
     * 协议处理器
     */
    private ProtocolPackager packager;

    /**
     * 初始化
     */
    public void init() {
        this.connectState = new AtomicBoolean(false);
        this.loginState = new AtomicBoolean(false);
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
     * 是否已连接服务端平台
     *
     * @return true:已连接,false:未连接
     */
    public boolean isConnect() {
        return this.connectState.get();
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
     * 绑定客户端
     */
    public void bindClient(NettyClient client) {
        this.client = client;
        stateChange();
    }

    /**
     * 绑定主机名
     *
     * @param hostname 主机名
     */
    public void bindHostname(String hostname) {
        this.currentHostname = hostname;
        stateChange();
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
        this.loginTime = new Date();
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
        if (isVehicleMessage(message) && !this.serverPlatform.isVehicleRegistered(message.getVin())) {
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
