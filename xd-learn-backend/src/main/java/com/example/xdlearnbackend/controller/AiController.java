package com.example.xdlearnbackend.controller;

import com.example.xdlearnbackend.entity.User;
import com.example.xdlearnbackend.exception.BusinessException;
import com.example.xdlearnbackend.service.AiService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author 木又
 * @date 2026/5/22 23:11
 */
@RestController
@RequestMapping("/api/assistant")
public class AiController {

    @Autowired
    private AiService aiService;

    @PostMapping("/ask/stream")
    public SseEmitter askAIStream(@RequestBody String question, HttpServletRequest request) throws BusinessException {
        User currentUser = (User) request.getAttribute("currentUser");
        if (currentUser == null) {
            throw new BusinessException(401, "用户未登录");
        }
        return aiService.askAIStream(question, currentUser.getId());
    }
}
