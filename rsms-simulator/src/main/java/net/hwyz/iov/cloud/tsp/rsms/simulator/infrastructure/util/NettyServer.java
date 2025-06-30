package net.hwyz.iov.cloud.tsp.rsms.simulator.infrastructure.util;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.tsp.rsms.simulator.application.service.message.GbMessageDecoder;
import net.hwyz.iov.cloud.tsp.rsms.simulator.application.service.message.GbMessageEncoder;
import net.hwyz.iov.cloud.tsp.rsms.simulator.application.service.message.GbMessageHandler;
import net.hwyz.iov.cloud.tsp.rsms.simulator.domain.server.model.ServerPlatformDo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Netty服务端
 */
@Slf4j
@Component
@Scope("prototype")
@RequiredArgsConstructor
public class NettyServer {
    private ChannelFuture channelFuture;

    private final GbMessageHandler gbMessageHandler;

    public void start(ServerPlatformDo serverPlatform) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new GbMessageDecoder())
                                    .addLast(new GbMessageEncoder())
                                    .addLast(gbMessageHandler);
                        }
                    });
            channelFuture = bootstrap.bind(serverPlatform.getServerPlatformType().getPort()).sync();
            logger.info("Netty服务器启动成功，监听端口[{}]", serverPlatform.getServerPlatformType().getPort());
            channelFuture.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public void stop() {
        if (channelFuture != null && channelFuture.channel().isActive()) {
            channelFuture.channel().close();
        }
    }
}
