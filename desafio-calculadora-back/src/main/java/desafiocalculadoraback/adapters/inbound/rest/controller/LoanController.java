package desafiocalculadoraback.adapters.inbound.rest.controller;

import desafiocalculadoraback.adapters.inbound.rest.dto.LoanRequestDTO;
import desafiocalculadoraback.adapters.inbound.rest.dto.LoanResponseDTO;
import desafiocalculadoraback.adapters.inbound.rest.dto.ScheduleRowDTO;
import desafiocalculadoraback.domain.model.Loan;
import desafiocalculadoraback.domain.model.LoanResult;
import desafiocalculadoraback.domain.ports.inbound.CalculateLoanUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/emprestimos")
@CrossOrigin(origins = "*")
@Tag(name = "Empréstimos", description = "Endpoints para cálculo, simulação e consulta de cronogramas de empréstimos")
public class LoanController {

    private final CalculateLoanUseCase calculateLoanUseCase;

    public LoanController(CalculateLoanUseCase calculateLoanUseCase) {
        this.calculateLoanUseCase = calculateLoanUseCase;
    }

    @PostMapping("/calcular")
    @Operation(summary = "Calcula e persiste um novo cronograma de empréstimo", description = "Recebe os parâmetros do financiamento, executa a simulação e grava o resultado no banco PostgreSQL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Simulação calculada e salva com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos enviados na requisição")
    })
    public ResponseEntity<LoanResponseDTO> calculate(@Valid @RequestBody LoanRequestDTO request) {
        Loan loan = new Loan(
                null,
                request.initialDate(),
                request.finalDate(),
                request.firstPaymentDate(),
                request.amount(),
                request.interestRate()
        );

        LoanResult result = calculateLoanUseCase.calculateAndSave(loan);
        return ResponseEntity.ok(mapToResponseDTO(result));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma simulação de empréstimo por ID", description = "Retorna os detalhes e o cronograma gravado de um empréstimo específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empréstimo encontrado"),
            @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado para o ID fornecido")
    })
    public ResponseEntity<LoanResponseDTO> findById(@PathVariable Long id) {
        LoanResult result = calculateLoanUseCase.findById(id);
        return ResponseEntity.ok(mapToResponseDTO(result));
    }

    private LoanResponseDTO mapToResponseDTO(LoanResult result) {
        Loan loan = result.getLoan();
        List<ScheduleRowDTO> scheduleDTOs = result.getSchedule() == null ? List.of() :
                result.getSchedule().stream()
                        .map(row -> new ScheduleRowDTO(
                                row.getDate(),
                                row.getType(),
                                row.getInstallmentNumber(),
                                row.getDays(),
                                row.getInterestProvision(),
                                row.getInterestAccumulated(),
                                row.getInterestPaid(),
                                row.getAmortization(),
                                row.getBalance()
                        ))
                        .collect(Collectors.toList());

        return new LoanResponseDTO(
                loan.getId(),
                loan.getInitialDate(),
                loan.getFinalDate(),
                loan.getFirstPaymentDate(),
                loan.getAmount(),
                loan.getInterestRate(),
                result.getInstallmentsCount(),
                result.getTotalInterest(),
                scheduleDTOs
        );
    }
}
