package com.ithuskie.netty2.im;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IMInitializer extends ChannelInitializer<SocketChannel> {

	@Value("${websocket.path}")
	private String websocketPath;

	@Autowired
	private IMHandler imHandler;

	@Override
    public void initChannel(SocketChannel ch) throws Exception {
		 ChannelPipeline pipeline = ch.pipeline();
		//将 Http 请求封装为 FullHttpRequest
        pipeline.addLast(new HttpServerCodec());
        //聚集多个消息转换为 Http请求或相应
		pipeline.addLast(new HttpObjectAggregator(65536));
//		ChunkedWriteHandler主要用于处理大文件流，这里不需要
//		pipeline.addLast(new ChunkedWriteHandler());
		pipeline.addLast(new WebSocketServerProtocolHandler(websocketPath));
		pipeline.addLast(imHandler);

    }
}