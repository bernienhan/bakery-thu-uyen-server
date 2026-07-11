CREATE TABLE roles (
    id UUID PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE users (
    id UUID PRIMARY KEY,
    role_id UUID NOT NULL,
    email VARCHAR(255) UNIQUE,
    phone VARCHAR(30) UNIQUE,
    full_name VARCHAR(255),
    avatar_url TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    email_verified_at TIMESTAMP WITH TIME ZONE,
    phone_verified_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_users_role_id
        FOREIGN KEY (role_id) REFERENCES roles (id),
    CONSTRAINT chk_users_status
        CHECK (status IN ('ACTIVE', 'INACTIVE', 'BANNED', 'DELETED'))
);

CREATE TABLE user_credentials (
    user_id UUID PRIMARY KEY,
    password_hash TEXT NOT NULL,
    password_changed_at TIMESTAMP WITH TIME ZONE,
    failed_attempts INTEGER NOT NULL DEFAULT 0,
    locked_until TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_user_credentials_user_id
        FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE auth_providers (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    provider_code VARCHAR(50) NOT NULL,
    provider_user_id VARCHAR(255) NOT NULL,
    provider_email VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_auth_providers_user_id
        FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT uq_auth_providers_provider_user
        UNIQUE (provider_code, provider_user_id),
    CONSTRAINT uq_auth_providers_user_provider
        UNIQUE (user_id, provider_code)
);

CREATE TABLE login_codes (
    id UUID PRIMARY KEY,
    user_id UUID,
    channel VARCHAR(20) NOT NULL,
    target VARCHAR(255) NOT NULL,
    code_hash TEXT NOT NULL,
    failed_attempts INTEGER NOT NULL DEFAULT 0,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    used_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_login_codes_user_id
        FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT chk_login_codes_channel
        CHECK (channel IN ('EMAIL', 'PHONE'))
);

CREATE TABLE refresh_tokens (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    token_hash TEXT NOT NULL UNIQUE,
    device_name VARCHAR(255),
    ip_address VARCHAR(50),
    user_agent TEXT,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    revoked_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_refresh_tokens_user_id
        FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE email_verification_tokens (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    token_hash TEXT NOT NULL UNIQUE,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    used_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_email_verification_tokens_user_id
        FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE password_reset_tokens (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    token_hash TEXT NOT NULL UNIQUE,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    used_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_password_reset_tokens_user_id
        FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE security_events (
    id UUID PRIMARY KEY,
    user_id UUID,
    event_code VARCHAR(100) NOT NULL,
    target_type VARCHAR(50),
    target_value VARCHAR(255),
    ip_address VARCHAR(50),
    user_agent TEXT,
    metadata JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_security_events_user_id
        FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE INDEX idx_security_events_user_id
    ON security_events (user_id);

CREATE INDEX idx_security_events_event_code
    ON security_events (event_code);

CREATE INDEX idx_security_events_target_value
    ON security_events (target_value);

CREATE INDEX idx_security_events_created_at
    ON security_events (created_at);

CREATE TABLE security_blocks (
    id UUID PRIMARY KEY,
    user_id UUID,
    target_type VARCHAR(50) NOT NULL,
    target_value VARCHAR(255) NOT NULL,
    reason_code VARCHAR(100) NOT NULL,
    blocked_until TIMESTAMP WITH TIME ZONE,
    revoked_at TIMESTAMP WITH TIME ZONE,
    created_by UUID,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_security_blocks_user_id
        FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_security_blocks_created_by
        FOREIGN KEY (created_by) REFERENCES users (id)
);

CREATE INDEX idx_security_blocks_user_id
    ON security_blocks (user_id);

CREATE INDEX idx_security_blocks_target
    ON security_blocks (target_type, target_value);

CREATE INDEX idx_security_blocks_reason_code
    ON security_blocks (reason_code);

CREATE INDEX idx_security_blocks_blocked_until
    ON security_blocks (blocked_until);

CREATE INDEX idx_security_blocks_revoked_at
    ON security_blocks (revoked_at);
