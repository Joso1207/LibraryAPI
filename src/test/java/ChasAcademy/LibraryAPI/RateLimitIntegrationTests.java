package ChasAcademy.LibraryAPI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        properties = {
                "security.enabled=false",
                "ratelimit.enabled=true"
        }
)
@AutoConfigureMockMvc
@WithMockUser
@ActiveProfiles("test")
public class RateLimitIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRateLimitRequestsFromSameIp() throws Exception {

        for (int i = 0; i < 50; i++) {

            mockMvc.perform(get("/v2/api/books")
                            .with(request -> {
                                request.setRemoteAddr("192.168.1.1");
                                return request;
                            }))
                    .andExpect(status().isOk());
        }

        mockMvc.perform(get("/v2/api/books")
                        .with(request -> {
                            request.setRemoteAddr("192.168.1.1");
                            return request;
                        }))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    void shouldUseSeparateBucketsPerIp() throws Exception {

        for (int i = 0; i < 50; i++) {

            mockMvc.perform(get("/v2/api/books")
                            .with(request -> {
                                request.setRemoteAddr("10.0.0.1");
                                return request;
                            }))
                    .andExpect(status().isOk());
        }

        mockMvc.perform(get("/v2/api/books")
                        .with(request -> {
                            request.setRemoteAddr("10.0.0.2");
                            return request;
                        }))
                .andExpect(status().isOk());
    }
}