package my_app.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

//schemas
public final class DBInitializer {

    private DBInitializer() {}

    public static void init() {
        try {
            Connection conn = DB.getInstance().connection();
            try (Statement st = conn.createStatement()) {

                st.execute("""
                    CREATE TABLE IF NOT EXISTS produto (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        codigo_barras TEXT UNIQUE NOT NULL,
                        descricao TEXT,
                        preco_compra REAL,
                        preco_venda REAL,
                        unidade TEXT,
                        categoria_id INTEGER,
                        fornecedor_id INTEGER,
                        estoque INTEGER,
                        observacoes TEXT,
                        imagem TEXT,
                        data_criacao INTEGER,
                        FOREIGN KEY (categoria_id) REFERENCES categoria(id)
                    )
                """);

                st.execute("""
                    CREATE TABLE IF NOT EXISTS categoria (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        nome TEXT NOT NULL UNIQUE,
                        data_criacao INTEGER NOT NULL
                    )
                """);

            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inicializar banco", e);
        }
    }
}
