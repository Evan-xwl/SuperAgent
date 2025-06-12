package com.xwl.superagent.service;

import com.xwl.superagent.model.FilmEntity;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.StructuredOutputConverter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

/**
 * @author ruoling
 * @date 2025/6/10 20:29:13
 * @description
 */
@Service
public class ChatService {
    private ChatClient chatClient;

    public ChatService(ChatClient.Builder builder) {
        ChatMemory chatMemory = new InMemoryChatMemory();
        this.chatClient = builder
                .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory))
                .build();
    }

    public ChatResponse chatByChatResponse(String prompt) {
        return chatClient.prompt(prompt)
                .user("你好")
                .call()
                .chatResponse();
    }

    public Flux<ChatResponse> chatByFlux(String prompt) {
        return chatClient.prompt(prompt)
                .user("你好")
                .stream()
                .chatResponse();
    }

    record LoveReport(String title, List<String> suggestions) {
    }

    public String chatWithFormat(String promptStr) {
        StructuredOutputConverter outputConverter = new BeanOutputConverter(FilmEntity.class);
        String userInputTemplate = """
        ... 用户文本输入 ....
        {format}
        """; // 用户输入，包含一个“format”占位符。
        Prompt prompt = new Prompt(
                new PromptTemplate(
                        userInputTemplate,
                        Map.of("format", outputConverter.getFormat()) // 用转换器的格式替换“format”占位符
        ).createMessage());
        System.out.println(prompt.getContents());
//        chatClient.prompt(prompt)
//                .user("你好")
//                .call()
//                .entity(FilmEntity.class);
        return prompt.getContents();
    }
}
