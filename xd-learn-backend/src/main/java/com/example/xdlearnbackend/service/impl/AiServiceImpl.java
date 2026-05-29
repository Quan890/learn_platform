package com.example.xdlearnbackend.service.impl;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.example.xdlearnbackend.service.AiService;
import io.reactivex.Flowable;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AI服务实现类（带上下文记忆 + 自定义人设）
 */
@Service
public class AiServiceImpl implements AiService {

    // 从配置文件中读取DashScope API密钥
    @Value("${dashscope.api.key:sk-b5e994cf5a504288ae46e956afb379a8}")
    private String apiKey;

    // 日志工具
    private static final Logger logger = LoggerFactory.getLogger(AiServiceImpl.class);

    // AI客户端
    private Generation generation;

    // ===================== 【新增】上下文存储 =====================
    // key: 用户ID (这里先用固定值，你后面可以换成登录用户ID)
    // value: 对话历史列表
    private final Map<Long, List<Message>> userContextMap = new ConcurrentHashMap<>();

    // ===================== 【自定义】AI人设 + 名字 =====================
    private static final String AI_NAME = "木又编程学习助手";
    private static final String SYSTEM_PROMPT =
            "你的名字叫" + AI_NAME + "，是一个专业的编程学习助手。" +
                    "你要温柔、耐心、专业地回答用户的学习问题。" +
                    "用户问你是谁，你就回答：我是" + AI_NAME + "，你的专属编程学习助手~";

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        generation = new Generation();
    }

    /**
     * 核心流式问答（带上下文记忆）
     */
    @Override
    public SseEmitter askAIStream(String question, Long userId) {
        SseEmitter emitter = new SseEmitter(60000L);

        // 1. 获取该用户的历史对话
        List<Message> messageList = userContextMap.getOrDefault(userId, new ArrayList<>());

        // 2. 如果是第一次对话，加入【系统人设】
        if (messageList.isEmpty()) {
            Message systemMsg = Message.builder()
                    .role(Role.SYSTEM.getValue())
                    .content(SYSTEM_PROMPT)
                    .build();
            messageList.add(systemMsg);
        }

        // 3. 加入当前用户问题
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(question)
                .build();
        messageList.add(userMsg);

        // 4. 异步调用AI
        new Thread(() -> {
            try {
                GenerationParam param = buildGenerationParam(messageList);
                Flowable<GenerationResult> result = generation.streamCall(param);

                // 拼接AI返回的完整内容
                StringBuilder fullAnswer = new StringBuilder();

                result.blockingForEach(message -> {
                    String content = message.getOutput().getChoices().get(0).getMessage().getContent();
                    fullAnswer.append(content);
                    emitter.send(content);
                });

                // 5. 把AI的回答也存入上下文（下次对话能记住）
                Message aiMsg = Message.builder()
                        .role(Role.ASSISTANT.getValue())
                        .content(fullAnswer.toString())
                        .build();
                messageList.add(aiMsg);
                userContextMap.put(userId, messageList);

                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
                logger.error("AI流式调用异常", e);
            }
        }).start();

        return emitter;
    }

    /**
     * 构建请求参数（传入完整对话列表）
     */
    private GenerationParam buildGenerationParam(List<Message> messageList) {
        return GenerationParam.builder()
                .apiKey(apiKey)
                .model("qwen-plus")
                .messages(messageList)  // 传入完整上下文
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .incrementalOutput(true)
                .build();
    }
}