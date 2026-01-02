package my_app.db.repositories;

import my_app.db.DB;
import my_app.db.DBInitializer;
import my_app.db.models.CategoriaModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class CategoriaRepositoryTest {
    private CategoriaRepository repo;

    @BeforeEach
    void setup() throws Exception {
        DB.reset();
        DB.getInstance("jdbc:sqlite::memory:");
        DBInitializer.init();
        repo = new CategoriaRepository();
    }


    @Test
    void salvar() throws SQLException {
        var model = categoriaFake();
        model.nome = "cat1";
        repo.salvar(model);

        var encontrado = repo.buscarPorId(model.id);

        assertNotNull(encontrado);
        assertEquals("cat1", encontrado.nome);
    }

    @Test
    void listar() throws SQLException {
        {
            assertTrue(repo.listar().isEmpty());

            var model1 = categoriaFake();
            var model2 = categoriaFake();
            model1.nome = "categ1";
            model2.nome = "categ2";

            repo.salvar(model1);
            repo.salvar(model2);

            var lista = repo.listar();

            assertEquals(2, lista.size());
            assertTrue(
                    lista.stream().anyMatch(p -> p.nome.equals("categ1"))
            );
            assertTrue(
                    lista.stream().anyMatch(p -> p.nome.equals("categ2"))
            );
        }

    }

    @Test
    void atualizar() throws SQLException {
        var model = categoriaFake();
        repo.salvar(model);

        model.nome = "cat2";
        repo.atualizar(model);

        var atualizado = repo.buscarPorId(model.id);
        assertEquals("cat2", atualizado.nome);
    }

    @Test
    void excluir() throws SQLException {
        var model = categoriaFake();
        repo.salvar(model);

        repo.excluir(model.id);

        assertNull(repo.buscarPorId(model.id));
    }


    private CategoriaModel categoriaFake() {
        var model = new CategoriaModel();
        model.id = 1L;
        model.nome = "cat1";
        model.dataCriacao = System.currentTimeMillis();
        return model;
    }


}