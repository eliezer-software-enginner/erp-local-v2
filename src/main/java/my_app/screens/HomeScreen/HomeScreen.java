package my_app.screens.HomeScreen;

import megalodonte.*;

public class HomeScreen {

    private final Router router;

    public HomeScreen(Router router) {
        this.router = router;
    }

    public Component render (){
        return new Column()
                .child(new Text("Home", new TextProps().fontSize(30)))
                .child(new Button("Go to Profile", new ButtonProps().fontSize(30).onClick(()-> router.navigateTo("profile",e->{}))))
                .child(new Button("Spawn detail", new ButtonProps().fontSize(30).onClick(()-> router.spawnWindow("detail"))));
    }
}
