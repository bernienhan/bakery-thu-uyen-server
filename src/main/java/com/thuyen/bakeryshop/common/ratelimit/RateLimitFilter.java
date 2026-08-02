package com.thuyen.bakeryshop.common.ratelimit;

import com.thuyen.bakeryshop.common.constant.ApiV1Paths;
import com.thuyen.bakeryshop.common.constant.RedisKeys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

@Component
public class RateLimitFilter extends OncePerRequestFilter {
    private static final String LOGIN_PATH = ApiV1Paths.Auth.BASE + ApiV1Paths.Auth.LOGIN;
    private static final String REGISTER_PATH = ApiV1Paths.Auth.BASE + ApiV1Paths.Auth.REGISTER;
    private static final String POST = "POST";
    private static final String X_FORWARDED_FOR = "X-Forwarded-For";

    private final RateLimitService rateLimitService;
    private final RateLimitProperties properties;
    private final JsonMapper jsonMapper;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public RateLimitFilter(
            RateLimitService rateLimitService,
            RateLimitProperties properties,
            JsonMapper jsonMapper,
            HandlerExceptionResolver handlerExceptionResolver
    ) {
        this.rateLimitService = rateLimitService;
        this.properties = properties;
        this.jsonMapper = jsonMapper;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        HttpServletRequest requestToUse = request;
        try {
            if (isLoginRequest(request)) {
                CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(request);
                checkLogin(cachedRequest);
                requestToUse = cachedRequest;
            } else if (isRegisterRequest(request)) {
                checkRegister(request);
            }
        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
            return;
        }

        filterChain.doFilter(requestToUse, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return !isLoginRequest(request) && !isRegisterRequest(request);
    }

    private void checkLogin(CachedBodyHttpServletRequest request) {
        String ipAddress = clientIp(request);
        rateLimitService.check(
                RedisKeys.Auth.RateLimit.loginIp(ipAddress),
                properties.loginIpRule()
        );

        readLoginIdentifier(request).ifPresent(identifier -> rateLimitService.check(
                RedisKeys.Auth.RateLimit.loginIdentifier(identifier),
                properties.loginIdentifierRule()
        ));
    }

    private void checkRegister(HttpServletRequest request) {
        rateLimitService.check(
                RedisKeys.Auth.RateLimit.registerIp(clientIp(request)),
                properties.registerIpRule()
        );
    }

    private Optional<String> readLoginIdentifier(CachedBodyHttpServletRequest request) {
        try {
            LoginRateLimitRequest body = jsonMapper.readValue(request.bodyAsString(), LoginRateLimitRequest.class);
            return normalize(body.identifier());
        } catch (JacksonException exception) {
            return Optional.empty();
        }
    }

    // Only normalize string
    private Optional<String> normalize(String value) {
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(value.trim().toLowerCase(Locale.ROOT));
    }

    private String clientIp(HttpServletRequest request) {
        return firstForwardedIp(request.getHeader(X_FORWARDED_FOR))
                .or(() -> header(request))
                .orElse(request.getRemoteAddr());
    }

    private Optional<String> firstForwardedIp(String value) {
        return headerValue(value).map(header -> header.split(",")[0].trim());
    }

    private Optional<String> header(HttpServletRequest request) {
        return headerValue(request.getHeader("X-Real-IP"));
    }

    private Optional<String> headerValue(String value) {
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(value.trim());
    }

    private boolean isLoginRequest(HttpServletRequest request) {
        return POST.equals(request.getMethod()) && LOGIN_PATH.equals(request.getRequestURI());
    }

    private boolean isRegisterRequest(HttpServletRequest request) {
        return POST.equals(request.getMethod()) && REGISTER_PATH.equals(request.getRequestURI());
    }

    private record LoginRateLimitRequest(String identifier) {
    }
}
