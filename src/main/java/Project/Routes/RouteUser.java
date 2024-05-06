package Project.Routes;

import Project.Persistence.HibernateConfig;
import Project.Controllers.SecurityController;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.security.RouteRole;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class RouteUser {
    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig(false);
    private static final ObjectMapper jsonMapper = new ObjectMapper();
    private static SecurityController securityController = new SecurityController(emf);

    public EndpointGroup securityRoutes() {
        return () -> {
            path("/auth", () -> {
                before(securityController.authenticate());
                post("/login", securityController.login(), Role.ANYONE);
                post("/register", securityController.register(), Role.ANYONE);
            });
        };
    }

    public EndpointGroup securedRoutes() {
        return () -> {
            path("/protected", () -> {
                before(securityController.authenticate());
                get("/user_demo", (ctx) -> ctx.json(jsonMapper.createObjectNode().put("msg", "Hello from USER Protected")), Role.USER, Role.ADMIN);
                get("/admin_demo", (ctx) -> ctx.json(jsonMapper.createObjectNode().put("msg", "Hello from ADMIN Protected")), Role.ADMIN);
            });
        };
    }
}

enum Role implements RouteRole {ANYONE, USER, ADMIN}