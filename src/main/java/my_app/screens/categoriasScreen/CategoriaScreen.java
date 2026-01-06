package my_app.screens.categoriasScreen;

import megalodonte.*;
import megalodonte.components.*;
import megalodonte.components.inputs.Input;
import megalodonte.props.CardProps;
import megalodonte.props.TextProps;
import megalodonte.props.TextTone;
import megalodonte.props.TextVariant;
import megalodonte.router.Router;
import megalodonte.styles.CardStyler;
import megalodonte.theme.Theme;
import megalodonte.theme.ThemeManager;
import my_app.screens.components.ForEachStateDemo;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;

public class CategoriaScreen {
    private final Router router;

    State<String> nome = new State<>("");
    State<String> descricao = new State<>("");
    State<String> search = new State<>("");

    private final State<Integer> refreshTrigger = new State<>(0); // Gatilho para forçar re-renderização
    
    public CategoriaScreen(Router router) {
        this.router = router;
        

    }

    private Theme theme = ThemeManager.theme();

    public Component render (){
      return new Column().c_child(
              ForEachStateDemo.create()
      );
    }

//    Component form(){
//        return new Column()
//                .c_child(
//                        new Row()
//                                .r_child(new Text("Cadastrar Nova Categoria", new TextProps().bold().variant(TextVariant.SUBTITLE))))
//                .c_child(new SpacerVertical(20))
//                .c_child(new Row(new RowProps().bottomVertically().spacingOf(10))
//                        .r_child(new Input(nome, new InputProps().height(45).fontSize(18).placeHolder("Ex: Eletrônicos")))
//                        .r_child(new Button("+ Adicionar", new ButtonProps().fillWidth().height(45).bgColor("#2563eb").fontSize(20).textColor("white").onClick(()-> {}))))
//                ;
//    }


}