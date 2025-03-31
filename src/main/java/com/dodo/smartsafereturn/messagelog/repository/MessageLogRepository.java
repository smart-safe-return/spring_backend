package com.dodo.smartsafereturn.messagelog.repository;

import com.dodo.smartsafereturn.messagelog.entity.MessageLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageLogRepository extends JpaRepository<MessageLog, Long>, CustomMessageLogRepository {
}
