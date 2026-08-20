package desafiocalculadoraback.adapters.outbound.persistence.repository;

import desafiocalculadoraback.adapters.outbound.persistence.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataLoanRepository extends JpaRepository<LoanEntity, Long> {
}
