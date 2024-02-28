package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;

import java.time.LocalDate;

public class UserPostDTO {

  private Long userId;

  private String token;

  private UserStatus status;

  private String username;

  private String password;

  private LocalDate birthday;

  private LocalDate creationDate;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getToken() {
        return token; }
  public void setToken(String token) {
        this.token = token;
    }

  public UserStatus getStatus() {
        return status;
    }
  public void setStatus(UserStatus status) {
        this.status = status;
    }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
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

  public void setBirthday() {
    this.birthday = birthday;
  }

  public LocalDate getCreationDate() {
    return creationDate;
  }

  public void setCreationDate() {
    this.creationDate = creationDate;
  }
}
