package com.walters.uservault_api;

import com.walters.uservault_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class UserVaultApiApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateUser() throws Exception {
        String json = """
                {
                  "firstName": "William",
                  "lastName": "Walters",
                  "email": "william@example.com",
                  "role": "Software Engineer"
                }
                """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("William"))
                .andExpect(jsonPath("$.email").value("william@example.com"));
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        String json = """
                {
                  "firstName": "William",
                  "lastName": "Walters",
                  "email": "william@example.com",
                  "role": "Software Engineer"
                }
                """;

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void shouldReturn404ForMissingUser() throws Exception {
        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found with id: 999"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        String json = """
                {
                  "firstName": "William",
                  "lastName": "Walters",
                  "email": "delete@example.com",
                  "role": "Software Engineer"
                }
                """;

        String response = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String id = response.replaceAll(".*\"id\":(\\d+).*", "$1");

        mockMvc.perform(delete("/api/users/" + id))
                .andExpect(status().isNoContent());
    }
}
