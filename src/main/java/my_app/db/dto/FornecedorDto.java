package my_app.db.dto;

public record FornecedorDto(
        String nome,
        String cpfCnpj,
        String celular,
        String email,
        String inscricaoEstadual,
        String ufSelected,
        String cidade,
        String bairro,
        String rua,
        String numero,
        String observacao
) {}