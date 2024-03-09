package ch.uzh.ifi.hase.soprafs24.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class UserRepositoryIntegrationTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

/*
  @Test
  public void findByUserId_success() { //Changed from findByName to findById
    // given
    User user = new User();
    user.setName("Firstname Lastname");
    user.setUserId(1L);
    user.setUsername("firstname@lastname");
    user.setBirthday(LocalDate.of(2000, 1, 1));
    user.setCreationDate(LocalDate.of(2000, 1, 1));
    user.setPassword("password");
    user.setStatus(UserStatus.OFFLINE);
    user.setToken("1");

    entityManager.persist(user);
    entityManager.flush();

    // when
    User found = userRepository.findByUserId(user.getUserId()); //Changed findByName and getName to findById and getId

    // then
    assertNotNull(found.getUserId());
    assertEquals(found.getUserId(), user.getUserId()); //changed getName and getName to getId and get Id
    assertEquals(found.getUsername(), user.getUsername());
    assertEquals(found.getToken(), user.getToken());
    assertEquals(found.getStatus(), user.getStatus());
    assertEquals(found.getPassword(), user.getPassword());
    assertEquals(found.getBirthday(), user.getBirthday());
    assertEquals(found.getCreationDate(), user.getCreationDate());
  }
  */
}
