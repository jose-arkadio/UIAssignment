package com.ui.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
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

    @BeforeEach
    public void setUp() {
        Mockito.reset(userService);
    }

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
                .andExpect(jsonPath("$[0].createdAt", is(String.valueOf(user.getCreatedAt()))))
                .andExpect(jsonPath("$[0].updatedAt", is(String.valueOf(user.getUpdatedAt()))))
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

    @Test
    public void shouldFoundSingleUserByFullname() throws Exception {

        //given
        String nameToFound = "Steven Spielberg";
        BasicUserDto userToFound = givenUserWithFullName(nameToFound);

        List<BasicUserDto> foundUsers = List.of(userToFound);

        //when
        when(userService.findUsersByFullName(nameToFound)).thenReturn(foundUsers);

        //then
        mockMvc.perform(get("/users").param("fullName", nameToFound)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].acct", is(userToFound.getAcct())))
                .andExpect(jsonPath("$[0].fullName", is(nameToFound)))
                .andExpect(jsonPath("$[0].createdAt", is(String.valueOf(userToFound.getCreatedAt()))))
                .andExpect(jsonPath("$[0].updatedAt", is(String.valueOf(userToFound.getUpdatedAt()))))
                .andExpect(jsonPath("$[0].password").doesNotHaveJsonPath());
    }

    @Test
    public void shouldFoundSingleUserDetails() throws Exception {

        //given
        BasicUserDto userToFound = givenUser();

        //when
        when(userService.findUserByAcct(userToFound.getAcct())).thenReturn(userToFound);

        //then
        mockMvc.perform(get("/users/" + userToFound.getAcct())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.acct", is(userToFound.getAcct())))
                .andExpect(jsonPath("$.fullName", is(userToFound.getFullName())))
                .andExpect(jsonPath("$.createdAt", is(String.valueOf(userToFound.getCreatedAt()))))
                .andExpect(jsonPath("$.updatedAt", is(String.valueOf(userToFound.getUpdatedAt()))))
                .andExpect(jsonPath("$.password").doesNotHaveJsonPath());
    }

    @Test
    public void shouldNotFoundSingleUserDetails() throws Exception {

        //given
        String customMessage = "User not found";
        String acct = givenUser().getAcct();

        //when
        when(userService.findUserByAcct(acct)).thenThrow(new ResourceNotFoundException(customMessage));

        //then
        mockMvc.perform(get("/users/" + acct)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(customMessage)));
    }

    private static BasicUserDto givenUser() {
        return givenUserWithFullName("Steven Spielberg");
    }

    private static BasicUserDto givenUserWithFullName(String fullname) {
        BasicUserDto basicUserDto = new BasicUserDto();
        basicUserDto.setAcct(UUID.randomUUID()
                .toString());
        basicUserDto.setPassword("teST@123");
        basicUserDto.setFullName(fullname);
        basicUserDto.setCreatedAt(LocalDateTime.now()
                .truncatedTo(ChronoUnit.MILLIS));
        basicUserDto.setUpdatedAt(LocalDateTime.now()
                .truncatedTo(ChronoUnit.MILLIS));
        return basicUserDto;
    }
}