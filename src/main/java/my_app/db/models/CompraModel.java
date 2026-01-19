package my_app.db.models;

import my_app.db.dto.CompraDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CompraModel implements ModelBase<CompraDto> {
    public Long id;
    public String produtoCod;
    public Long fornecedorId;
    public double quantidade;
    public String descontoEmReais;
    public String tipoPagamento;
    public String observacao;
    public Long dataCriacao;

    public CompraModel() {}

    @Override
    public CompraModel fromResultSet(ResultSet rs) throws SQLException {
        var model = new CompraModel();
        model.id = rs.getLong("id");
        model.produtoCod = rs.getString("produto_cod");
        model.fornecedorId = rs.getLong("fornecedor_id");
        model.quantidade = rs.getInt("quantidade");
        model.descontoEmReais = rs.getString("desconto_em_reais");
        model.tipoPagamento = rs.getString("tipo_pagamento");
        model.observacao = rs.getString("observacao");
        model.dataCriacao = rs.getLong("data_criacao");
        return model;
    }

    @Override
    public CompraModel fromIdAndDto(Long id, CompraDto compraDto) {
        var model = new CompraModel();
        model.id = id;
        model.produtoCod = compraDto.produtoCod();
        model.fornecedorId = compraDto.fornecedorId();
        model.quantidade = compraDto.quantidade();
        model.descontoEmReais = compraDto.descontoEmReais();
        model.tipoPagamento = compraDto.tipoPagamento();
        model.observacao = compraDto.observacao();
        return model;
    }

}