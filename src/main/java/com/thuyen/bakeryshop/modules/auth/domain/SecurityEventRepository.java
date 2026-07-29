package com.thuyen.bakeryshop.modules.auth.domain;

import com.thuyen.bakeryshop.modules.auth.domain.model.SecurityEvent;

public interface SecurityEventRepository {
    SecurityEvent save(SecurityEvent securityEvent);
}
