package org.e;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.e.handle.HttpServerHandler;
import org.e.handle.MqttServerHandler;
import org.e.handle.MqttServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NettyServer {
    @Autowired
    MqttServerHandler mqttServerHandler;
    private final static int port = 8888;
    private final Logger log =  LoggerFactory.getLogger(this.getClass());

    public void startup() throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup);
            bootstrap.channel(NioServerSocketChannel.class);

            bootstrap.option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .option(ChannelOption.SO_RCVBUF, 10485760);

            bootstrap.childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel ch) {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new MqttDecoder());
                    pipeline.addLast(MqttEncoder.INSTANCE);
                    pipeline.addLast(mqttServerHandler);
//                    pipeline.addLast(new HttpResponseEncoder());
//                    pipeline.addLast(new HttpRequestDecoder());
//                    pipeline.addLast(new HttpServerHandler());
                }
            });
            ChannelFuture f = bootstrap.bind(port).sync();
            if(f.isSuccess()){
                log.info("服务器开启成功！ 端口：" + port);
                f.channel().closeFuture().sync();
            } else {
                log.error("服务器开启失败！ 端口：" + port);
            }
        } catch (Exception e) {
            log.error("服务器开启失败！"+e.toString());
        } finally {
            bossGroup.shutdownGracefully().sync();
            workGroup.shutdownGracefully().sync();
            log.info("shutdown success");

        }

    }

}

