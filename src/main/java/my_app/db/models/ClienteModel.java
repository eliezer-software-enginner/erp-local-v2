package my_app.db.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteModel {
    public Long id;
    public String nome;
    public String cpfCnpj;
    public String celular;
    public Long dataCriacao;

    public ClienteModel() {}

    public ClienteModel(Long id, String nome, String cpfCnpj, String celular, Long dataCriacao) {
        this.id = id;
        this.nome = nome;
        this.cpfCnpj = cpfCnpj;
        this.celular = celular;
        this.dataCriacao = dataCriacao;
    }

    public static ClienteModel fromResultSet(ResultSet rs) throws SQLException {
        var model = new ClienteModel();
        model.id = rs.getLong("id");
        model.nome = rs.getString("nome");
        model.cpfCnpj = rs.getString("cpfCnpj");
        model.celular = rs.getString("celular");
        model.dataCriacao = rs.getLong("data_criacao");
        return model;
    }
}