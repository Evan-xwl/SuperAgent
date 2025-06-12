package com.xwl.superagent.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author ruoling
 * @date 2025/6/8 16:50:53
 * @description
 */
@RestController
@RequestMapping("/dashscope")
public class DashScopeChatModelController {
    private final ChatModel dashScopeChatModel;

    public DashScopeChatModelController(ChatModel chatModel) {
        this.dashScopeChatModel = chatModel;
    }

    @GetMapping("/simple/chat")
    public String simpleChat() {
        return dashScopeChatModel.call(new Prompt("你是谁")).getResult().getOutput().getText();
    }

    /**
     * Stream 流式调用。可以使大模型的输出信息实现打字机效果。
     * @return Flux<String> types.
     */
    @GetMapping("/stream/chat")
    public Flux<String> streamChat(HttpServletResponse response) {

        // 避免返回乱码
        response.setCharacterEncoding("UTF-8");

        Flux<ChatResponse> stream = dashScopeChatModel.stream(new Prompt("你是谁"));
        return stream.map(resp -> resp.getResult().getOutput().getText());
    }

    public ChatModel getDashScopeChatModel() {
        return dashScopeChatModel;
    }
}
