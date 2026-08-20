package desafiocalculadoraback.adapters.outbound.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "schedule_rows")
public class ScheduleRowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    private LoanEntity loan;

    @Column(name = "event_date", nullable = false)
    private LocalDate date;

    @Column(name = "event_type", nullable = false)
    private String type;

    @Column(name = "installment_number")
    private Integer installmentNumber;

    @Column(name = "days", nullable = false)
    private Long days;

    @Column(name = "interest", nullable = false, precision = 15, scale = 2)
    private BigDecimal interest;

    @Column(name = "amortization", nullable = false, precision = 15, scale = 2)
    private BigDecimal amortization;

    @Column(name = "balance", nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;

    public ScheduleRowEntity() {
    }

    public ScheduleRowEntity(Long id, LoanEntity loan, LocalDate date, String type, Integer installmentNumber, Long days, BigDecimal interest, BigDecimal amortization, BigDecimal balance) {
        this.id = id;
        this.loan = loan;
        this.date = date;
        this.type = type;
        this.installmentNumber = installmentNumber;
        this.days = days;
        this.interest = interest;
        this.amortization = amortization;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LoanEntity getLoan() {
        return loan;
    }

    public void setLoan(LoanEntity loan) {
        this.loan = loan;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getInstallmentNumber() {
        return installmentNumber;
    }

    public void setInstallmentNumber(Integer installmentNumber) {
        this.installmentNumber = installmentNumber;
    }

    public Long getDays() {
        return days;
    }

    public void setDays(Long days) {
        this.days = days;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public BigDecimal getAmortization() {
        return amortization;
    }

    public void setAmortization(BigDecimal amortization) {
        this.amortization = amortization;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
