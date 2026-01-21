package my_app.utils;

import javafx.scene.control.TableView;
import my_app.db.models.ModelBase;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;
import java.util.function.Consumer;

public class Utils {
    public static String toBRLCurrency(BigDecimal value){
        final NumberFormat BRL =
                NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return BRL.format(value);

    }

    public static <T> void onItemTableSelectedChange(TableView<T> table, Consumer<T> eventHandler){
        table.getSelectionModel().selectedItemProperty().addListener((_, _, newSelection) -> {
            if (newSelection != null) {
                eventHandler.accept(newSelection);
            }
        });

    }

    /**
     * Gera um código de barras no padrão EAN-13 matematicamente válido.
     *
     * <p><b>Como funciona:</b></p>
     * <ul>
     *   <li>Gera 12 dígitos numéricos aleatórios</li>
     *   <li>Calcula o dígito verificador usando o algoritmo oficial do EAN-13</li>
     *   <li>Retorna uma String com 13 dígitos no total</li>
     * </ul>
     *
     * <p><b>Validade:</b></p>
     * <ul>
     *   <li>✔ Código EAN-13 matematicamente correto</li>
     *   <li>✔ Aceito por leitores de código de barras</li>
     *   <li>✔ Ideal para sistemas internos, testes e controle de estoque próprio</li>
     * </ul>
     *
     * <p><b>Limitações importantes:</b></p>
     * <ul>
     *   <li>❌ Não representa um código GS1 oficial</li>
     *   <li>❌ Pode colidir com códigos reais existentes no mercado</li>
     *   <li>❌ Não deve ser usado para produtos destinados a varejo, emissão fiscal ou comercialização oficial</li>
     * </ul>
     *
     * <p><b>Use este método apenas quando:</b></p>
     * <ul>
     *   <li>O sistema for interno</li>
     *   <li>O código servir apenas como identificador técnico</li>
     *   <li>Não houver exigência fiscal ou comercial</li>
     * </ul>
     *
     * @return String contendo um código EAN-13 válido
     */
    public static String gerarCodigoBarrasEAN13() {
        Random random = new Random();
        StringBuilder codigo = new StringBuilder();

        // Gera os 12 dígitos base
        for (int i = 0; i < 12; i++) {
            codigo.append(random.nextInt(10));
        }

        // Calcula o dígito verificador
        int soma = 0;
        for (int i = 0; i < 12; i++) {
            int digito = Character.getNumericValue(codigo.charAt(i));
            soma += (i % 2 == 0) ? digito : digito * 3;
        }

        int digitoVerificador = (10 - (soma % 10)) % 10;

        return codigo.append(digitoVerificador).toString();
    }


    /**
     * Esse método é usado para transformar os centavos visuais para valor em Real que será persistido no banco de dados.
     * 1000 centavos equivalem a 10 reais.
     * A conversão entre centavos e reais é baseada na relação de que 1 real = 100 centavos.  Para converter centavos em reais, basta dividir o número de centavos por 100:
     *
     * 1000 centavos ÷ 100 = 10 reais
     * @param centavos
     * @return
     */
public static BigDecimal deCentavosParaReal(String centavos){
        if (centavos == null || centavos.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(centavos).movePointLeft(2);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }


    /**
     * Transforma o valor em Real persitido no banco para centavos para utilizar nos inputs
     * @param real valor em Real recuperado do banco
     * @return valor em centavos para exibição nos inputs
     */
    public static String deRealParaCentavos(BigDecimal real){
        return real.multiply(new BigDecimal("100")).toPlainString();
    }
}
