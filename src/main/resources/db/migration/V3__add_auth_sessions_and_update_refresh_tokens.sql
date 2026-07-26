-- ============================================================
-- Add auth sessions and move device/session metadata out of refresh tokens
-- ============================================================

CREATE TABLE auth_sessions (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    session_type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    device_id VARCHAR(255),
    device_name VARCHAR(255),
    ip_address INET,
    user_agent TEXT,
    last_active_at TIMESTAMP WITH TIME ZONE,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    revoked_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_auth_sessions_user_id
        FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT chk_auth_sessions_session_type
        CHECK (session_type IN ('WEB', 'MOBILE')),
    CONSTRAINT chk_auth_sessions_status
        CHECK (status IN ('ACTIVE', 'REVOKED', 'EXPIRED'))
);

CREATE INDEX idx_auth_sessions_user_id
    ON auth_sessions (user_id);

CREATE INDEX idx_auth_sessions_device_id
    ON auth_sessions (device_id);

CREATE INDEX idx_auth_sessions_status
    ON auth_sessions (status);

CREATE INDEX idx_auth_sessions_expires_at
    ON auth_sessions (expires_at);


-- Existing refresh tokens represented mobile-style sessions before auth_sessions existed.
INSERT INTO auth_sessions (
    id,
    user_id,
    session_type,
    status,
    device_name,
    ip_address,
    user_agent,
    last_active_at,
    expires_at,
    revoked_at,
    created_at,
    updated_at
)
SELECT
    id,
    user_id,
    'MOBILE',
    CASE
        WHEN revoked_at IS NOT NULL THEN 'REVOKED'
        WHEN expires_at <= CURRENT_TIMESTAMP THEN 'EXPIRED'
        ELSE 'ACTIVE'
    END,
    device_name,
    ip_address,
    user_agent,
    created_at,
    expires_at,
    revoked_at,
    created_at,
    created_at
FROM refresh_tokens;


ALTER TABLE refresh_tokens
    ADD COLUMN session_id UUID;

UPDATE refresh_tokens
SET session_id = id;

ALTER TABLE refresh_tokens
    ALTER COLUMN session_id SET NOT NULL;

ALTER TABLE refresh_tokens
    ADD CONSTRAINT fk_refresh_tokens_session_id
        FOREIGN KEY (session_id) REFERENCES auth_sessions (id);

ALTER TABLE refresh_tokens
    ADD COLUMN replaced_by_token_id UUID;

ALTER TABLE refresh_tokens
    ADD CONSTRAINT fk_refresh_tokens_replaced_by_token_id
        FOREIGN KEY (replaced_by_token_id) REFERENCES refresh_tokens (id);

CREATE INDEX idx_refresh_tokens_session_id
    ON refresh_tokens (session_id);

CREATE INDEX idx_refresh_tokens_user_id
    ON refresh_tokens (user_id);

CREATE INDEX idx_refresh_tokens_expires_at
    ON refresh_tokens (expires_at);

CREATE INDEX idx_refresh_tokens_revoked_at
    ON refresh_tokens (revoked_at);

ALTER TABLE refresh_tokens
    DROP COLUMN device_name,
    DROP COLUMN ip_address,
    DROP COLUMN user_agent;
