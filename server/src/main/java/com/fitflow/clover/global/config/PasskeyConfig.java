package com.fitflow.clover.global.config;

import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.data.RelyingPartyIdentity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PasskeyConfig {
    @Bean
    public RelyingParty relyingParty(CredentialRepository repository) {
        return RelyingParty.builder()
                .identity(RelyingPartyIdentity.builder()
                        .id("localhost")
                        .name("Clo-ver")
                        .build())
                .credentialRepository(repository)
                .build();
    }
}
