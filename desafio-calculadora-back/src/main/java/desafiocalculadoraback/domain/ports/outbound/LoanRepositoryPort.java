package desafiocalculadoraback.domain.ports.outbound;

import desafiocalculadoraback.domain.model.LoanResult;

import java.util.Optional;

public interface LoanRepositoryPort {

    LoanResult save(LoanResult loanResult);

    Optional<LoanResult> findById(Long id);
}
