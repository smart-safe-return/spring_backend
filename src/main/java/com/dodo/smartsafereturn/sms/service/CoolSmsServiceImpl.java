package com.dodo.smartsafereturn.sms.service;

import com.dodo.smartsafereturn.sms.exception.InsufficientBalanceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.model.Balance;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.MultipleDetailMessageSentResponse;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoolSmsServiceImpl implements SmsService {

    private final DefaultMessageService messageService;

    @Value("${cool-sms.from-number}")
    private String fromNumber;

    // SMS당 예상 비용 (원 단위)
    @Value("${cool-sms.cost-per-sms:20}")
    private long costPerSms;

    // 최소 필요 잔액 (원 단위)
    @Value("${cool-sms.minimum-balance: 50}")
    private long minimumBalance;

    // 단일 메시지 보내기
    @Override
    public void sendSms(String to, String content) {

        try {
            // 잔액 확인
            checkSufficientBalance(1);
            
            Message message = new Message();
            // 수신/발신 번호는 - 없이 숫자 11개로만.
            message.setFrom(fromNumber);
            message.setTo(to);
            message.setText(content);

            SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
            log.info("[CoolSmsServiceImpl] sendSms response: {}", response); // todo 나중에 로그 삭제
            
        } catch (Exception e) {
            
            log.error("[CoolSmsServiceImpl] Failed to send SMS: {}", e.getMessage(), e);
            // 예외를 래핑하여 상위로 전달하거나 처리 방식 결정
            throw new RuntimeException("SMS 발송 실패", e);
        }
    }

    // 여러 수신자에게 동일한 메시지 보내기
    @Override
    public void sendSmsToMany(List<String> toList, String content) {
        try {
            // 잔액 확인 (수신자 수만큼의 SMS 비용 계산)
            checkSufficientBalance(toList.size());

            ArrayList<Message> messages = new ArrayList<>();

            for (String number : toList) {
                Message message = new Message();
                message.setFrom(fromNumber);
                message.setTo(number);
                message.setText(content);
                messages.add(message);
            }

            MultipleDetailMessageSentResponse response = messageService.send(messages);
            log.info("[CoolSmsServiceImpl] sendSmsToMany response: {}", response);

        } catch (Exception e) {

            log.error("[CoolSmsServiceImpl] Failed to send multiple SMS: {}", e.getMessage(), e);
            throw new RuntimeException("다중 SMS 발송 실패", e);
        }
    }

    // 잔액 조회 메서드
    @Override
    public long getBalance() {
        try {
            Balance balance = messageService.getBalance();
            log.info("[CoolSmsServiceImpl] Current balance: {}", balance.getBalance());
            return balance.getBalance() != null ? balance.getBalance().longValue() : -1;
        } catch (Exception e) {
            log.error("[CoolSmsServiceImpl] Failed to get balance: {}", e.getMessage(), e);
            throw new RuntimeException("잔액 조회 실패", e);
        }
    }

    // 잔액이 충분한지 확인하는 메서드
    private void checkSufficientBalance(int messageCount) {
        long currentBalance = getBalance();
        long requiredBalance = costPerSms * messageCount;

        if (currentBalance < requiredBalance || currentBalance < minimumBalance) {
            log.warn("[CoolSmsServiceImpl] Insufficient balance: current={}, required={}",
                    currentBalance, Math.max(requiredBalance, minimumBalance));
            throw new InsufficientBalanceException("SMS 발송을 위한 잔액이 부족합니다. 현재 잔액: " + currentBalance + "원",
                    currentBalance);
        }
    }

}
