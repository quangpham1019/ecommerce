package org.ecommerce.UnitTests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ecommerce.Config.TestSecurityConfig;
import org.ecommerce.user.api.controller.UserController;
import org.ecommerce.user.application.dto.UserCreateDTO;
import org.ecommerce.user.application.dto.UserProfileDTO;
import org.ecommerce.user.application.dto.UserResponseDTO;
import org.ecommerce.user.application.service.UserApplicationService;
import org.ecommerce.user.domain.model.Email;
import org.ecommerce.user.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = {UserController.class, TestSecurityConfig.class}) // Override the @Import on main class
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserApplicationService userApplicationService;

    @MockitoSpyBean
    private ObjectMapper objectMapper;

    private User user;
    private UserCreateDTO userCreateDTO;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    public void setUp() {
        user = new User("John Doe", "Zaq12wsx@j", new Email("john.doe@example.com"));
        userCreateDTO = new UserCreateDTO("John Doe", "Zaq12wsx@j", new Email("john.doe@example.com"));
        userResponseDTO = new UserResponseDTO(1L, "John Doe", "john.doe@example.com");
    }

    @Test
    public void getUsers_ShouldReturnAllUsers() throws Exception {
        List<User> users = Arrays.asList(user);
        when(userApplicationService.getUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("John Doe"))
                .andExpect(jsonPath("$[0].email.address").value("john.doe@example.com"));

        verify(userApplicationService, times(1)).getUsers();
    }

    @Test
    public void getUserProfile_ShouldReturnUserProfileDTO_IfUserExists() throws Exception {
        Long userId = 1L;
        List<String> roles = Arrays.asList("ADMIN", "USER");
        UserProfileDTO userProfileDTO = new UserProfileDTO(1L, "John Doe", "john.doe@example.com", roles);
        when(userApplicationService.getUserProfile(userId)).thenReturn(userProfileDTO);

        mockMvc.perform(get("/api/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.roleNames", containsInAnyOrder("ADMIN", "USER")));

        verify(userApplicationService, times(1)).getUserProfile(userId);
    }

    @Test
    public void registerUser_ShouldReturnRegisteredUser_WhenUserInputIsValid() throws Exception {
        when(userApplicationService.registerUser(any(UserCreateDTO.class))).thenReturn(userResponseDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(userApplicationService, times(1)).registerUser(any(UserCreateDTO.class));
    }

    @Test
    public void deleteUser_ShouldRemoveUserFromDatabase_WhenUserExists() throws Exception {
        long userId = 1L;
        doNothing().when(userApplicationService).deleteById(userId);

        mockMvc.perform(delete("/api/users/{userId}", userId))
                .andExpect(status().isNoContent());

        verify(userApplicationService, times(1)).deleteById(userId);
    }

    @Test
    public void updateUser_ShouldReturnUpdatedUser_WhenUserExists_AndUserInputIsValid() throws Exception {
        UserResponseDTO userResponseDto = new UserResponseDTO(1L, "John Doe Updated", "john.doe.updated@example.com");
        when(userApplicationService.updatePartial(anyLong(), any(UserCreateDTO.class))).thenReturn(userResponseDto);

        mockMvc.perform(put("/api/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("John Doe Updated"))
                .andExpect(jsonPath("$.email").value("john.doe.updated@example.com"));

        verify(userApplicationService, times(1)).updatePartial(eq(1L), any(UserCreateDTO.class));
    }

    @Test
    public void registerUsers_ShouldReturnListOfRegisteredUsers_WhenAllUserInputsAreValid() throws Exception {
        List<User> users = Arrays.asList(user);
        when(userApplicationService.registerUsers(anyList())).thenReturn(users);

        mockMvc.perform(post("/api/users/saveAll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(users)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("John Doe"))
                .andExpect(jsonPath("$[0].email.address").value("john.doe@example.com"));

        verify(userApplicationService, times(1)).registerUsers(anyList());
    }
}
