package ba.smoki.celebration.ejb.user;

import ba.smoki.celebration.servlet.user.login.AuthenticationModel;
import ba.smoki.celebration.servlet.user.registration.RegistrationModel;
import jakarta.ejb.Local;

import java.util.List;

@Local
public interface UserServiceLocal {

    Integer CLIENT_PRIVILEGE = 1;

    void create(User user);

    void edit(User user);

    void delete(User user);

    User find(Object id);

    List<User> findAll();

    User findByUsername(String username);

    User register(RegistrationModel registrationModel);

    User login(AuthenticationModel authenticationModel);
}
