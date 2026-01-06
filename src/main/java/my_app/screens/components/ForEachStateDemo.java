package my_app.screens.components;

import megalodonte.ButtonProps;
import megalodonte.ColumnProps;
import megalodonte.State;
import megalodonte.ForEachState;
import megalodonte.components.*;
import megalodonte.props.TextProps;

import java.util.Arrays;
import java.util.List;

/**
 * Exemplo simples do ForEachState funcionando
 */
public class ForEachStateDemo {
    
    public record Produto(String nome, double preco) {}
    
    public static Component create() {
        // Estado com lista de produtos
        State<List<Produto>> produtosState = State.of(Arrays.asList(
            new Produto("Café", 15.00),
            new Produto("Pão", 8.00)
        ));
        
        // Cria o ForEachState que mapeia produtos para botões
        ForEachState<Produto, Button> forEachState = ForEachState.of(
            produtosState,
            produto -> new Button(produto.nome + " - R$ " + produto.preco)
        );

        Column mainColumn = new Column()
                .c_child(new Text("Produtos Disponíveis", new TextProps().fontSize(20).bold()))

                .c_child( new Button("Adicionar Novo Produto", new ButtonProps().onClick(()->{
                    // Agora com o método add() implementado!
                    produtosState.add(new Produto("Teste", 10));
                })))
                // Agora com o método items() implementado!
                .items(forEachState)
                .c_child(new Text("Sou um footer besta"));
        
        return mainColumn;
    }
}