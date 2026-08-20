package desafiocalculadoraback.adapters.outbound.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "loans")
public class LoanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "initial_date", nullable = false)
    private LocalDate initialDate;

    @Column(name = "final_date", nullable = false)
    private LocalDate finalDate;

    @Column(name = "first_payment_date", nullable = false)
    private LocalDate firstPaymentDate;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "interest_rate", nullable = false, precision = 8, scale = 4)
    private BigDecimal interestRate;

    @Column(name = "installments_count", nullable = false)
    private Integer installmentsCount;

    @Column(name = "total_interest", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalInterest;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ScheduleRowEntity> schedule = new ArrayList<>();

    public LoanEntity() {
    }

    public LoanEntity(Long id, LocalDate initialDate, LocalDate finalDate, LocalDate firstPaymentDate, BigDecimal amount, BigDecimal interestRate, Integer installmentsCount, BigDecimal totalInterest) {
        this.id = id;
        this.initialDate = initialDate;
        this.finalDate = finalDate;
        this.firstPaymentDate = firstPaymentDate;
        this.amount = amount;
        this.interestRate = interestRate;
        this.installmentsCount = installmentsCount;
        this.totalInterest = totalInterest;
    }

    public void addScheduleRow(ScheduleRowEntity row) {
        row.setLoan(this);
        this.schedule.add(row);
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

    public Integer getInstallmentsCount() {
        return installmentsCount;
    }

    public void setInstallmentsCount(Integer installmentsCount) {
        this.installmentsCount = installmentsCount;
    }

    public BigDecimal getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(BigDecimal totalInterest) {
        this.totalInterest = totalInterest;
    }

    public List<ScheduleRowEntity> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<ScheduleRowEntity> schedule) {
        this.schedule = schedule;
    }
}
