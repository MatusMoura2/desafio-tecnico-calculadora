package desafiocalculadoraback.infrastructure.config;

import desafiocalculadoraback.domain.ports.inbound.CalculateLoanUseCase;
import desafiocalculadoraback.domain.ports.outbound.LoanRepositoryPort;
import desafiocalculadoraback.domain.service.LoanCalculatorDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public CalculateLoanUseCase calculateLoanUseCase(LoanRepositoryPort repositoryPort) {
        return new LoanCalculatorDomainService(repositoryPort);
    }
}
