package desafiocalculadoraback.domain.service;

import desafiocalculadoraback.domain.model.Loan;
import desafiocalculadoraback.domain.model.LoanResult;
import desafiocalculadoraback.domain.ports.outbound.LoanRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoanCalculatorDomainServiceTest {

    private LoanRepositoryPort repositoryPort;
    private LoanCalculatorDomainService domainService;

    @BeforeEach
    void setUp() {
        repositoryPort = mock(LoanRepositoryPort.class);
        domainService = new LoanCalculatorDomainService(repositoryPort);
    }

    @Test
    void shouldCalculateLoanScheduleAndSave() {
        Loan loan = new Loan(
                null,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 6, 30),
                LocalDate.of(2024, 1, 31),
                new BigDecimal("10000.00"),
                new BigDecimal("2.00")
        );

        when(repositoryPort.save(any(LoanResult.class))).thenAnswer(invocation -> {
            LoanResult res = invocation.getArgument(0);
            res.getLoan().setId(1L);
            return res;
        });

        LoanResult result = domainService.calculateAndSave(loan);

        assertNotNull(result);
        assertEquals(5, result.getInstallmentsCount());
        assertNotNull(result.getSchedule());
        assertFalse(result.getSchedule().isEmpty());

        verify(repositoryPort, times(1)).save(any(LoanResult.class));
    }

    @Test
    void shouldFindLoanResultById() {
        LoanResult mockResult = new LoanResult();
        when(repositoryPort.findById(1L)).thenReturn(Optional.of(mockResult));

        LoanResult result = domainService.findById(1L);

        assertNotNull(result);
        verify(repositoryPort, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenLoanNotFound() {
        when(repositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> domainService.findById(99L));
    }
}
