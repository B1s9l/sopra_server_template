package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import java.time.LocalDate;

import java.util.Arrays;
import java.util.ArrayList;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class UserService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  @Autowired
  public UserService(@Qualifier("userRepository") UserRepository userRepository) {
    this.userRepository = userRepository;
  }

    public List<User> getUsers() {
        List<User> users = this.userRepository.findAll();
        List<User> basicUsers = new ArrayList<>();

        for (User user : users) {
            User basicUser = new User();
            basicUser.setUsername(user.getUsername());
            basicUser.setBirthday(user.getBirthday());
            basicUser.setUserId(user.getUserId());
            basicUser.setCreationDate(user.getCreationDate());
            basicUser.setStatus(user.getStatus());

            basicUsers.add(basicUser);
        }

        return basicUsers;
    }

    public List<User> getUsers(String token) {
      if (token == null) {
          return getUsers();
      }
      List<User> advancedUsers = getAllUsers();
      User userWithToken = null;

      for (User user : advancedUsers) {
          if (user.getToken().equals(token)) {
              userWithToken = user;
              break;
          }
      }

      if (userWithToken != null) {
          User updatedUser = userRepository.findByUserId(userWithToken.getUserId());

          if (updatedUser != null) {
              advancedUsers.remove(userWithToken);
              advancedUsers.add(updatedUser);
          }
      }
        return advancedUsers;
    }

    public List<User> getAllUsers() {
      return this.userRepository.findAll();
    }

  public User createUser(User newUser) {
    checkIfUsernameValid(newUser, true);
    checkIfUserExists(newUser);
    newUser.setToken(UUID.randomUUID().toString());
    newUser.setStatus(UserStatus.ONLINE);
    newUser.setCreationDate(LocalDate.now());

    newUser = userRepository.save(newUser);
    userRepository.flush();

    log.debug("Created Information for User: {}", newUser);
    return newUser;
  }


  /**
   * This is a helper method that will check the uniqueness criteria of the
   * username and the name
   * defined in the User entity. The method will do nothing if the input is unique
   * and throw an error otherwise.
   *
   * @param userToBeCreated
   * @throws org.springframework.web.server.ResponseStatusException
   * @see User
   */
  private void checkIfUserExists(User userToBeCreated) {
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
    if (userByUsername != null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "The username provided is not unique. Therefore, the user could not be created!");
    }
}

    private void checkIfUsernameValid(User userToBeValidated, boolean isCreateContext) {
        String username = userToBeValidated.getUsername();

        // Regular expression to match only letters and numbers
        String regex = "^[a-zA-Z0-9]+$";
        List<String> illegalWords = Arrays.asList("settings");

        if (!username.matches(regex) || illegalWords.contains(username)) {
            String errorMessage = isCreateContext ?
                    "The username provided is not valid. Therefore, the user could not be created!" :
                    "The username provided is not valid. Therefore, the username could not be changed!";
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, errorMessage);
        }
    }


  public User loginUser(User currentUser) {
    checkIfUserCorrect(currentUser);
    userRepository.flush();
    log.debug("Logged In for User: {}", currentUser);
    User existingUser = userRepository.findByUsername(currentUser.getUsername());
      existingUser.setStatus(UserStatus.ONLINE);
    return existingUser;
  }

    public User logoutUser(String token) {
        List<User> users = getAllUsers();
        User userToBeLoggedOut = null;
        token = token.replaceAll("\"", "");

        for (User user : users) {
            System.out.println("user.getToken(): " + user.getToken());
            if (user.getToken().equals(token)) {
                userToBeLoggedOut = user;
            }
        }
        userToBeLoggedOut.setStatus(UserStatus.OFFLINE);
        return userToBeLoggedOut;
    }

  private void checkIfUserCorrect(User userToBeLoggedIn) {
      User userByUsername = userRepository.findByUsername(userToBeLoggedIn.getUsername());

      if (userByUsername == null || !(userByUsername != null && userByUsername.getPassword().equals(userToBeLoggedIn.getPassword()))) {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The username or password provided are incorrect. Check your spelling or register a new user!");
      }
  }

    public boolean checkIfTokenValid(String token) {
        List<User> users = getAllUsers();

        for (User user : users) {
            if (user.getToken().equals(token)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkIfTokenMaster(String token) {
        String masterCode = "mastercode";
            if (token.equals(masterCode)) {
                return true;
            }

        return false;
    }

    public User getUserByUserIdMaster(Long userId) {
        User userByUserId = userRepository.findByUserId(userId);
        if (userByUserId != null) {
            return userByUserId;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user with the provided userId was not found!");
        }
        }

    public User getUserByUserIdBasic(Long userId) {
        User userByUserId = userRepository.findByUserId(userId);
        if (userByUserId != null) {
            User basicUser = new User();
            basicUser.setUsername(userByUserId.getUsername());
            basicUser.setBirthday(userByUserId.getBirthday());
            basicUser.setUserId(userByUserId.getUserId());
            basicUser.setCreationDate(userByUserId.getCreationDate());
            basicUser.setStatus(userByUserId.getStatus());
            return userByUserId;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user with the provided userId was not found!");
        }

    }

    public User getUserByUsername(String username) {
        User userByUsername = userRepository.findByUsername(username);
        return userByUsername;
    }
    public User updateUser(User updatedUser) {
        checkIfUsernameValid(updatedUser, false);
        // Retrieve the existing user from the database
        User existingUser = userRepository.findByUserId(updatedUser.getUserId());
        if (existingUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + updatedUser.getUserId());
        }
        // Update the user data with the new username and birthday
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setBirthday(updatedUser.getBirthday());
        // Save the updated user data
        User savedUser = userRepository.save(existingUser);
        userRepository.flush();
        log.debug("Saved Information for User: {}", savedUser);
        return savedUser;
    }


}

