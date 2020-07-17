package com.ithuskie.netty2.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author: Yakai Zheng（zhengyk@cloud-young.com）
 * @date: Created on 2018/10/23
 * @description:
 * @version: 1.0
 */
@Getter
@Setter
public class MessageVo implements Serializable {

    //0-提示信息 1-在线聊天  2-拍一拍
    private int type;
    //名称
    private String name;
    //消息内容
    private String talkWords;
}
