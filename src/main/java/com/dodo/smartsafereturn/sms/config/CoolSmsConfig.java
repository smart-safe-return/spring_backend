package com.dodo.smartsafereturn.sms.config;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoolSmsConfig {

    @Value("${cool-sms.api-key}")
    private String apiKey;

    @Value("${cool-sms.secret-key}")
    private String secretKey;

    @Bean
    public DefaultMessageService messageService() {
        return NurigoApp.INSTANCE.initialize(apiKey, secretKey, "https://api.coolsms.co.kr");
    }
}
