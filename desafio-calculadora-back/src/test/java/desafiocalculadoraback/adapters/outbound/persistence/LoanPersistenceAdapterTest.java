package desafiocalculadoraback.adapters.outbound.persistence;

import desafiocalculadoraback.adapters.outbound.persistence.adapter.LoanPersistenceAdapter;
import desafiocalculadoraback.adapters.outbound.persistence.repository.SpringDataLoanRepository;
import desafiocalculadoraback.domain.model.Loan;
import desafiocalculadoraback.domain.model.LoanResult;
import desafiocalculadoraback.domain.model.ScheduleRow;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(LoanPersistenceAdapter.class)
class LoanPersistenceAdapterTest {

    @Autowired
    private LoanPersistenceAdapter adapter;

    @Autowired
    private SpringDataLoanRepository repository;

    @Test
    void shouldSaveAndFindLoanResultInDatabase() {
        Loan loan = new Loan(null, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 6, 30), LocalDate.of(2024, 1, 31), new BigDecimal("10000.00"), new BigDecimal("2.00"));
        ScheduleRow row1 = new ScheduleRow(LocalDate.of(2024, 1, 1), "INITIAL", null, 0L, BigDecimal.ZERO, BigDecimal.ZERO, new BigDecimal("10000.00"));
        ScheduleRow row2 = new ScheduleRow(LocalDate.of(2024, 1, 31), "PAYMENT", 1, 30L, new BigDecimal("16.67"), new BigDecimal("1666.67"), new BigDecimal("8333.33"));
        LoanResult loanResult = new LoanResult(loan, 6, new BigDecimal("58.43"), List.of(row1, row2));

        LoanResult savedResult = adapter.save(loanResult);

        assertNotNull(savedResult);
        assertNotNull(savedResult.getLoan().getId());
        assertEquals(2, savedResult.getSchedule().size());

        Optional<LoanResult> foundResult = adapter.findById(savedResult.getLoan().getId());

        assertTrue(foundResult.isPresent());
        assertEquals(savedResult.getLoan().getId(), foundResult.get().getLoan().getId());
        assertEquals(6, foundResult.get().getInstallmentsCount());
    }
}
