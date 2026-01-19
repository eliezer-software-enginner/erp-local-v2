package my_app.db.repositories;

import my_app.db.dto.CompraDto;
import my_app.db.models.CompraModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ComprasRepository extends BaseRepository<CompraDto, CompraModel> {

    // CREATE
    public CompraModel salvar(CompraDto dto) throws SQLException {
        String sql = """
        INSERT INTO compras 
        (produto_cod, fornecedor_id, quantidade, desconto_em_reais, tipo_pagamento, 
         observacao, data_criacao) 
         VALUES (?,?,?,?,?,?,?)
        """;

        try (PreparedStatement ps = conn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, dto.produtoCod());
            ps.setLong(2, dto.fornecedorId());
            ps.setDouble(3, dto.quantidade());
            ps.setString(4, dto.descontoEmReais());
            ps.setString(5, dto.tipoPagamento());
            ps.setString(6, dto.observacao());
            ps.setLong(7,  System.currentTimeMillis());
            ps.executeUpdate();
            
            // Recupera o ID gerado e cria nova instância
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long idGerado = generatedKeys.getLong(1);
                    return new CompraModel().fromIdAndDto(idGerado, dto);
                }
            }
        }
        throw new SQLException("Falha ao recuperar ID gerado");
    }

    public List<CompraModel> listar() throws SQLException {
        List<CompraModel> lista = new ArrayList<>();
        try (Statement st = conn().createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM compras");
            while (rs.next()) lista.add(new CompraModel().fromResultSet(rs));
        }
        return lista;
    }

    // UPDATE
    public void atualizar(CompraModel model) throws SQLException {
        String sql = """
        UPDATE compras SET produto_cod = ?, fornecedor_id = ?, quantidade = ?,
        desconto_em_reais = ?, tipo_pagamento = ?, observacao = ?
        WHERE id = ?
        """;

        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, model.produtoCod);
            ps.setLong(2, model.fornecedorId);
            ps.setDouble(3, model.quantidade);
            ps.setString(4, model.descontoEmReais);
            ps.setString(5, model.tipoPagamento);
            ps.setString(6, model.observacao);
            ps.setLong(7, model.id);
            ps.executeUpdate();
        }
    }

    public void excluirById(Long id) throws SQLException {
        try (PreparedStatement ps =
                     conn().prepareStatement("DELETE FROM compras WHERE id = ?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    protected CompraModel buscarById(Long id) throws SQLException {
        String sql = "SELECT * FROM compras WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? new CompraModel().fromResultSet(rs) : null;
        }
    }
}

