package net.hwyz.iov.cloud.tsp.rsms.service.infrastructure.util;

import cn.hutool.core.util.ObjUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.framework.common.util.StrUtil;
import net.hwyz.iov.cloud.tsp.rsms.service.application.event.publish.NettyClientPublish;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.message.MessageDecoder;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.message.MessageEncoder;
import net.hwyz.iov.cloud.tsp.rsms.service.application.service.message.MessageHandler;
import net.hwyz.iov.cloud.tsp.rsms.service.domain.client.model.ClientPlatformDo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Netty客户端
 */
@Slf4j
@Getter
@Component
@Scope("prototype")
@RequiredArgsConstructor
public class NettyClient {

    private final ApplicationContext ctx;
    private final NettyClientPublish publish;

    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private Channel channel;
    /**
     * 重连次数
     */
    private int reconnectCount = 0;
    /**
     * 最大重连次数
     */
    private static final int MAX_RECONNECT_COUNT = 5;

    /**
     * 建立 Netty 连接
     */
    public void connect(ClientPlatformDo clientPlatform) {
        if (channel != null && channel.isActive()) {
            logger.warn("已有活动连接，跳过重连");
            return;
        }
        String url = clientPlatform.getServerPlatform().getUrl();
        Integer port = clientPlatform.getServerPlatform().getPort();
        String protocol = clientPlatform.getServerPlatform().getProtocol();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        String messageDecoderClassName = MessageDecoder.class.getPackageName() + "." +
                                StrUtil.upperFirst(protocol) + "MessageDecoder";
                        MessageDecoder messageDecoder = (MessageDecoder) Class.forName(messageDecoderClassName)
                                .getDeclaredConstructor().newInstance();
                        String messageEncoderClassName = MessageEncoder.class.getPackageName() + "." +
                                StrUtil.upperFirst(protocol) + "MessageEncoder";
                        MessageEncoder messageEncoder = (MessageEncoder) Class.forName(messageEncoderClassName)
                                .getDeclaredConstructor().newInstance();
                        MessageHandler messageHandler = ctx.getBean(protocol + "MessageHandler", MessageHandler.class);
                        messageHandler.setClientPlatform(clientPlatform);
                        ch.pipeline()
                                .addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS))
                                .addLast(messageDecoder)
                                .addLast(messageEncoder)
                                .addLast(messageHandler);
                    }
                });

        try {
            ChannelFuture future = bootstrap.connect(url, port).sync();
            this.channel = future.channel();
            // 监听连接关闭事件，自动移除失效连接
            this.channel.closeFuture().addListener(f -> {
                logger.info("连接[{}:{}]已关闭，尝试第[{}]次重连...", url, port, ++reconnectCount);
                if (reconnectCount <= MAX_RECONNECT_COUNT) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            connect(clientPlatform);
                        }
                    }, 5000);
                } else {
                    logger.error("达到最大重连次数，不再尝试");
                    // TODO 发送通知
                }
            });
            logger.info("连接[{}:{}]已建立", url, port);
            clientPlatform.bindClient(this);
            publish.connectSuccess(clientPlatform);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("连接[{}:{}]被中断", url, port, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 发送消息
     */
    public void send(Object message) {
        if (ObjUtil.isNull(channel) || !channel.isActive()) {
            logger.warn("连接不存在或已关闭，无法发送消息");
            return;
        }
        channel.writeAndFlush(message);
    }

    /**
     * 关闭连接
     */
    public void close() {
        if (ObjUtil.isNotNull(channel)) {
            channel.close();
        }
    }

    /**
     * 优雅关闭资源
     */
    @PreDestroy
    public void destroy() {
        close();
        workerGroup.shutdownGracefully();
    }

}
