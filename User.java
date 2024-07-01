package ba.smoki.celebration.ejb.user;

import ba.smoki.celebration.ejb.town.Town;
import ba.smoki.celebration.ejb.user.privilege.Privilege;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "user")
@NamedQueries(
        @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username=:username")
)
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @Size(max = 55)
    @Column(name = "username")
    private String username;

    @Basic(optional = false)
    @Size(max = 255)
    @Column(name = "password")
    @JsonbTransient
    private String password;

    @Basic(optional = true)
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Basic(optional = true)
    @Size(max = 45)
    @Column(name = "surname")
    private String surname;
    @Basic(optional = false)
    @Size(max = 255)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Size(max = 45)
    @Column(name = "contact")
    private String contact;

    @Column(name = "active")
    private Boolean active = true;

    @JoinColumn(name = "id_town", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonbTransient
    private Town town;

    @JoinColumn(name = "id_privilege", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Privilege privilege;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }


    public Privilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Privilege privilege) {
        this.privilege = privilege;
    }

    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
