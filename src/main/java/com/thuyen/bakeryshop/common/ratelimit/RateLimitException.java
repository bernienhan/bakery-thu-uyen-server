package com.thuyen.bakeryshop.common.ratelimit;

import com.thuyen.bakeryshop.common.exception.ApiException;
import com.thuyen.bakeryshop.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class RateLimitException extends ApiException {
    private final RateLimitResult result;

    public RateLimitException(RateLimitResult result) {
        super(ErrorCode.TOO_MANY_REQUESTS);
        this.result = result;
    }
}
