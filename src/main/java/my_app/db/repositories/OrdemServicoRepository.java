package my_app.db.repositories;

import my_app.db.dto.OrdemServicoDto;
import my_app.db.models.OrdemServicoModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrdemServicoRepository extends BaseRepository<OrdemServicoDto, OrdemServicoModel> {

    // CREATE
    public OrdemServicoModel salvar(OrdemServicoDto dto) throws SQLException {
        String sql = """
        INSERT INTO ordens_de_servico 
        (cliente_id, tecnico_id, equipamento, mao_de_obra_valor, pecas_valor, 
         tipo_pagamento, checklist_relatorio, data_escolhida, 
         total_liquido, data_criacao) 
         VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)
        """;

        try (PreparedStatement ps = conn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, dto.clienteId());
            ps.setLong(2, dto.tecnicoId());
            ps.setString(3, dto.equipamento());
            ps.setBigDecimal(4, dto.mao_de_obra_valor());
            ps.setBigDecimal(5, dto.pecas_valor());
            ps.setString(6, dto.tipoPagamento());
            ps.setString(7, dto.checklist_relatorio());
            ps.setLong(8, dto.data_escolhida());
            ps.setBigDecimal(9, dto.totalLiquido());
            ps.setLong(10, System.currentTimeMillis());
            ps.executeUpdate();
            
            // Recupera o ID gerado e cria nova instância
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long idGerado = generatedKeys.getLong(1);
                    return new OrdemServicoModel().fromIdAndDtoAndMillis(idGerado, dto, System.currentTimeMillis());
                }
            }
        }
        throw new SQLException("Falha ao recuperar ID gerado");
    }

    public List<OrdemServicoModel> listar() throws SQLException {
        List<OrdemServicoModel> lista = new ArrayList<>();
        try (Statement st = conn().createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM ordens_de_servico");
            while (rs.next()) lista.add((OrdemServicoModel) new OrdemServicoModel().fromResultSet(rs));
        }
        return lista;
    }

    // UPDATE
    public void atualizar(OrdemServicoModel model) throws SQLException {
        String sql = """
        UPDATE ordens_de_servico SET cliente_id = ?, tecnico_id = ?, equipamento = ?,
        mao_de_obra_valor = ?, pecas_valor = ?, tipo_pagamento = ?,
        checklist_relatorio = ?, data_escolhida = ?, total_liquido = ?
        WHERE id = ?
        """;

        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setLong(1, model.clienteId);
            ps.setLong(2, model.tecnicoId);
            ps.setString(3, model.equipamento);
            ps.setBigDecimal(4, model.maoDeObraValor);
            ps.setBigDecimal(5, model.pecas_valor);
            ps.setString(6, model.tipoPagamento);
            ps.setString(7, model.checklistRelatorio);
            ps.setLong(8, model.dataEscolhida);
            ps.setBigDecimal(9, model.totalLiquido);
            ps.setLong(10, model.id);

            ps.executeUpdate();
        }
    }

    public void excluirById(Long id) throws SQLException {
        try (PreparedStatement ps =
                     conn().prepareStatement("DELETE FROM ordens_de_servico WHERE id = ?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    protected OrdemServicoModel buscarById(Long id) throws SQLException {
        String sql = "SELECT * FROM ordens_de_servico WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? (OrdemServicoModel) new OrdemServicoModel().fromResultSet(rs) : null;
        }
    }
}

