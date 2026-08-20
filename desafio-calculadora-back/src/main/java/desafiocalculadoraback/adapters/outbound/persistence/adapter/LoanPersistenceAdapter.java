package desafiocalculadoraback.adapters.outbound.persistence.adapter;

import desafiocalculadoraback.adapters.outbound.persistence.entity.LoanEntity;
import desafiocalculadoraback.adapters.outbound.persistence.entity.ScheduleRowEntity;
import desafiocalculadoraback.adapters.outbound.persistence.repository.SpringDataLoanRepository;
import desafiocalculadoraback.domain.model.Loan;
import desafiocalculadoraback.domain.model.LoanResult;
import desafiocalculadoraback.domain.model.ScheduleRow;
import desafiocalculadoraback.domain.ports.outbound.LoanRepositoryPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class LoanPersistenceAdapter implements LoanRepositoryPort {

    private final SpringDataLoanRepository repository;

    public LoanPersistenceAdapter(SpringDataLoanRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public LoanResult save(LoanResult loanResult) {
        Loan loan = loanResult.getLoan();
        LoanEntity entity = new LoanEntity(
                loan.getId(),
                loan.getInitialDate(),
                loan.getFinalDate(),
                loan.getFirstPaymentDate(),
                loan.getAmount(),
                loan.getInterestRate(),
                loanResult.getInstallmentsCount(),
                loanResult.getTotalInterest()
        );

        if (loanResult.getSchedule() != null) {
            for (ScheduleRow row : loanResult.getSchedule()) {
                ScheduleRowEntity rowEntity = new ScheduleRowEntity(
                        null,
                        entity,
                        row.getDate(),
                        row.getType(),
                        row.getInstallmentNumber(),
                        row.getDays(),
                        row.getInterest(),
                        row.getAmortization(),
                        row.getBalance()
                );
                entity.addScheduleRow(rowEntity);
            }
        }

        LoanEntity savedEntity = repository.save(entity);
        return mapToDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LoanResult> findById(Long id) {
        return repository.findById(id).map(this::mapToDomain);
    }

    private LoanResult mapToDomain(LoanEntity entity) {
        Loan loan = new Loan(
                entity.getId(),
                entity.getInitialDate(),
                entity.getFinalDate(),
                entity.getFirstPaymentDate(),
                entity.getAmount(),
                entity.getInterestRate()
        );

        List<ScheduleRow> schedule = entity.getSchedule().stream()
                .map(row -> new ScheduleRow(
                        row.getDate(),
                        row.getType(),
                        row.getInstallmentNumber(),
                        row.getDays(),
                        row.getInterest(),
                        row.getAmortization(),
                        row.getBalance()
                ))
                .collect(Collectors.toList());

        return new LoanResult(loan, entity.getInstallmentsCount(), entity.getTotalInterest(), schedule);
    }
}
