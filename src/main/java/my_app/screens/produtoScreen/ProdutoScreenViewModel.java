package my_app.screens.produtoScreen;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import megalodonte.ComputedState;
import megalodonte.State;
import my_app.db.models.CategoriaModel;
import my_app.db.models.ProdutoModel;
import my_app.db.repositories.CategoriaRepository;
import my_app.db.repositories.ProdutoRepository;
import my_app.lifecycle.viewmodel.component.ViewModel;
import my_app.services.ProdutoService;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProdutoScreenViewModel extends ViewModel {

    private final ProdutoService service = new ProdutoService();
    private final ProdutoRepository produtoRepository = new ProdutoRepository();
    public final ObservableList<ProdutoModel> produtos = FXCollections.observableArrayList();
    public final State<String> codigoBarras = new State<>("123456789");
    public final State<String> descricao = new State<>("teste");
    public final State<String> precoCompra = new State<>("0");
    public final State<String> precoVenda = new State<>("0");

    // depois vira ComputedState
    public final State<String> margem = new State<>("0");
    public final State<String> lucro = new State<>("0");

    public final State<String> comissao = new State<>("");
    public final State<String> garantia = new State<>("");
    public final State<String> marca = new State<>("");

    public final List<String> unidades = List.of("UN","KG","ml");
    public final State<String> unidadeSelected = new State<>("UN");

    public final State<List<String>> categorias = new State<>(List.of("Padrão"));
    public final State<String> categoriaSelected = new State<>("Padrão");

    //TODO: BUSCAR FORNECEDORES DO BANCO
    public final List<String> fornecedores = List.of("Fornecedor Padrão");
    public final State<String> fornecedorSelected = new State<>("Fornecedor Padrão");

    public final State<String> observacoes = new State<>("");
    public final State<String> estoque = new State<>("0");
    public final State<String> validade = new State<>("");

    public final State<String> imagem = new State<>("/assets/produto-generico.png");

    public final State<Boolean> modoEdicao = State.of(false);
    public final ComputedState<String> btnText = ComputedState.of(()-> modoEdicao.get()? "Atualizar": "+ Adicionar", modoEdicao);

    public ProdutoModel toProduto() {
        var p = new ProdutoModel();
        p.codigoBarras = codigoBarras.get();
        p.descricao = descricao.get();
        // Converte de centavos para reais
       // p.precoCompra = new BigDecimal(precoCompraRaw.get()).movePointLeft(2);
        //p.precoVenda = new BigDecimal(precoVendaRaw.get()).movePointLeft(2);
        p.precoCompra = new BigDecimal(precoCompra.get()).movePointLeft(2);
        p.precoVenda = new BigDecimal(precoVenda.get()).movePointLeft(2);
        p.unidade = unidadeSelected.get();
        p.categoriaId = 1L;   // temporário
        p.fornecedorId = 1L;  // temporário
        p.estoque = Integer.parseInt(estoque.get());
        p.observacoes = observacoes.get();
        p.imagem = imagem.get();
        return p;
    }

    public void carregar(ProdutoModel p) {
        descricao.set(p.descricao);
        //convertando pra centavos
        precoCompra.set(p.precoCompra.multiply(new BigDecimal("100")).toString());
        precoVenda.set(p.precoVenda.multiply(new BigDecimal("100")).toString());
        
        // Converte reais para centavos para o raw state
        unidadeSelected.set(p.unidade);
        categoriaSelected.set(String.valueOf(p.categoriaId));
        fornecedorSelected.set(String.valueOf(p.fornecedorId));
        estoque.set(String.valueOf(p.estoque));
        observacoes.set(p.observacoes);
        imagem.set(p.imagem);
    }

    public void salvar() throws Exception {
        if(modoEdicao.get()){
            //TODO: implementar
            return;
        }

        service.salvar(toProduto());
    }

    public void atualizar() throws Exception {
        service.atualizar(toProduto());
    }

    public void excluir() throws Exception {
        service.excluir(codigoBarras.get());
    }

    public void buscar() throws Exception {
        var p = service.buscar(codigoBarras.get());
        if (p != null) carregar(p);
    }

    public ProdutoScreenViewModel() {
        onInit();
    }

    protected void onInit() {
        loadCategorias();
        loadProdutos();
    }

    private void loadCategorias() {
        try {
            CategoriaRepository repo = new CategoriaRepository();
            List<CategoriaModel> categoriasModel = repo.listar();
            List<String> nomesCategorias = categoriasModel.stream()
                    .map(c -> c.nome)
                    .toList();
            
            if (!nomesCategorias.isEmpty()) {
                categorias.set(nomesCategorias);
                categoriaSelected.set(nomesCategorias.get(0)); // Seleciona primeira
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar categorias: " + e.getMessage());
            // Mantém lista padrão em caso de erro
        }
    }

    private void loadProdutos() {
        try {
            produtos.clear();
            produtos.addAll(produtoRepository.listar());
        } catch (Exception e) {
            System.err.println("Erro ao carregar produtos: " + e.getMessage());
        }
    }

    public void refreshProdutos() {
        loadProdutos();
    }
}

