package com.thuyen.bakeryshop.common.ratelimit;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "app.rate-limit.auth")
public class RateLimitProperties {
    private int loginIpMaxRequests;
    private Duration loginIpWindow;
    private int loginIdentifierMaxRequests;
    private Duration loginIdentifierWindow;
    private int registerIpMaxRequests;
    private Duration registerIpWindow;

    public RateLimitRule loginIpRule() {
        return new RateLimitRule(loginIpMaxRequests, loginIpWindow);
    }

    public RateLimitRule loginIdentifierRule() {
        return new RateLimitRule(loginIdentifierMaxRequests, loginIdentifierWindow);
    }

    public RateLimitRule registerIpRule() {
        return new RateLimitRule(registerIpMaxRequests, registerIpWindow);
    }

}
