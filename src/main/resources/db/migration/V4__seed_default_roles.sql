INSERT INTO roles (
    id,
    code,
    name,
    description,
    created_at,
    updated_at
)
VALUES
(
    '7f6d4c3a-2a1b-4f8e-9c0d-5b6a7e8f9a01',
    'ADMIN',
    'Admin',
    'Administrator role',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
),
(
    '9a8b7c6d-5e4f-4a3b-9c2d-1e0f8a7b6c5d',
    'USER',
    'User',
    'Default user role',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
)
ON CONFLICT (code) DO NOTHING;
