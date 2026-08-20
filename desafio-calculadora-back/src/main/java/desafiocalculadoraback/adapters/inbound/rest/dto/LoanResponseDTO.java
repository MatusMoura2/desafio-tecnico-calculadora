package desafiocalculadoraback.adapters.inbound.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "Resultado completo do cálculo do empréstimo com cronograma")
public record LoanResponseDTO(

        @Schema(description = "Identificador único da simulação gravada", example = "1")
        Long id,

        @Schema(description = "Data inicial contratada", example = "2024-01-01")
        LocalDate initialDate,

        @Schema(description = "Data final do contrato", example = "2024-06-30")
        LocalDate finalDate,

        @Schema(description = "Data do primeiro pagamento", example = "2024-01-31")
        LocalDate firstPaymentDate,

        @Schema(description = "Valor inicial financiado", example = "10000.00")
        BigDecimal amount,

        @Schema(description = "Taxa de juros aplicada (%)", example = "2.00")
        BigDecimal interestRate,

        @Schema(description = "Quantidade total de parcelas", example = "6")
        Integer installmentsCount,

        @Schema(description = "Total de juros acumulados", example = "58.43")
        BigDecimal totalInterest,

        @Schema(description = "Cronograma detalhado de eventos e parcelas")
        List<ScheduleRowDTO> schedule
) {
}
