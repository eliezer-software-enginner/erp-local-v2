package my_app.screens;

import megalodonte.State;
import megalodonte.components.*;
import megalodonte.props.*;
import megalodonte.router.Router;

public class MyScreen {

    private final Router router;

    State<String> name = State.of("Eliezer");
    State<Boolean> nameIsEliezer = State.of(true);

    public MyScreen(Router router) {
        this.router = router;
    }

    public Component render (){

        Runnable handleBtnClick = ()->{
            name.set(nameIsEliezer.get()? name.get(): "Megalodonte");
        };

        return new Column(new ColumnProps().bgColor("#fff"))
                .c_child(new Text(name))
                .c_child(new Button("Toggle name", new ButtonProps()).onClick(handleBtnClick));
    }

}
