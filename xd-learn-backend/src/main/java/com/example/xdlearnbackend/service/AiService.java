package com.example.xdlearnbackend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author 木又
 * @date 2026/5/22 23:13
 */

@Service
public interface AiService {
    SseEmitter askAIStream(String question, Long userId);
}
