package my_app.screens.profileScreen;

import megalodonte.State;
import my_app.lifecycle.viewmodel.component.ViewModel;

import java.util.List;

public class ProdutoScreenViewModel extends ViewModel {

    public final State<String> codigoBarras = new State<>("123456789");
    public final State<String> descricao = new State<>("");
    public final State<String> precoCompra = new State<>("0");

    // depois vira ComputedState
    public final State<String> margem = new State<>("0");
    public final State<String> lucro = new State<>("0");

    public final State<String> precoVenda = new State<>("0");

    public final State<String> comissao = new State<>("");
    public final State<String> garantia = new State<>("");
    public final State<String> marca = new State<>("");

    public final List<String> unidades = List.of("UN","KG","ml");
    public final State<String> unidadeSelected = new State<>("UN");

    public final List<String> categorias = List.of("Padrão");
    public final State<String> categoriaSelected = new State<>("Padrão");

    public final List<String> fornecedores = List.of("Fornecedor Padrão");
    public final State<String> fornecedorSelected = new State<>("Fornecedor Padrão");

    public final State<String> observacoes = new State<>("");
    public final State<String> estoque = new State<>("0");
    public final State<String> validade = new State<>("");

    public final State<String> imagem = new State<>("/assets/produto-generico.png");

    public ProdutoScreenViewModel() {
        onInit();
    }
}

