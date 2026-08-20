package desafiocalculadoraback.domain.service;

import desafiocalculadoraback.domain.model.Loan;
import desafiocalculadoraback.domain.model.LoanResult;
import desafiocalculadoraback.domain.model.ScheduleRow;
import desafiocalculadoraback.domain.ports.inbound.CalculateLoanUseCase;
import desafiocalculadoraback.domain.ports.outbound.LoanRepositoryPort;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class LoanCalculatorDomainService implements CalculateLoanUseCase {

    private final LoanRepositoryPort repositoryPort;

    public LoanCalculatorDomainService(LoanRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public LoanResult calculateAndSave(Loan loan) {
        List<LocalDate> paymentDates = generatePaymentDates(
                loan.getInitialDate(),
                loan.getFinalDate(),
                loan.getFirstPaymentDate()
        );

        int installmentsCount = paymentDates.size();
        if (installmentsCount == 0) {
            throw new IllegalArgumentException("O período especificado não possui parcelas de pagamento.");
        }

        BigDecimal amortization = loan.getAmount()
                .divide(BigDecimal.valueOf(installmentsCount), 2, RoundingMode.HALF_UP);

        Set<LocalDate> allDates = new TreeSet<>();
        allDates.add(loan.getInitialDate());
        allDates.addAll(paymentDates);

        LocalDate currentMonth = YearMonth.from(loan.getInitialDate()).atEndOfMonth();
        LocalDate lastMonth = YearMonth.from(loan.getFinalDate()).atEndOfMonth();
        while (!currentMonth.isAfter(lastMonth)) {
            if (!currentMonth.isBefore(loan.getInitialDate()) && !currentMonth.isAfter(loan.getFinalDate())) {
                allDates.add(currentMonth);
            }
            currentMonth = currentMonth.plusMonths(1).withDayOfMonth(1).plusMonths(1).minusDays(1);
        }

        List<ScheduleRow> schedule = new ArrayList<>();
        BigDecimal balance = loan.getAmount();
        BigDecimal totalInterest = BigDecimal.ZERO;
        LocalDate previousDate = loan.getInitialDate();
        int currentInstallment = 1;

        for (LocalDate date : allDates) {
            if (date.equals(loan.getInitialDate())) {
                schedule.add(new ScheduleRow(
                        date,
                        "INITIAL",
                        null,
                        0L,
                        BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                        BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                        balance.setScale(2, RoundingMode.HALF_UP)
                ));
            } else {
                long days = ChronoUnit.DAYS.between(previousDate, date);
                BigDecimal interest = balance
                        .multiply(loan.getInterestRate())
                        .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(days))
                        .divide(BigDecimal.valueOf(360), 2, RoundingMode.HALF_UP);

                totalInterest = totalInterest.add(interest);

                boolean isPayment = paymentDates.contains(date);
                BigDecimal currentAmortization = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

                if (isPayment) {
                    currentAmortization = (currentInstallment == installmentsCount) ? balance : amortization;
                    balance = balance.subtract(currentAmortization);
                }

                schedule.add(new ScheduleRow(
                        date,
                        isPayment ? "PAYMENT" : "MONTH_END",
                        isPayment ? currentInstallment++ : null,
                        days,
                        interest,
                        currentAmortization,
                        balance.setScale(2, RoundingMode.HALF_UP)
                ));
            }
            previousDate = date;
        }

        LoanResult result = new LoanResult(
                loan,
                installmentsCount,
                totalInterest.setScale(2, RoundingMode.HALF_UP),
                schedule
        );

        return repositoryPort.save(result);
    }

    @Override
    public LoanResult findById(Long id) {
        return repositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado para o ID: " + id));
    }

    private List<LocalDate> generatePaymentDates(LocalDate start, LocalDate end, LocalDate firstPayment) {
        List<LocalDate> dates = new ArrayList<>();
        int targetDay = firstPayment.getDayOfMonth();
        LocalDate current = firstPayment;

        while (!current.isAfter(end)) {
            dates.add(current);
            YearMonth nextMonth = YearMonth.from(current).plusMonths(1);
            int validDay = Math.min(targetDay, nextMonth.lengthOfMonth());
            current = nextMonth.atDay(validDay);
        }

        return dates;
    }
}
