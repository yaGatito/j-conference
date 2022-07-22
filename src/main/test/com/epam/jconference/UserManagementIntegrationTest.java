package com.epam.jconference;

import com.epam.jconference.controller.model.UserModel;
import com.epam.jconference.dto.UserDto;
import com.epam.jconference.model.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.net.URI;
import java.util.List;
import java.util.Locale;

import static com.epam.jconference.test.utils.TestUserDataUtil.createUserDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserManagementIntegrationTest {

    @Value("http://localhost:${local.server.port}/api/v1/users")
    private String baseUrl;

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * For testRestTemplate initialization. Need to supporting PATCH method.
     */
    @BeforeTestClass
    public void setup() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
    void createUser() {
        String email = "newemail@email.com";
        UserDto userDto = createUserDto();
        userDto.setEmail(email);

        ResponseEntity<UserModel> responseEntity = restTemplate.exchange(baseUrl, HttpMethod.POST,
                new RequestEntity<>(userDto, HttpMethod.POST, URI.create(baseUrl)), UserModel.class);

        assertNotNull(responseEntity.getBody().getUserDto().getId());
        assertEquals(responseEntity.getBody().getUserDto().getEmail(), email);

        Long id = responseEntity.getBody().getUserDto().getId();
        ResponseEntity<UserModel> responseEntity2 = restTemplate.exchange(baseUrl + "/" + email, HttpMethod.GET, null
                , new ParameterizedTypeReference<UserModel>() {
                });

        assertEquals(responseEntity2.getBody().getUserDto().getEmail(), email);
    }

    @Test
    void updateUserTest() {
        String email = "email@gr.com";
        String lastname = "greeeeekov";

        UserDto userDto = UserDto.builder().email(email).lastname(lastname).build();

        ResponseEntity<UserModel> responseEntity = restTemplate.exchange(baseUrl, HttpMethod.PATCH,
                new RequestEntity<>(userDto, HttpMethod.PATCH, URI.create(baseUrl)), UserModel.class);

        ResponseEntity<UserModel> responseEntity2 = restTemplate.exchange(baseUrl + "/" + email, HttpMethod.GET, null
                , new ParameterizedTypeReference<UserModel>() {
                });

        assertEquals(responseEntity2.getBody().getUserDto().getEmail(), responseEntity.getBody()
                                                                                      .getUserDto()
                                                                                      .getEmail());
        assertEquals(responseEntity2.getBody().getUserDto().getLastname(), responseEntity.getBody()
                                                                                         .getUserDto()
                                                                                         .getLastname());
        assertEquals(responseEntity2.getBody().getUserDto().getId(), responseEntity.getBody().getUserDto().getId());
    }

    @Test
    void loginTest() {
        String email = "email@pe.com";
        String password = "1234";

        UserDto userDto = UserDto.builder().email(email).password(password).build();

        ResponseEntity<UserModel> loginUser = restTemplate.exchange(baseUrl + "/login", HttpMethod.POST,
                new RequestEntity<>(userDto, HttpMethod.POST, URI.create(baseUrl + "/login")), UserModel.class);

    }

    @Test
    void logoutTest() {
        restTemplate.exchange(baseUrl + "/logout", HttpMethod.POST, null,
                Void.class);
    }

    @Test
    void getUsers() {
        String userRole = "user";
        ResponseEntity<List<UserDto>> response = restTemplate.exchange(baseUrl + "/role/" + userRole, HttpMethod.GET,
                null, new ParameterizedTypeReference<List<UserDto>>() {
                });
        //3 users in data-h2.sql and 1 in tests
        assertEquals(response.getBody().size(), (int) response.getBody()
                                                              .stream()
                                                              .filter(l -> l.getRole()
                                                                            .equals(UserRole.valueOf(userRole.toUpperCase(Locale.ROOT))))
                                                              .count());
    }

    @Test
    void getByEmailTest() {
        String email = "email@gr.com";

        ResponseEntity<UserModel> response = restTemplate.exchange(baseUrl + "/" + email, HttpMethod.GET, null
                , new ParameterizedTypeReference<UserModel>() {
                });

        assertNotNull(response.getBody().getUserDto());
        assertEquals(response.getBody().getUserDto().getEmail(), email);
        assertNotNull(response.getBody().getUserDto().getId());
        assertNotNull(response.getBody().getUserDto().getRole());
        assertNotNull(response.getBody().getUserDto().getLastname());
        assertNotNull(response.getBody().getUserDto().getName());
        assertNotNull(response.getBody().getUserDto().getNotifications());
    }

    @Test
    void setUserRoleTest() {
        String userRole = "speaker";
        String email = "email@ne.com";
        ResponseEntity<UserModel> response =
                restTemplate.exchange(baseUrl + "/role?role=" + userRole + "&email=" + email, HttpMethod.PATCH, null
                , new ParameterizedTypeReference<UserModel>() {
                });

        assertEquals(response.getBody().getUserDto().getEmail(), email);
        assertEquals(response.getBody().getUserDto().getRole().name(), userRole.toUpperCase());
    }
}
