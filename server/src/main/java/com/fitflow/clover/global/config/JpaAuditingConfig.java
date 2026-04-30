package com.fitflow.clover.global.config;

import com.fitflow.clover.global.security.SecurityUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;


@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {
    @Bean
    public AuditorAware<Long> auditorProvider() {
        return () -> {
            try {
                return Optional.of(SecurityUtil.getCurrentMemberId());
            } catch (Exception e) {
                return Optional.empty();
            }
        };
    }
}
