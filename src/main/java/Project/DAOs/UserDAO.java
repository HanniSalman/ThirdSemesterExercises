package Project.DAOs;

import Project.Persistence.Model.Role;
import Project.Persistence.Model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;

public class UserDAO implements ISecurityDAO {

    private static UserDAO instance;
    private static EntityManagerFactory emf;

    public static UserDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserDAO();
        }
        return instance;
    }

    @Override
    public User getVerifiedUser(String username, String password) throws EntityNotFoundException {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class);
            query.setParameter("id", username);

            User user = em.find(User.class, username);

            if (user == null) {
                throw new EntityNotFoundException("No user found with username: " + username);
            }

            if (!user.verifyPassword(password)) {
                throw new EntityNotFoundException("Wrong password");
            }
            return user;
        }
    }


    @Override
    public User createUser(String username, String password) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        User user = new User(username, password);
        Role userRole = em.find(Role.class, "user");
        if (userRole == null) {
            userRole = new Role("user");
            em.persist(userRole);
        }
        user.addRole(userRole);
        em.persist(user);
        em.getTransaction().commit();
        em.close();
        return user;
    }

    @Override
    public Role createRole(String role) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Role createdRole = new Role();
            createdRole.setName(role);
            em.persist(createdRole);
            em.getTransaction().commit();
            return createdRole;
        } finally {
            em.close();
        }
    }

    @Override
    public User addUserRole(String username, String role) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            User foundUser = em.find(User.class, username);
            Role foundRole = em.find(Role.class, role);
            foundUser.addRole(foundRole);
            em.merge(foundUser);
            em.getTransaction().commit();
            return foundUser;
        } finally {
            em.close();
        }
    }

    public User addRoleToUser(String username, String role) {
        return null;
    }

    public User verifyUser(String username, String password) {
        EntityManager em = emf.createEntityManager();
        User user = em.find(User.class, username);
        if (user == null) throw new EntityNotFoundException("No user found");
        if (!user.verifyPassword(password)) throw new EntityNotFoundException("Wrong password");
        return user;
    }
}

