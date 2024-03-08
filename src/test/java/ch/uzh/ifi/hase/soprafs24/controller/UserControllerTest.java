package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;


/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //TEST POST /getusers [get all users and return them as an array]
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  @Test
  public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
    // given
    User user = new User();

    user.setUsername("firstname@lastname");
    user.setStatus(UserStatus.OFFLINE);

    List<User> allUsers = Collections.singletonList(user);

    // this mocks the UserService -> we define above what the userService should
    // return when getUsers() is called
    given(userService.getUsers()).willReturn(allUsers);

    // when
    MockHttpServletRequestBuilder postRequest = post("/getusers").contentType(MediaType.APPLICATION_JSON);

    // then
    mockMvc.perform(postRequest).andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].username", is(user.getUsername())))
        .andExpect(jsonPath("$[0].status", is(user.getStatus().toString())));
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //TEST POST /users [create a new user] (1 OK, 1 INVALID USERNAME, 1 DUPLICATE USERNAME)
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //CREATE USER OK
  @Test
  public void createUser_validInput_userCreated() throws Exception {
    // given
    User user = new User();
    user.setUserId(1L);
    user.setUsername("testUsername");
    user.setToken("1");
    user.setStatus(UserStatus.ONLINE);

    UserPostDTO userPostDTO = new UserPostDTO();
    userPostDTO.setUsername("testUsername");

    given(userService.createUser(Mockito.any())).willReturn(user);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userPostDTO));

    // then
    mockMvc.perform(postRequest)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.userId", is(user.getUserId().intValue())))
        .andExpect(jsonPath("$.username", is(user.getUsername())));
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //CREATE USER INVALID USERNAME [prohibited words cant be chosen]
    @Test
    public void createUser_invalidInput_usernameInvalid() throws Exception {
        // given
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("settings");

        given(userService.createUser(Mockito.any()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The username provided is not valid"));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isNotAcceptable());
    }

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //CREATE USER DUPLICATE USERNAME [a taken username cant be chosen]
    @Test
    public void createUser_invalidInput_usernameTaken() throws Exception {
        // given
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("username");
        userPostDTO.setPassword("password");

        given(userService.createUser(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isConflict());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //TEST GET /users/{userId} [retrieve a specific user based on the user id] (1 OK, 1 ID not found, 1 Invalid token )
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //FIND USER SUCCESS
    @Test
    public void getUser_validId_userFound() throws Exception {
        // given
        User user = new User();
        user.setUserId(1L);
        user.setUsername("username");

        given(userService.checkIfTokenMaster("mastercode")).willReturn(true);
        given(userService.getUserByUserIdMaster(1L)).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/users/{userId}", user.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("mastercode");

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(user.getUserId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //FIND USER INVALID USER ID [user cant be found because the provided user id doesnt exist]
    @Test
    public void getUser_invalidId_userNotFound() throws Exception {
        // given
        User user = new User();
        user.setUserId(1L);
        user.setUsername("username");

        given(userService.checkIfTokenMaster("mastercode")).willReturn(true);
        given(userService.getUserByUserIdMaster(Mockito.any()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "The user with the provided userId was not found!"));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/users/{userId}", 2L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("mastercode");

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //FIND USER INVALID TOKEN [getting a user requires a token, which is wrong in this case]
    @Test
    public void getUser_invalidtoken_tokenNotAccepted() throws Exception {
        // given
        User user = new User();
        user.setUserId(1L);
        user.setUsername("username");

        given(userService.checkIfTokenMaster("mastercode")).willReturn(true);
        given(userService.checkIfTokenMaster(Mockito.any()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "The user with the provided userId was not found!"));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/users/{userId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("notMastercode");

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //TEST PUT /users/{userId} [CHANGE A USERS USERNAME OR BIRTHDAY] (1 user updated, 1 user not found, 1 username already exists)
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //TEST UPDATE USER SUCCESS

    //Here I wasn't sure from the assignment if we are supposed to use 204 or something smaller than 204
    //204 No content doesnt make sense to me since i return (and i presume everybody does) the updated user profile to the client
    //204 would only make sense if we dont return anything to my understanding
    @Test
    public void updateUser_validId_userUpdated() throws Exception {
        // given
        User user = new User();
        user.setUserId(1L);
        user.setUsername("oldUsername");
        user.setPassword("password");

        User updatedUser = new User();
        updatedUser.setUserId(user.getUserId());
        updatedUser.setUsername("newUsername");
        updatedUser.setBirthday(LocalDate.now());

        given(userService.getUserByUserIdBasic(user.getUserId())).willReturn(user);
        given(userService.getUserByUsername(user.getUsername())).willReturn(user);
        given(userService.updateUser(Mockito.any())).willReturn(updatedUser);

        String requestInput = "{\"username\": \"" + updatedUser.getUsername() + "\", \"birthday\": \"" + updatedUser.getBirthday() + "\"}";

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/users/" + user.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestInput);

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(updatedUser.getUsername())))
                .andExpect(jsonPath("$.birthday", is(updatedUser.getBirthday().toString())));
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //TEST UPDATE USER INVALID ID [the provided id is wrong and the user which is to be updated cant be found]
    @Test
    public void updateUser_invalidId_userNotUpdated() throws Exception {
        // given
        User user = new User();
        user.setUserId(1L);
        user.setUsername("oldUsername");
        user.setPassword("password");

        User updatedUser = new User();
        updatedUser.setUserId(user.getUserId());
        updatedUser.setUsername("newUsername");
        updatedUser.setBirthday(LocalDate.now());

        given(userService.getUserByUserIdBasic(Mockito.any()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "The user with the provided userId was not found!"));
        given(userService.getUserByUsername(user.getUsername())).willReturn(user);
        given(userService.updateUser(Mockito.any())).willReturn(updatedUser);

        String requestInput = "{\"username\": \"" + updatedUser.getUsername() + "\", \"birthday\": \"" + updatedUser.getBirthday() + "\"}";
        System.out.println(requestInput);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/users/" + 2L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestInput);

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isNotFound());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //TEST UPDATE USER INVALID USERNAME [the username which is chosen is invalid or taken]

    @Test
    public void updateUser_invalidUsername_userNotUpdated() throws Exception {
        // given
        User user = new User();
        user.setUserId(1L);
        user.setUsername("oldUsername");
        user.setPassword("password");

        User updatedUser = new User();
        updatedUser.setUserId(user.getUserId());
        updatedUser.setUsername("newUsername");
        updatedUser.setBirthday(LocalDate.now());

        given(userService.getUserByUserIdBasic(user.getUserId())).willReturn(user);
        given(userService.getUserByUsername(user.getUsername())).willReturn(user);
        given(userService.updateUser(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT));

        String requestInput = "{\"username\": \"" + updatedUser.getUsername() + "\", \"birthday\": \"" + updatedUser.getBirthday() + "\"}";
        System.out.println(requestInput);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/users/" + user.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestInput);

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isConflict());
    }

  /**
   * Helper Method to convert userPostDTO into a JSON string such that the input
   * can be processed
   * Input will look like this: {"name": "Test User", "username": "testUsername"}
   * 
   * @param object
   * @return string
   */
  private String asJsonString(final Object object) {
    try {
      return new ObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format("The request body could not be created.%s", e.toString()));
    }
  }
}