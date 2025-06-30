package net.hwyz.iov.cloud.tsp.rsms.simulator.application.service;

import net.hwyz.iov.cloud.tsp.rsms.api.contract.GbMessage;

import java.util.Optional;

/**
 * 国标协议处理接口
 *
 * @author hwyz_leo
 */
public interface GbProtocolHandler {

    /**
     * 处理国标消息
     *
     * @param message 国标消息
     * @return 国标消息
     */
    Optional<GbMessage> handle(GbMessage message);

}
