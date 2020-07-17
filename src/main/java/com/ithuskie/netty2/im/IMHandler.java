package com.ithuskie.netty2.im;

import com.alibaba.fastjson.JSONObject;
import com.ithuskie.netty2.model.MessageVo;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ChannelHandler.Sharable
public class IMHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {


	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	public static Map<Channel,String> map = new ConcurrentHashMap<>(1024);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx,
			TextWebSocketFrame webSocketFrame) throws Exception {
		Channel incoming = ctx.channel();
        String msg = webSocketFrame.text();
		log.info("收到的消息: "+msg);
		MessageVo messageVo = JSONObject.parseObject(msg, MessageVo.class);
		map.put(incoming, messageVo.getName());
		channels.writeAndFlush(new TextWebSocketFrame(msg));
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		channels.add(channel);
		log.info(channel.remoteAddress() +"加入");
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();

		String name = map.get(channel);
		map.remove(channel);
		MessageVo messageVo = new MessageVo();
		messageVo.setTalkWords(name+"离开了聊天室");
		log.info(messageVo.getTalkWords());
		String msg = JSONObject.toJSONString(messageVo);
		channels.writeAndFlush(new TextWebSocketFrame(msg));
		}
	    
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
		log.info("Client:"+incoming.remoteAddress()+"在线");
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
		log.info("Client:"+incoming.remoteAddress()+"掉线");
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
    	Channel incoming = ctx.channel();
		log.info("Client:"+incoming.remoteAddress()+"异常：",cause);
        // 当出现异常就关闭连接
        ctx.close();
	}

}