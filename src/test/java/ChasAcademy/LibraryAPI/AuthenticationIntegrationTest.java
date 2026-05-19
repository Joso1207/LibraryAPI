package ChasAcademy.LibraryAPI;

import ChasAcademy.LibraryAPI.api.core.dto.UserRequest;
import ChasAcademy.LibraryAPI.persistence.model.AppUser;
import ChasAcademy.LibraryAPI.persistence.repository.UserRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository repo;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    public void setup(){
        repo.deleteAll();
    }

    @Test
    public void userCanRegister() throws Exception {

        UserRequest request = UserRequest.builder()
                .username("TestUser")
                .password("TestPass")
                .build();

        mockMvc.perform(post("/v1/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());


        repo.findAll().forEach(System.out::println);
        Optional<AppUser> savedUser =
                repo.findByUsername("TestUser");

        assertTrue(savedUser.isPresent());

        AppUser user = savedUser.get();

        assertEquals("TestUser", user.getUsername());

        assertNotEquals("TestPass", user.getPassword());

        assertTrue(
                encoder.matches(
                        "TestPass",
                        user.getPassword()
                )
        );
    }


    @Test
    void login_shouldReturnJwtToken() throws Exception {

        AppUser newUser = AppUser.builder()
                .username("testuser")
                .password(encoder.encode("password"))
                .role("USER")
                .build();

        repo.save(newUser);

        UserRequest request = new UserRequest(
                "testuser",
                "password"
        );

        mockMvc.perform(post("/v1/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isString());
    }

    @Test
    void authenticatedRequest_withValidJwt_shouldSucceed() throws Exception {

        AppUser newUser = AppUser.builder()
                .username("testuser")
                .password(encoder.encode("password"))
                .role("USER")
                .build();

        repo.save(newUser);

        UserRequest request = new UserRequest(
                "testuser",
                "password"
        );

        MvcResult loginResult = mockMvc.perform(post("/v1/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = loginResult.getResponse().getContentAsString();

        String token = JsonPath.read(responseBody, "$.token");

        mockMvc.perform(get("/v1/api/books")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void authenticatedRequest_withoutJwt_shouldFail() throws Exception {

        mockMvc.perform(get("/v1/api/books"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void authenticatedRequest_withInvalidJwt_shouldFail() throws Exception {

        mockMvc.perform(get("/v1/api/books")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer invalid-token"))
                .andExpect(status().isUnauthorized());
    }
}