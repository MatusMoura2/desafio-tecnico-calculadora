package desafiocalculadoraback.domain.model;

import java.math.BigDecimal;
import java.util.List;

public class LoanResult {

    private Loan loan;
    private Integer installmentsCount;
    private BigDecimal totalInterest;
    private List<ScheduleRow> schedule;

    public LoanResult() {
    }

    public LoanResult(Loan loan, Integer installmentsCount, BigDecimal totalInterest, List<ScheduleRow> schedule) {
        this.loan = loan;
        this.installmentsCount = installmentsCount;
        this.totalInterest = totalInterest;
        this.schedule = schedule;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
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

    public List<ScheduleRow> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<ScheduleRow> schedule) {
        this.schedule = schedule;
    }
}
