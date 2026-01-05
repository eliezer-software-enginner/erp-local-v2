package my_app.db.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FornecedorModel {
    public Long id;
    public String nome;
    public String cpfCnpj;
    public Long dataCriacao;

    public static FornecedorModel fromResultSet(ResultSet rs) throws SQLException {
        var model = new FornecedorModel();
        model.id = rs.getLong("id");
        model.nome = rs.getString("nome");
        model.cpfCnpj = rs.getString("cpfCnpj");
        model.dataCriacao = rs.getLong("data_criacao");
        return model;
    }
}