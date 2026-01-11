package my_app.db.dto;

public record ClienteDto(String nome, String cnpj, String telefone) {
    public ClienteDto {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }
    }
}