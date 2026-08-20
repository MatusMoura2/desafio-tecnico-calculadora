package desafiocalculadoraback.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Loan {

    private Long id;
    private LocalDate initialDate;
    private LocalDate finalDate;
    private LocalDate firstPaymentDate;
    private BigDecimal amount;
    private BigDecimal interestRate;

    public Loan() {
    }

    public Loan(Long id, LocalDate initialDate, LocalDate finalDate, LocalDate firstPaymentDate, BigDecimal amount, BigDecimal interestRate) {
        this.id = id;
        this.initialDate = initialDate;
        this.finalDate = finalDate;
        this.firstPaymentDate = firstPaymentDate;
        this.amount = amount;
        this.interestRate = interestRate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(LocalDate initialDate) {
        this.initialDate = initialDate;
    }

    public LocalDate getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(LocalDate finalDate) {
        this.finalDate = finalDate;
    }

    public LocalDate getFirstPaymentDate() {
        return firstPaymentDate;
    }

    public void setFirstPaymentDate(LocalDate firstPaymentDate) {
        this.firstPaymentDate = firstPaymentDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
