package desafiocalculadoraback.adapters.inbound.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import desafiocalculadoraback.adapters.inbound.rest.controller.LoanController;
import desafiocalculadoraback.adapters.inbound.rest.dto.LoanRequestDTO;
import desafiocalculadoraback.domain.model.Loan;
import desafiocalculadoraback.domain.model.LoanResult;
import desafiocalculadoraback.domain.ports.inbound.CalculateLoanUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoanController.class)
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CalculateLoanUseCase calculateLoanUseCase;

    @Test
    void shouldReturn200AndCalculatedLoan() throws Exception {
        LoanRequestDTO requestDTO = new LoanRequestDTO(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 6, 30),
                LocalDate.of(2024, 1, 31),
                new BigDecimal("10000.00"),
                new BigDecimal("2.00")
        );

        Loan mockLoan = new Loan(1L, requestDTO.initialDate(), requestDTO.finalDate(), requestDTO.firstPaymentDate(), requestDTO.amount(), requestDTO.interestRate());
        LoanResult mockResult = new LoanResult(mockLoan, 6, new BigDecimal("57.90"), List.of());

        when(calculateLoanUseCase.calculateAndSave(any(Loan.class))).thenReturn(mockResult);

        mockMvc.perform(post("/api/emprestimos/calcular")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.installmentsCount").value(6))
                .andExpect(jsonPath("$.totalInterest").value(57.90));
    }

    @Test
    void shouldReturn400BadRequestWhenPayloadIsInvalid() throws Exception {
        LoanRequestDTO invalidDTO = new LoanRequestDTO(
                null,
                LocalDate.of(2024, 6, 30),
                LocalDate.of(2024, 1, 31),
                new BigDecimal("-100.00"),
                new BigDecimal("2.00")
        );

        mockMvc.perform(post("/api/emprestimos/calcular")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFindLoanById() throws Exception {
        Loan mockLoan = new Loan(1L, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 6, 30), LocalDate.of(2024, 1, 31), new BigDecimal("10000.00"), new BigDecimal("2.00"));
        LoanResult mockResult = new LoanResult(mockLoan, 6, new BigDecimal("57.90"), List.of());

        when(calculateLoanUseCase.findById(1L)).thenReturn(mockResult);

        mockMvc.perform(get("/api/emprestimos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}
