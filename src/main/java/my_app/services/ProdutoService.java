package my_app.services;

import my_app.db.models.ProdutoModel;
import my_app.db.repositories.ProdutoRepository;

public class ProdutoService {
    private final ProdutoRepository repo = new ProdutoRepository();

    public void salvar(ProdutoModel p) throws Exception {
        if (repo.buscarPorCodigoBarras(p.codigoBarras) != null) {
            throw new IllegalStateException("Produto já existe");
        }
        repo.salvar(p);
    }

    public void atualizar(ProdutoModel p) throws Exception {
        if (repo.buscarPorCodigoBarras(p.codigoBarras) == null) {
            throw new IllegalStateException("Produto não encontrado");
        }
        repo.atualizar(p);
    }

    public ProdutoModel buscar(String codigoBarras) throws Exception {
        return repo.buscarPorCodigoBarras(codigoBarras);
    }

    public void excluir(String codigoBarras) throws Exception {
        repo.excluir(codigoBarras);
    }
}
