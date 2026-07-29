package com.thuyen.bakeryshop.modules.auth.infrastructure.persistence.entity;

import com.thuyen.bakeryshop.modules.auth.domain.enums.LoginCodeChannel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "login_codes")
@Getter
@Setter
@NoArgsConstructor
public class LoginCodeEntity {

    @Id
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false, length = 20)
    private LoginCodeChannel channel;

    @Column(name = "target", nullable = false)
    private String target;

    @Column(name = "code_hash", nullable = false, columnDefinition = "text")
    private String codeHash;

    @Column(name = "failed_attempts", nullable = false)
    private int failedAttempts;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "used_at")
    private Instant usedAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
