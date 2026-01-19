package my_app.screens.categoriasScreen;

import megalodonte.*;
import megalodonte.components.*;
import megalodonte.components.inputs.Input;
import megalodonte.props.InputProps;

public class TestInput {
    public static void main(String[] args) {
        // Teste simples para verificar o comportamento do Input + State
        State<String> nome = State.of("");
        
        Input input = new Input(nome);
        
        // Simular digitação
        nome.set("Teste");
        
        // Verificar valor
        String valor = nome.get();
        System.out.println("Valor no State: " + valor);
        
        // Testar com Input com props
        Input inputComProps = new Input(nome, 
            new InputProps().height(45).fontSize(18).placeHolder("Ex: Eletrônicos"));
        
        System.out.println("Teste concluído");
    }
}