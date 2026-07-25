-- ============================================================
-- Convert IP address columns from VARCHAR to PostgreSQL INET
-- ============================================================

-- Empty strings cannot be converted to INET.
-- Convert them to NULL before changing the column type.
UPDATE refresh_tokens
SET ip_address = NULL
WHERE ip_address IS NOT NULL
  AND BTRIM(ip_address) = '';

UPDATE security_events
SET ip_address = NULL
WHERE ip_address IS NOT NULL
  AND BTRIM(ip_address) = '';


ALTER TABLE refresh_tokens
ALTER COLUMN ip_address TYPE INET
    USING NULLIF(BTRIM(ip_address), '')::INET;


ALTER TABLE security_events
ALTER COLUMN ip_address TYPE INET
    USING NULLIF(BTRIM(ip_address), '')::INET;


-- ============================================================
-- Ensure failed_attempts cannot contain negative values
-- ============================================================

ALTER TABLE user_credentials
    ADD CONSTRAINT chk_user_credentials_failed_attempts_non_negative
        CHECK (failed_attempts >= 0)
    NOT VALID;

ALTER TABLE user_credentials
    VALIDATE CONSTRAINT
    chk_user_credentials_failed_attempts_non_negative;


ALTER TABLE login_codes
    ADD CONSTRAINT chk_login_codes_failed_attempts_non_negative
        CHECK (failed_attempts >= 0)
    NOT VALID;

ALTER TABLE login_codes
    VALIDATE CONSTRAINT
    chk_login_codes_failed_attempts_non_negative;