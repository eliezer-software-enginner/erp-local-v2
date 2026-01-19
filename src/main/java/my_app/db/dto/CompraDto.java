package my_app.db.dto;

public record CompraDto(
        String produtoCod,
                        String precoCompra,
                        Long fornecedorId, double quantidade,
                        String descontoEmReais,
                        String tipoPagamento,
                        String observacao) {
    public CompraDto {
//        if (nome == null || nome.trim().isEmpty()) {
//            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
//        }
    }
}
