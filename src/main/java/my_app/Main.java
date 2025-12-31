package my_app;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import my_app.hotreload.CoesionApp;
import my_app.hotreload.HotReload;
import my_app.routes.AppRoutes;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@CoesionApp
public class Main extends Application {
    public static Stage stage;
    HotReload hotReload;
    boolean devMode = true;

    static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        initializeScene(primaryStage);
        initHotReload(primaryStage);

        final var router = AppRoutes.defineRoutes(stage);

        stage.show();
    }

    public static void initializeScene(Stage stage) throws Exception {
        stage.setTitle("Adb file pusher");
        stage.setResizable(false);

        final String[] images = {"/logo_32x32.png", "/logo_256x256.png"};

        for (String image : images) {
            stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream(image))));
        }


       // final var root = new VBox(new UI().render().getNode());

        //stage.setScene(new Scene(root, 700, 500));
        System.out.println("[App] Scene re-initialized.");
    }

    private void initHotReload(Stage primaryStage){
        Set<String> exclusions = new HashSet<String>();
        exclusions.add("my_app.hotreload.CoesionApp");
        exclusions.add("my_app.hotreload.Reloader");

        if(devMode){
            this.hotReload = new HotReload(
                    "src/main/java/my_app",
                    "build/classes/java/main",
                    "build/resources/main",
                    "my_app.hotreload.UIReloaderImpl",
                    primaryStage,
                    exclusions
            );
            this.hotReload.start();
        }
    }
}
