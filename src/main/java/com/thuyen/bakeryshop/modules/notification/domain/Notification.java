package com.thuyen.bakeryshop.modules.notification.domain;

import java.util.UUID;

public record Notification(
        UUID id,
        String recipient,
        String message
) {
}
