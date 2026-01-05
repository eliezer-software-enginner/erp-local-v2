package my_app.db.repositories;

import my_app.db.DB;
import my_app.db.models.ProdutoModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LicensaRepository {

    private Connection conn() throws SQLException {
        return DB.getInstance().connection();
    }

    // CREATE
    public void salvar(String valor) throws SQLException {
        String sql = """
        INSERT INTO licensas (valor, data_criacao)
        VALUES (?,?)
    """;

        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, valor);
            ps.setLong(2, System.currentTimeMillis());
            ps.executeUpdate();
        }
    }
}

