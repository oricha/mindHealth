package com.mindhealth.mindhealth.integration;

import com.mindhealth.mindhealth.config.CorrelationIdFilter;
import com.mindhealth.mindhealth.model.EventDTO;
import com.mindhealth.mindhealth.rest.SearchResource;
import com.mindhealth.mindhealth.security.JwtAuthenticationFilter;
import com.mindhealth.mindhealth.security.JwtTokenProvider;
import com.mindhealth.mindhealth.security.RateLimitFilter;
import com.mindhealth.mindhealth.service.CategoryService;
import com.mindhealth.mindhealth.service.SearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("removal")
@WebMvcTest(controllers = SearchResource.class,
        excludeAutoConfiguration = org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = com.mindhealth.mindhealth.config.ControllerConfig.class))
@AutoConfigureMockMvc(addFilters = false)
class SearchFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchService searchService;

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
    void searchEndpointShouldReturnOk() throws Exception {
        EventDTO dto = new EventDTO();
        dto.setId(1L);
        dto.setTitle("Meditation event");
        dto.setPrice(BigDecimal.TEN);
        dto.setDateTime(OffsetDateTime.now().plusDays(1));
        dto.setAvailableSeats(10);
        dto.setOrganizer(1L);

        when(searchService.search("meditation", null, null, 0, 20)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/events/search").param("q", "meditation"))
                .andExpect(status().isOk());
    }
}
