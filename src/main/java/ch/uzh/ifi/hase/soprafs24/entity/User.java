package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how
 * the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes
 * the primary key
 */
@Entity
@Table(name = "USER")
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private Long userId;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false, unique = true)
  private String token;

  @Column(nullable = false)
  private String password;

  @Column(nullable = true)
  private LocalDateTime lastIn;



    public Long getUserId() {
    return userId;
  }
  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }

  public String getToken() {
    return token;
  }
  public void setToken(String token) {
    this.token = token;
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
