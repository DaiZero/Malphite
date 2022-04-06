package com.dzero.malphite.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "登录")
@RequestMapping("/login")
@RestController
@Slf4j
public class LoginController {
    @Operation(summary = "测试", description = "测试接口")
    @GetMapping(value = "/test")
    public void insert(@RequestParam() @Parameter(description = "单词") String word) {
        log.info(word);
    }
}
