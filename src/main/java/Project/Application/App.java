package Project.Application;

import Project.Routes.Route;
import Project.ReST.ApplicationConfig;

public class App {
    public static void main(String[] args) {
        ApplicationConfig app = ApplicationConfig.getInstance();
        app.initiateServer()
                .startServer(7007)
                .setExceptionHandlers()
                .checkSecurityRoles()
                .setRoute(Route.addRoutes());
    }
}