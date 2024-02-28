package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;

@RestController
public class UserController {

  private final UserService userService;

  UserController(UserService userService) {
    this.userService = userService;
  }

  //GET MAPPINGS

  @GetMapping("/users")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<UserGetDTO> getAllUsers() {
    List<User> users = userService.getUsers();
    List<UserGetDTO> userGetDTOs = new ArrayList<>();
    for (User user : users) {
      userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
    }
    return userGetDTOs;
  }

  @GetMapping("/users/{userId}") // New endpoint for fetching a specific user by id
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserGetDTO getUserByUserId(@PathVariable Long userId) {
    User user = userService.getUserByUserId(userId);
    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
  }



//POST MAPPINGS

  @PostMapping("/users")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
    User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
    User createdUser = userService.createUser(userInput);
    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
  }

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserGetDTO loginUser(@RequestBody UserPostDTO userPostDTO) {
    User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
    User existingUser = userService.loginUser(userInput);
    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(existingUser);
  }

    //PUT MAPPING
    @PutMapping("/users/{userId}") // New endpoint for updating user data by user id
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO updateUser(@PathVariable Long userId, @RequestBody UserPostDTO userPostDTO) {
        // Retrieve the user from the database by userId
        User existingUser = userService.getUserByUserId(userId);

        // Check if the new username is already in use by another user
        User userWithNewUsername = userService.getUserByUsername(userPostDTO.getUsername());
        if (userWithNewUsername != null && !userWithNewUsername.getUserId().equals(existingUser.getUserId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The username provided is not unique. Therefore, the username could not be changed!");
        }

        // Update the user data with the new username and birthday
        existingUser.setUsername(userPostDTO.getUsername());
        existingUser.setBirthday(userPostDTO.getBirthday());

        // Save the updated user data
        userService.updateUser(existingUser);

        // Convert and return the updated user DTO
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(existingUser);
    }
    @PutMapping("/users/status/{userId}") // New endpoint for updating user data by user id
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO updateUserStatus(@PathVariable Long userId, @RequestBody UserPostDTO userPostDTO) {
        // Retrieve the user from the database by userId
        User existingUser = userService.getUserByUserId(userId);

        if (existingUser != null) {
            existingUser.setStatus(userPostDTO.getStatus());
        }

        userService.updateUserStatus(existingUser);

        // Convert and return the updated user DTO
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(existingUser);
    }


}
