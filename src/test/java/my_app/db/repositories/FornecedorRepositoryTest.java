package my_app.db.repositories;

import my_app.db.DB;
import my_app.db.DBInitializer;
import my_app.db.models.CategoriaModel;
import my_app.db.models.FornecedorModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FornecedorRepositoryTest {
    private FornecedorRepository repo;

    @BeforeEach
    void setup() throws Exception {
        DB.reset();
        DB.getInstance("jdbc:sqlite::memory:");
        DBInitializer.init();
        repo = new FornecedorRepository();
    }

    @Test
    void salvar() throws SQLException {
        var model = fornecedorFake();
        model.nome = "Fornecedor Teste";
        var salvo = repo.salvar(model);

        var encontrado = repo.buscarPorId(salvo.id);

        assertNotNull(encontrado);
        assertEquals("Fornecedor Teste", encontrado.nome);
        assertNotNull(salvo.id);
    }

    @Test
    void listar() throws SQLException {
        var listaInicial = repo.listar();
        
        // Deve ter o fornecedor padrão
        assertTrue(
                listaInicial.stream().anyMatch(p -> p.nome.equals("Fornecedor Padrão"))
        );

        var model1 = fornecedorFake();
        var model2 = fornecedorFake();
        model1.nome = "forn1";
        model2.nome = "forn2";

        repo.salvar(model1);
        repo.salvar(model2);

        var lista = repo.listar();

        // Deve ter o fornecedor padrão mais os 2 novos
        assertEquals(listaInicial.size() + 2, lista.size());
        assertTrue(
                lista.stream().anyMatch(p -> p.nome.equals("Fornecedor Padrão"))
        );
        assertTrue(
                lista.stream().anyMatch(p -> p.nome.equals("forn1"))
        );
        assertTrue(
                lista.stream().anyMatch(p -> p.nome.equals("forn2"))
        );
    }

    @Test
    void atualizar() throws SQLException {
        var model = fornecedorFake();
        repo.salvar(model);

        model.nome = "forn2";
        repo.atualizar(model);

        var atualizado = repo.buscarPorId(model.id);
        assertEquals("forn2", atualizado.nome);
    }

    @Test
    void excluir() throws SQLException {
        var model = fornecedorFake();
        repo.salvar(model);

        repo.excluir(model.id);

        assertNull(repo.buscarPorId(model.id));
    }

    private FornecedorModel fornecedorFake() {
        var model = new FornecedorModel();
        model.id = null; // Deixar o banco definir o ID autoincrement
        model.nome = "forn1";
        model.cpfCnpj = "12345678901";
        model.dataCriacao = System.currentTimeMillis();
        return model;
    }
}