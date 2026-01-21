package my_app.db.models;

import my_app.db.dto.ProdutoDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoModelTest {

    @Test
    void fromIdAndDto_deveCriarProdutoCorretamente() {
        // Given
        Long id = 1L;
        ProdutoDto dto = new ProdutoDto();
        dto.codigoBarras = "1234567890123";
        dto.descricao = "Test Product";
        dto.precoCompra = new BigDecimal("10.00");
        dto.precoVenda = new BigDecimal("15.00");
        dto.unidade = "UN";
        dto.marca = "Test Brand";
        dto.categoriaId = 1L;
        dto.fornecedorId = 2L;
        dto.estoque = new BigDecimal("5");
        dto.observacoes = "Test observations";
        dto.imagem = "test.jpg";
        dto.validade = "2025-12-31";
        dto.comissao = "5%";
        dto.garantia = "12 meses";
        
        // When
        ProdutoModel result = new ProdutoModel().fromIdAndDto(id, dto);
        
        // Then
        assertNotNull(result);
        assertEquals(id, result.id);
        assertEquals(dto.codigoBarras, result.codigoBarras);
        assertEquals(dto.descricao, result.descricao);
        assertEquals(dto.precoCompra, result.precoCompra);
        assertEquals(dto.precoVenda, result.precoVenda);
        assertEquals(dto.unidade, result.unidade);
        assertEquals(dto.marca, result.marca);
        assertEquals(dto.categoriaId, result.categoriaId);
        assertEquals(dto.fornecedorId, result.fornecedorId);
        assertEquals(dto.estoque, result.estoque);
        assertEquals(dto.observacoes, result.observacoes);
        assertEquals(dto.imagem, result.imagem);
        assertEquals(dto.validade, result.validade);
        assertEquals(dto.comissao, result.comissao);
        assertEquals(dto.garantia, result.garantia);
        
        // Verificar lucro calculado
        assertEquals(new BigDecimal("5.00"), result.lucro);
    }
}