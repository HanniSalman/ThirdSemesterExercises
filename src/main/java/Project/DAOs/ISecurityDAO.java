package Project.DAOs;

import Project.Persistence.Model.Role;
import Project.Persistence.Model.User;

public interface ISecurityDAO {
    User getVerifiedUser(String username, String password);

    User createUser(String username, String password);

    Role createRole(String role);

    User addUserRole(String username, String role);
}
