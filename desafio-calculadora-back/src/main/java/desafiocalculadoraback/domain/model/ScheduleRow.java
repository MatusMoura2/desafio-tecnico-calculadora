package desafiocalculadoraback.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ScheduleRow {

    private LocalDate date;
    private String type;
    private Integer installmentNumber;
    private Long days;
    private BigDecimal interest;
    private BigDecimal amortization;
    private BigDecimal balance;

    public ScheduleRow() {
    }

    public ScheduleRow(LocalDate date, String type, Integer installmentNumber, Long days, BigDecimal interest, BigDecimal amortization, BigDecimal balance) {
        this.date = date;
        this.type = type;
        this.installmentNumber = installmentNumber;
        this.days = days;
        this.interest = interest;
        this.amortization = amortization;
        this.balance = balance;
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
