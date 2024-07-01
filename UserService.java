package ba.smoki.celebration.ejb.user;

import ba.smoki.celebration.ejb.AbstractService;
import ba.smoki.celebration.ejb.town.Town;
import ba.smoki.celebration.ejb.town.TownServiceLocal;
import ba.smoki.celebration.ejb.user.privilege.Privilege;
import ba.smoki.celebration.ejb.user.privilege.PrivilegeServiceLocal;
import ba.smoki.celebration.servlet.user.login.AuthenticationModel;
import ba.smoki.celebration.servlet.user.registration.RegistrationModel;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.*;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

import java.util.List;
import java.util.logging.Logger;

@Stateless
public class UserService extends AbstractService<User> implements UserServiceLocal {
    @PersistenceContext(unitName = "celebrationPU")
    private EntityManager entityManager;

    @Inject
    private Pbkdf2PasswordHash pbkdf2PasswordHash;

    @Inject
    private TownServiceLocal townServiceLocal;

    @Inject
    private PrivilegeServiceLocal privilegeServiceLocal;

    public UserService() {
        super(User.class);
    }

    @Override
    protected EntityManager entityManager() {
        return entityManager;
    }

    public User register(RegistrationModel registrationModel) {
        User user = findByUsername(registrationModel.getUsername());
        Privilege privilege = privilegeServiceLocal.find(CLIENT_PRIVILEGE);
        Integer townId = registrationModel.getTownId();
        Town town = townServiceLocal.find(townId);
        if (user == null) {
            user = new User();
            user.setName(registrationModel.getName());
            user.setSurname(registrationModel.getSurname());
            user.setUsername(registrationModel.getUsername());
            String plainPassword = registrationModel.getPassword();
            char[] plainPasswordChars = plainPassword.toCharArray();
            String hashedPassword = pbkdf2PasswordHash.generate(plainPasswordChars);
            user.setPassword(hashedPassword);
            user.setTown(town);
            user.setPrivilege(privilege);
            user.setContact(registrationModel.getContact());
            user.setEmail(registrationModel.getEmail());
            user.setActive(true);
            create(user);
        }
        return user;
    }

    /**
     *
     * @param authenticationModel
     * @return user or null
     */
    @Override
    public User login(AuthenticationModel authenticationModel) {
        try {
            Query query = entityManager.createNamedQuery("User.findByUsername");
            query.setParameter("username", authenticationModel.getUsername());
            User user = (User) query.getSingleResult();
            char[] plainPassword = authenticationModel.getPlainPasswordAsCharArray();
            String hashedPassword = user.getPassword();
            boolean verified = pbkdf2PasswordHash.verify(plainPassword, hashedPassword);
            return verified ? user : null;
        } catch (NonUniqueResultException | NoResultException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public User findByUsername(String username) {
        try {
            Query query = entityManager.createNamedQuery("User.findByUsername");
            query.setParameter("username", username);
            List<User> users = query.getResultList();
            return users.isEmpty() ? null : users.get(0);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public User login(String username, String plainPassword) {
        try {
            User user = findByUsername(username);
            if (user != null) {
                char[] plainPasswordChars = plainPassword.toCharArray();
                String hashedPassword = user.getPassword();
                boolean verified = pbkdf2PasswordHash.verify(plainPasswordChars, hashedPassword);
                return verified ? user : null;
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
