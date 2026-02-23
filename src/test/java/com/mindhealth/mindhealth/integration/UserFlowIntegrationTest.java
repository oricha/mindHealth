package com.mindhealth.mindhealth.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindhealth.mindhealth.config.CorrelationIdFilter;
import com.mindhealth.mindhealth.model.UserDTO;
import com.mindhealth.mindhealth.rest.UserResource;
import com.mindhealth.mindhealth.security.JwtAuthenticationFilter;
import com.mindhealth.mindhealth.security.JwtTokenProvider;
import com.mindhealth.mindhealth.security.RateLimitFilter;
import com.mindhealth.mindhealth.service.CategoryService;
import com.mindhealth.mindhealth.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("removal")
@WebMvcTest(controllers = UserResource.class,
        excludeAutoConfiguration = org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = com.mindhealth.mindhealth.config.ControllerConfig.class))
@AutoConfigureMockMvc(addFilters = false)
class UserFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private RateLimitFilter rateLimitFilter;

    @MockBean
    private CorrelationIdFilter correlationIdFilter;

    @Test
    void registerUserShouldReturnCreated() throws Exception {
        UserDTO dto = new UserDTO();
        dto.setEmail("user@example.com");
        dto.setName("User Name");
        dto.setPassword("Strong1!");

        when(userService.registerUser(any(UserDTO.class))).thenReturn(1L);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }
}
