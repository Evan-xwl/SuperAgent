package com.xwl.superagent.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ruoling
 * @date 2025/6/10 20:51:15
 * @description
 */
@SpringBootTest
@Slf4j
class ChatServiceTest {

    private Logger logger = LoggerFactory.getLogger(ChatServiceTest.class);
    @Autowired
    private ChatService chatService;

    @Test
    void chatByChatResponse() {
        ChatResponse response = chatService.chatByChatResponse("现在是什么时间");
        logger.info(response.getResult().getOutput().getText());
        Assert.notNull(response, "response is null");
    }

    @Test
    void chatByFlux() {
        Flux<ChatResponse> chatResponseFlux = chatService.chatByFlux("你好");
        chatResponseFlux.subscribe(chatResponse -> {
            System.out.println(chatResponse.getResult().getOutput().getText());
        });
        Assert.notNull(chatResponseFlux, "response is null");
    }

    @Test
    void chatByChatResponseInMemory() {
        ChatResponse response = chatService.chatByChatResponse("你好，我是若凌");
        System.out.println(response);
        response = chatService.chatByChatResponse("你还记得我是谁吗");
        System.out.println(response);
        Assert.notNull(response, "response is null");
    }

    @Test
    void chatWithFormat() {
        chatService.chatWithFormat("你好");
    }
}