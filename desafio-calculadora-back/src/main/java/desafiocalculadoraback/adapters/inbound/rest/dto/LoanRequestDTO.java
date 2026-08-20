package desafiocalculadoraback.adapters.inbound.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Dados de entrada para cálculo e simulação de empréstimo")
public record LoanRequestDTO(

        @Schema(description = "Data inicial da contratação", example = "2024-01-01")
        @NotNull(message = "A data inicial é obrigatória.")
        LocalDate initialDate,

        @Schema(description = "Data final do contrato", example = "2024-06-30")
        @NotNull(message = "A data final é obrigatória.")
        LocalDate finalDate,

        @Schema(description = "Data do primeiro pagamento da parcela", example = "2024-01-31")
        @NotNull(message = "A data do primeiro pagamento é obrigatória.")
        LocalDate firstPaymentDate,

        @Schema(description = "Valor total do empréstimo (R$)", example = "10000.00")
        @NotNull(message = "O valor do empréstimo é obrigatório.")
        @Positive(message = "O valor do empréstimo deve ser positivo.")
        BigDecimal amount,

        @Schema(description = "Taxa de juros ao ano (%)", example = "7.00")
        @NotNull(message = "A taxa de juros é obrigatória.")
        @Positive(message = "A taxa de juros deve ser positiva.")
        BigDecimal interestRate
) {
}
