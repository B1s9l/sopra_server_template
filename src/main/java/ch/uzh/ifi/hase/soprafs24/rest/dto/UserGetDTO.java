package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;

import java.time.LocalDate;

public class UserGetDTO {

  private Long userId;
  private String username;
  private UserStatus status;
  private LocalDate birthday;
  private LocalDate creationDate;
  //I need to add password and token to the user object because a POST call to /users (to register)
  //will return the user object and without the password and token login  and my secure routing won't work.
  private String password;
  private String token;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getToken() {
    return token;
  }
  public void setToken(String token) {
    this.token = token;
  }

  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }

  public UserStatus getStatus() {
    return status;
  }
  public void setStatus(UserStatus status) {
    this.status = status;
  }

  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }

  public LocalDate getBirthday() {
    return birthday;
  }
  public void setBirthday(LocalDate birthday) {
    this.birthday = birthday;
  }

  public LocalDate getCreationDate() {
    return creationDate;
  }
  public void setCreationDate(LocalDate creationDate) {
    this.creationDate = creationDate;
  }
}
