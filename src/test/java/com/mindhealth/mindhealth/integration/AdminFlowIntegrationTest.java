package com.mindhealth.mindhealth.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindhealth.mindhealth.config.CorrelationIdFilter;
import com.mindhealth.mindhealth.model.CategoryDTO;
import com.mindhealth.mindhealth.rest.admin.AdminCategoryResource;
import com.mindhealth.mindhealth.security.JwtAuthenticationFilter;
import com.mindhealth.mindhealth.security.JwtTokenProvider;
import com.mindhealth.mindhealth.security.RateLimitFilter;
import com.mindhealth.mindhealth.service.CategoryService;
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
@WebMvcTest(controllers = AdminCategoryResource.class,
        excludeAutoConfiguration = org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = com.mindhealth.mindhealth.config.ControllerConfig.class))
@AutoConfigureMockMvc(addFilters = false)
class AdminFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    void createCategoryShouldReturnCreated() throws Exception {
        CategoryDTO dto = new CategoryDTO();
        dto.setName("Meditation");

        when(categoryService.create(any(CategoryDTO.class))).thenReturn(10L);

        mockMvc.perform(post("/api/admin/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }
}
