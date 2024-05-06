package Project.Persistence.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users") //User is a reserved keyword
public class User implements ISecurityUser {
    @Id
    private String username;
    private String password;

    @JoinTable(name = "user_roles", joinColumns = {
            @JoinColumn(name = "user_name", referencedColumnName = "username")},
            inverseJoinColumns = {
                    @JoinColumn(name = "role_name", referencedColumnName = "name")})
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        String salt = BCrypt.gensalt();
        this.password = BCrypt.hashpw(password, salt);
    }

    public void addRole(Role role) {
        roles.add(role);
        role.getUsers().add(this);
    }

    @Override
    public void removeRole(Role role) {
        roles.remove(role);
        role.getUsers().remove(this);
    }

    public Set<String> getRolesAsStrings() {
        if (roles.isEmpty()) {
            return null;
        }
        Set<String> rolesAsStrings = new HashSet<>();
        roles.forEach((role) -> {
            rolesAsStrings.add(role.getName());
        });
        return rolesAsStrings;
    }

    @Override
    public boolean verifyPassword(String pw) {
        return BCrypt.checkpw(pw, this.password);
    }
}
