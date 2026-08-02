package com.thuyen.bakeryshop.support;

import com.thuyen.bakeryshop.common.exception.ApiException;
import com.thuyen.bakeryshop.common.exception.ErrorCode;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public abstract class BaseUnitTest {

    protected ApiException assertApiException(ErrorCode expectedCode, Runnable action) {
        ApiException exception = assertThrows(ApiException.class, action::run);
        assertThat(exception.getErrorCode()).isEqualTo(expectedCode);
        return exception;
    }
}
