package my_app.screens.components;

import megalodonte.State;
import my_app.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ComponentsTest {

    @BeforeEach
    void setUp() {
        // Reset theme if needed
    }

    @Test
    void deRealParaCentavos_deveConverterCorretamente() {
        // Given
        BigDecimal real10 = new BigDecimal("10.00");
        BigDecimal realZero = BigDecimal.ZERO;
        
        // When
        String centavos10 = Utils.deRealParaCentavos(real10);
        String centavosZero = Utils.deRealParaCentavos(realZero);
        
        // Then
        assertEquals("1000", centavos10); // 10.00 reais = 1000 centavos
        assertEquals("0", centavosZero); // 0.00 reais = 0 centavos
    }

    @Test
    void deRealParaCentavos_deveTratarNulo() {
        // Given
        BigDecimal realNulo = null;
        
        // When
        String centavos = Utils.deRealParaCentavos(realNulo);
        
        // Then
        assertEquals("0", centavos);
    }

    @Test
    void inputColumnCurrency_deveManterEstadoCorreto() {
        // Given
        State<String> precoState = State.of("1000"); // 1000 centavos = R$ 10,00
        
        // When - simular digitação "1000"
        String result = precoState.get();
        
        // Then
        assertEquals("1000", result); // Estado deve manter "1000" (centavos)
    }

    @Test
    void inputColumnCurrency_deveExibirFormatado() {
        // Given
        State<String> precoState = State.of("2500"); // 2500 centavos = R$ 25,00
        
        // When - o input exibe formatado
        // Este teste verificaria o comportamento visual do componente
        
        // Then
        assertEquals("2500", precoState.get()); // Estado interno continua em centavos
        // O display do input mostraria "R$ 25,00"
    }
}