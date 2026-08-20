package desafiocalculadoraback.domain.ports.inbound;

import desafiocalculadoraback.domain.model.Loan;
import desafiocalculadoraback.domain.model.LoanResult;

public interface CalculateLoanUseCase {

    LoanResult calculateAndSave(Loan loan);

    LoanResult findById(Long id);
}
