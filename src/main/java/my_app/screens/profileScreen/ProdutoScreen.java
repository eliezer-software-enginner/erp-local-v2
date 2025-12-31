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
                .child(
                        new Row(new RowProps().paddingAll(20)
                                .spacingOf(20), new RowStyler().borderWidth(1).borderColor("black").borderRadius(1))
                                .child(ContainerLeft())
                                .child(ContainerRight())
                )
                .child(new Text("Informaçoẽs", new TextProps().fontSize(30)))
                .child(new Text("Dica 1: Pressione CTRL + G para gerar o código de barras"))
                .child(new Text("Dica 2: O sistema não permite gravar produtos diferentes com o mesmo código de barras!"))
                ;
    }


    Component ContainerRight(){

        State<String> imagemState = new State<>("/assets/produto-generico.png");

        return new Column()
                .child(new Text("Foto do produto",new TextProps().fontSize(20).bold()))
                .child(new Image(imagemState, new ImageProps().size(200)))
                .child(new Button("Inserir imagem", new ButtonProps().fontSize(20)));
    }
     Component ContainerLeft (){
        var rowProps = new RowProps().spacingOf(10);
        return new Column(new ColumnProps().spacingOf(20))
                .child(new Row(rowProps)
                        .child(InputColumn("SKU(Código de barras)", codigoBarrasState))
                        .child(InputColumn("Descrição", descState))
                ).child(new Row(rowProps)
                        .child(InputColumn("Unidade", codigoBarrasState))
                        .child(InputColumn("Preço de compra", descState))
                    .child(InputColumn("Margem", codigoBarrasState))
                    .child(InputColumn("Lucro", descState))
                        .child(InputColumn("Preço de venda", descState))
                )
                .child(new Row(rowProps)
                        .child(InputColumn("Categoria", codigoBarrasState))
                        .child(InputColumn("Fornecedor", descState))//fornecedor padrão
                        .child(InputColumn("Margem", codigoBarrasState))
                        .child(InputColumn("Lucro", descState))
                        .child(InputColumn("Preço de venda", descState))
                )
                .child(new Row(rowProps)
                        .child(InputColumn("Garantia", codigoBarrasState))
                        .child(InputColumn("Marca", descState))//fornecedor padrão
                        .child(InputColumn("Validade", codigoBarrasState))
                        .child(InputColumn("Comissão", descState))
                )
                .child(new Row(rowProps)
                        .child(InputColumn("Observações", codigoBarrasState))
                        .child(InputColumn("Estoque", descState))//fornecedor padrão
                )

                ;
    }

    Component InputColumn(String label, State<String> inputState){
        return new Column()
                .child(new Text(label, new TextProps().fontSize(25)))
                .child(new Input(inputState,new InputProps().fontSize(20)))
                ;
    }
}
