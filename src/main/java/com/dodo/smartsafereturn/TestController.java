package com.dodo.smartsafereturn;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden // swagger 에서 안보이도록 숨김
@Tag(name = "API 기본 테스트 엔드포인트", description = "기본적인 테스트를 포함해 엔드포인트 테스트가 필요할 때 사용")
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
