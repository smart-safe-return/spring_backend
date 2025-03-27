package com.dodo.smartsafereturn;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/jwtTest1")
    public String jwtTest1() {
        log.info("jwt 테스트");
        log.info("시큐리티 컨텍스트 세션 사용자 name = {}", SecurityContextHolder.getContext().getAuthentication().getName());
        log.info("세션 사용자 role = {}", SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority());
        return "JWT Test 1";
    }
}
