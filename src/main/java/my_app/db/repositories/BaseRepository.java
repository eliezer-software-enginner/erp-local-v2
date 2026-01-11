package my_app.db.repositories;

import my_app.db.DB;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class BaseRepository<Dto,Model> {
    protected Connection conn() throws SQLException {
        return DB.getInstance().connection();
    }

    protected abstract Model salvar(Dto dto) throws SQLException;
    protected abstract List<Model> listar() throws SQLException;
    protected abstract void atualizar(Model model) throws SQLException;
    protected abstract void excluirById(Long id) throws SQLException;
    protected abstract Model buscarById(Long id) throws SQLException;
}
