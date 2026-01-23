package my_app.db.dto;

import java.math.BigDecimal;

public record CompraDto(
        String produtoCod,
        String precoCompra,
        Long fornecedorId,
        BigDecimal quantidade,
        String descontoEmReais,
        String tipoPagamento,
        String observacao) {
}
