package my_app.routes;

import javafx.stage.Stage;
import megalodonte.Router;
import my_app.screens.DetailScreen.DetailScreen;
import my_app.screens.HomeScreen.HomeScreen;
import my_app.screens.ProfileScreen.ProfileScreen;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class AppRoutes {
    public static Router defineRoutes(Stage stage) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        var routes = Set.of(
                new Router.Route("home", router -> new HomeScreen(router), new Router.RouteProps(900, 700)),
                new Router.Route("profile",router-> new ProfileScreen(router), new Router.RouteProps(900, 700)),
                new Router.Route("detail",router-> new DetailScreen(router), new Router.RouteProps(900, 700))
        );
        return new Router(routes, "home", stage);
    }
}
