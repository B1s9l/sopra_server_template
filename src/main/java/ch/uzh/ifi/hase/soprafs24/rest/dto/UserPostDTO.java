package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.time.LocalDateTime;

public class UserPostDTO {

  private Long userId;

  private String token;

  private String username;

  private String password;

  private LocalDateTime lastIn;

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

  public LocalDateTime getLastIn() {
      return lastIn;
  }
  public void setLastIn(LocalDateTime lastIn) {
      this.lastIn = lastIn;
  }
}
