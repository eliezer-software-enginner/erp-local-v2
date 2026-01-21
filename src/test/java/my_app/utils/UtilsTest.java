package my_app.utils;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void deCentavosParaReal_deveConverterCorretamente() {
        // Given
        String centavos = "1000"; // 1000 centavos = 10 reais
        
        // When
        BigDecimal resultado = Utils.deCentavosParaReal(centavos);
        
        // Then
        assertEquals(new BigDecimal("10.00"), resultado);
    }
    
    @Test
    void deCentavosParaReal_deveRetornarZeroParaStringVazia() {
        // Given
        String centavos = "";
        
        // When
        BigDecimal resultado = Utils.deCentavosParaReal(centavos);
        
        // Then
        assertEquals(BigDecimal.ZERO, resultado);
    }
    
    @Test
    void deCentavosParaReal_deveRetornarZeroParaStringNula() {
        // Given
        String centavos = null;
        
        // When
        BigDecimal resultado = Utils.deCentavosParaReal(centavos);
        
        // Then
        assertEquals(BigDecimal.ZERO, resultado);
    }
    
    @Test
    void deCentavosParaReal_deveRetornarZeroParaStringInvalida() {
        // Given
        String centavos = "abc";
        
        // When
        BigDecimal resultado = Utils.deCentavosParaReal(centavos);
        
        // Then
        assertEquals(BigDecimal.ZERO, resultado);
    }
    
    @Test
    void deCentavosParaReal_deveTratarZero() {
        // Given
        String centavos = "0";
        
        // When
        BigDecimal resultado = Utils.deCentavosParaReal(centavos);
        
        // Then
        assertEquals(new BigDecimal("0.00"), resultado);
    }
}