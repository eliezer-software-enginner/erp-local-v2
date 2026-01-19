package my_app.db.repositories;

import my_app.db.DB;
import my_app.db.DBInitializer;
import my_app.db.dto.FornecedorDto;
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
        var dto = fornecedorFake();
        var model = repo.salvar(dto);

        var encontrado = repo.buscarPorId(model.id);

        assertNotNull(encontrado);
        assertEquals("Fornecedor Teste", encontrado.nome);
        assertNotNull(encontrado.id);
    }

    @Test
    void listar() throws SQLException {
        var listaInicial = repo.listar();
        
        // Deve ter o fornecedor padrão
        assertTrue(
                listaInicial.stream().anyMatch(p -> p.nome.equals("Fornecedor Padrão"))
        );

        var dto1 = fornecedorFake( "forn1");
        var dto2 = fornecedorFake( "forn2");

        repo.salvar(dto1);
        repo.salvar(dto2);

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
        var dto = fornecedorFake();
        var model = repo.salvar(dto);

        model.nome = "forn2";
        repo.atualizar(model);

        var atualizado = repo.buscarPorId(model.id);
        assertEquals("forn2", atualizado.nome);
    }

    @Test
    void excluir() throws SQLException {
        var dto = fornecedorFake();
        var model = repo.salvar(dto);

        repo.excluir(model.id);

        assertNull(repo.buscarPorId(model.id));
    }

    private FornecedorDto fornecedorFake(String nome) {
        var cpfCnpj = "12345678901";
        var telefone = "5512345678910";
        var email = "teste@teste.com";
        var endereco = "Rua x, bairro tal, cidade lá";
        var dataCriacao = System.currentTimeMillis();

        return new FornecedorDto(nome, cpfCnpj, telefone, email,  endereco, dataCriacao);
    }

    private FornecedorDto fornecedorFake() {
        var nome = "Fornecedor Teste";
        var cpfCnpj = "12345678901";
        var telefone = "5512345678910";
        var email = "teste@teste.com";
        var endereco = "Rua x, bairro tal, cidade lá";
        var dataCriacao = System.currentTimeMillis();

        return new FornecedorDto(nome, cpfCnpj, telefone, email,  endereco, dataCriacao);
    }
}