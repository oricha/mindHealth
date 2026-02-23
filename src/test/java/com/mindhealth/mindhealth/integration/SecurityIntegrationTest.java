package com.mindhealth.mindhealth.integration;

import com.mindhealth.mindhealth.security.RateLimitFilter;
import com.mindhealth.mindhealth.util.PasswordPolicy;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SecurityIntegrationTest {

    @Test
    void shouldRejectWeakPassword() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> PasswordPolicy.validate("weakpass"));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void rateLimitShouldReturnTooManyRequests() throws Exception {
        RateLimitFilter filter = new RateLimitFilter();

        for (int i = 0; i < 20; i++) {
            MockHttpServletRequest req = new MockHttpServletRequest("POST", "/api/auth/login");
            req.setRemoteAddr("127.0.0.1");
            MockHttpServletResponse res = new MockHttpServletResponse();
            filter.doFilter(req, res, new MockFilterChain());
        }

        MockHttpServletRequest req = new MockHttpServletRequest("POST", "/api/auth/login");
        req.setRemoteAddr("127.0.0.1");
        MockHttpServletResponse res = new MockHttpServletResponse();
        filter.doFilter(req, res, new MockFilterChain());

        assertEquals(429, res.getStatus());
    }
}
