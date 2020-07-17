package com.ithuskie.netty2.im;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author: Yakai Zheng（zhengyk@cloud-young.com）
 * @date: Created on 2018/10/23
 * @description:
 * @version: 1.0
 */
@Slf4j
@Component
public class IMServer {

    @Value("${netty.port}")
    private int port;

    @Autowired
    private ServerBootstrap b;

    @Autowired
    @Qualifier("boss")
    private NioEventLoopGroup boss;

    @Autowired()
    @Qualifier("worker")
    private NioEventLoopGroup worker;

    @PostConstruct
    public void run() throws Exception {
        try {
            ChannelFuture f = b.bind(port).sync();
            log.info("IMServer 启动，端口:"+port);
            //阻塞等待服务监听端口关闭
            f.channel().closeFuture().sync();
        } finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }
}
