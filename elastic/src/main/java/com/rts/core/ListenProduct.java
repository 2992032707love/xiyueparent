package com.rts.core;

import com.alibaba.fastjson.JSONObject;
import com.rts.canal.CanalMessage;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class ListenProduct {
    @RabbitListener(queues = "elastic")
    public void handlerListen(Message message) {
        try {
            String str = new String(message.getBody(), StandardCharsets.UTF_8);
            CanalMessage canalMessage = JSONObject.parseObject(str, CanalMessage.class);
            System.out.println(canalMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(message);
        }
    }
}
