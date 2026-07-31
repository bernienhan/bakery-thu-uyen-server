INSERT INTO roles (
    id,
    code,
    name,
    description,
    created_at,
    updated_at
)
VALUES (
    '11111111-1111-1111-1111-111111111111',
    'CUSTOMER',
    'Customer',
    'Default customer role',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
)
ON CONFLICT (code) DO NOTHING;
