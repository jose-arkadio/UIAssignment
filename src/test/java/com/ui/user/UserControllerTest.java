package com.ui.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ExtendWith(SpringExtension.class)
class UserControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void shouldReturnSingleUserOnList() throws Exception {

        //given
        BasicUserDto user = givenUser();
        List<BasicUserDto> allUsers = new ArrayList<>();
        allUsers.add(user);

        //when
        when(userService.getAllUsers()).thenReturn(allUsers);

        //then
        mockMvc.perform(get("/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].acct", is(user.getAcct())))
                .andExpect(jsonPath("$[0].fullName", is(user.getFullName())))
                .andExpect(jsonPath("$[0].createdAt", is(user.getCreatedAt()
                        .toString())))
                .andExpect(jsonPath("$[0].updatedAt", is(user.getUpdatedAt()
                        .toString())))
                .andExpect(jsonPath("$[0].password").doesNotHaveJsonPath());
    }

    @Test
    public void shouldReturnAllUsers() throws Exception {

        //given
        int expectedSize = 10;
        List<BasicUserDto> allUsers = new ArrayList<>();
        for (int i = 0; i < expectedSize; i++) {
            allUsers.add(givenUser());
        }

        //when
        when(userService.getAllUsers()).thenReturn(allUsers);

        //then
        mockMvc.perform(get("/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(expectedSize)));
    }

    private static BasicUserDto givenUser() {
        BasicUserDto basicUserDto = new BasicUserDto();
        basicUserDto.setAcct(UUID.randomUUID()
                .toString());
        basicUserDto.setPassword("teST@123");
        basicUserDto.setFullName("Steven Spielberg");
        basicUserDto.setCreatedAt(LocalDateTime.now()
                .truncatedTo(ChronoUnit.MILLIS));
        basicUserDto.setUpdatedAt(LocalDateTime.now()
                .truncatedTo(ChronoUnit.MILLIS));
        return basicUserDto;
    }
}