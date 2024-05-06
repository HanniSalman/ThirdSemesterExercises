package Project.Routes;

import Project.DAOs.HotelDAO;
import Project.DAOs.RoomDAO;
import Project.DAOs.UserDAO;
import Project.Persistence.HibernateConfig;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

public class Route {
    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig(false);
    private static HotelDAO hotelDAO = HotelDAO.getInstance(emf);
    private static RoomDAO roomDAO = RoomDAO.getInstance(emf);
    private static UserDAO userDAO = UserDAO.getInstance(emf);
    private static RoomRoute roomRoute = new RoomRoute(roomDAO);
    private static RouteHotel routeHotel = new RouteHotel(hotelDAO, roomDAO);
    private static RouteUser routeUser = new RouteUser();

    // Declare a public static method named addRoutes which returns an EndpointGroup
    public static EndpointGroup addRoutes() {
        // Call the combineRoutes method passing the EndpointGroup instances returned by routeHotel.hotelRoutes() and roomRoute.roomRoutes()
        return combineRoutes(routeHotel.hotelRoutes(), roomRoute.roomRoutes(), routeUser.securedRoutes(), routeUser.securityRoutes());
    }

    // Define a private static method named combineRoutes which takes multiple EndpointGroup instances as arguments
    private static EndpointGroup combineRoutes(EndpointGroup... endpointGroups) {
        // Define a lambda expression for the EndpointGroup
        return () -> {
            // Iterate through each EndpointGroup passed as arguments
            for (EndpointGroup group : endpointGroups) {
                // Add the endpoints from each EndpointGroup
                group.addEndpoints();
            }
        };
    }
}
