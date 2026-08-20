package desafiocalculadoraback.adapters.inbound.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Linha do cronograma de parcelas e juros do empréstimo")
public record ScheduleRowDTO(

        @Schema(description = "Data de competência", example = "2024-01-31")
        LocalDate date,

        @Schema(description = "Tipo de evento (INITIAL, PAYMENT, MONTH_END)", example = "PAYMENT")
        String type,

        @Schema(description = "Número da parcela", example = "1")
        Integer installmentNumber,

        @Schema(description = "Quantidade de dias no período", example = "30")
        Long days,

        @Schema(description = "Valor dos juros no período", example = "16.52")
        BigDecimal interest,

        @Schema(description = "Valor da amortização", example = "1666.67")
        BigDecimal amortization,

        @Schema(description = "Saldo devedor restante", example = "8333.33")
        BigDecimal balance
) {
}
