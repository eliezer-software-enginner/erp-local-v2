package my_app.db.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoriaModel {
    public Long id;
    public String nome;
    public Long dataCriacao;

    public static CategoriaModel fromResultSet(ResultSet rs) throws SQLException {
        var model  = new CategoriaModel();
        model.id = rs.getLong("id");
        model.nome = rs.getString("nome");
       return model;
    }
}



