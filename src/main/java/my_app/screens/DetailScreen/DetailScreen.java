package my_app.screens.DetailScreen;

import megalodonte.*;

public class DetailScreen {
    Router router;

    public DetailScreen(Router router) {
    }

    public Component render (){
        return new Column()
                .child(new Text("Detail screen", new TextProps().fontSize(30))
                );
    }
}
