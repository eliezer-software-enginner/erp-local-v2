package my_app.screens.ProfileScreen;

import megalodonte.*;

public class ProfileScreen {
    Router router;

    public ProfileScreen(Router router) {
    }


    public Component render (){
        return new Column()
                .child(new Text("Profile", new TextProps().fontSize(30))
                );
    }
}
