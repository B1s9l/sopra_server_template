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

@RestController
public class UserController {

  private final UserService userService;

  UserController(UserService userService) {
    this.userService = userService;
  }

  //GET MAPPINGS

  @PostMapping("/getusers")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<UserGetDTO> getUsers(@RequestBody(required = false) String token) {
      if (token != null) {
          token = token.replaceAll("\"", "");
          if (userService.checkIfTokenMaster(token)){
              List<User> users = userService.getAllUsers();
              List<UserGetDTO> userGetDTOs = new ArrayList<>();
              for (User user : users) {
                  userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
              }
              return userGetDTOs;
          }
          else if (userService.checkIfTokenValid(token)) {
              List<User> users = userService.getUsers(token);
              List<UserGetDTO> userGetDTOs = new ArrayList<>();
              for (User user : users) {
                  userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
              }
              return userGetDTOs;
          } else {
              throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The provided token is invalid! Try restarting the session!");
          }

      }
      List<User> users = userService.getUsers();
      List<UserGetDTO> userGetDTOs = new ArrayList<>();
      for (User user : users) {
          userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
      }
      return userGetDTOs;
  }
//CHECKPOINT
  @GetMapping("/users/{userId}") // New endpoint for fetching a specific user by id
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserGetDTO getUserByUserId(@PathVariable Long userId, @RequestBody String token) {
      if (token != null) { //THIS LOOP IS ENTERED IN THE TEST; A TOKEN IS THEREFORE SENT SUCCESSFULLY
          token = token.replaceAll("\"", ""); //THIS LINE IS ENTERED AS IT SHOULD
          boolean isMaster = userService.checkIfTokenMaster(token); //THIS FUNCTION IS NEVER CALLED
          boolean isValid = userService.checkIfTokenValid(token); //THIS FUNCTION IS NEVER CALLED
          if (isMaster){ //THIS LOOP IS SKIPPED BECAUSE FUNCTION ABOVE IS NOT CALLED
              User user = userService.getUserByUserIdMaster(userId);
              return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
          }
          else if (isValid) { //THIS LOOP IS SKIPPED BECAUSE FUNCTION ABOVE IS NOT CALLED
              User user = userService.getUserByUserIdBasic(userId);
              return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
          } else { //THIS ELSE IS ENTERED AND EXCEPTION THROWN EVEN IF ITS NOT SUPPOSED TOO
              throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The provided token is invalid! Try restarting the session!");
          }

      }
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The provided token is invalid! Try restarting the session!");
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

  @PostMapping("/logout")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserGetDTO logoutUser(@RequestBody String token) {
      User existingUser = userService.logoutUser(token);
      return DTOMapper.INSTANCE.convertEntityToUserGetDTO(existingUser);
  }


    //PUT MAPPING
    @PutMapping("/users/{userId}") // New endpoint for updating user data by user id
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO updateUser(@PathVariable Long userId, @RequestBody UserPostDTO userPostDTO) {
        // Retrieve the user from the database by userId
        User existingUser = userService.getUserByUserIdBasic(userId);

        // Check if the new username is already in use by another user
        User userWithNewUsername = userService.getUserByUsername(userPostDTO.getUsername());
        if (userWithNewUsername != null && !userWithNewUsername.getUserId().equals(existingUser.getUserId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The username provided is not unique. Therefore, the username could not be changed!");
        }

        // Update the user data with the new username and birthday
        existingUser.setUsername(userPostDTO.getUsername());
        existingUser.setBirthday(userPostDTO.getBirthday());

        // Save the updated user data
        userService.updateUser(existingUser);

        // Convert and return the updated user DTO
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(existingUser);
    }

}
