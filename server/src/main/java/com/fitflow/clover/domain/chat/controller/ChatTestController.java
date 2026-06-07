package com.fitflow.clover.domain.chat.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Hidden
@Controller
public class ChatTestController {
    @GetMapping("/chat-test")
    public String chatTestPage() {
        return "chat-test";
    }
}
