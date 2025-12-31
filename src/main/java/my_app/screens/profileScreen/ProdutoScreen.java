package my_app.screens.profileScreen;

import megalodonte.*;

public class ProdutoScreen {
    Router router;

    public ProdutoScreen(Router router) {
    }

    State<String> codigoBarrasState = new State<>("123456789");
    State<String> descState = new State<>("123456789");

    public Component render (){
        return new Column(new ColumnProps().paddingAll(15))
                .child(Container())
                ;
    }

    public Component Container (){
        return new Column(new ColumnProps().paddingAll(10), new ColumnStyler().borderWidth(1).borderColor("black").borderRadius(1))
                .child(new Row()
                        .child(InputColumn("SKU(Código de barras)", codigoBarrasState))
                        .child(InputColumn("Descrição", descState))
                ).child(new Row()
                        .child(InputColumn("Unidade", codigoBarrasState))
                        .child(InputColumn("Preço de compra", descState))
                    .child(InputColumn("Margem", codigoBarrasState))
                    .child(InputColumn("Lucro", descState))
                        .child(InputColumn("Preço de venda", descState))
                )
                .child(new Row()
                        .child(InputColumn("Categoria", codigoBarrasState))
                        .child(InputColumn("Fornecedor", descState))//fornecedor padrão
                        .child(InputColumn("Margem", codigoBarrasState))
                        .child(InputColumn("Lucro", descState))
                        .child(InputColumn("Preço de venda", descState))
                )
                .child(new Row()
                        .child(InputColumn("Garantia", codigoBarrasState))
                        .child(InputColumn("Marca", descState))//fornecedor padrão
                        .child(InputColumn("Validade", codigoBarrasState))
                        .child(InputColumn("Comissão", descState))
                )
                .child(new Row()
                        .child(InputColumn("Observações", codigoBarrasState))
                        .child(InputColumn("Estoque", descState))//fornecedor padrão
                )

                ;
    }

    Component InputColumn(String label, State<String> inputState){
        return new Column()
                .child(new Text(label))
                .child(new Input(inputState))
                ;
    }
}
