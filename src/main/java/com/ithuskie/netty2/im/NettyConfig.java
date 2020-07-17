package com.ithuskie.netty2.im;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyConfig {

    @Autowired
    private IMInitializer IMInitializer;

    @Bean
    public ServerBootstrap bootstrap() {
        ServerBootstrap b = new ServerBootstrap();
        b.group(boss(), worker())
                .channel(NioServerSocketChannel.class)
                .childHandler(IMInitializer)
                .option(ChannelOption.SO_BACKLOG, 2048)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        return b;
    }

    @Bean(name = "boss", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup boss() {
        return new NioEventLoopGroup();
    }

    @Bean(name = "worker", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup worker() {
        return new NioEventLoopGroup();
    }
}