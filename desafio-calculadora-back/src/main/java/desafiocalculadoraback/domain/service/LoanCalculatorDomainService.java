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
        int installmentsCount = (int) ChronoUnit.MONTHS.between(
                YearMonth.from(loan.getInitialDate()),
                YearMonth.from(loan.getFinalDate())
        );

        if (installmentsCount <= 0) {
            throw new IllegalArgumentException("O período especificado não possui parcelas de pagamento.");
        }

        List<LocalDate> paymentDates = generatePaymentDates(
                loan.getFinalDate(),
                loan.getFirstPaymentDate(),
                installmentsCount
        );

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
        BigDecimal accumulatedInterest = BigDecimal.ZERO;
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
                        BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                        BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                        balance.setScale(2, RoundingMode.HALF_UP)
                ));
            } else {
                long days = ChronoUnit.DAYS.between(previousDate, date);
                double rate = loan.getInterestRate().doubleValue() / 100.0;
                double fraction = (double) days / 360.0;
                double factor = Math.pow(1.0 + rate, fraction) - 1.0;
                BigDecimal currentInterest = balance.multiply(BigDecimal.valueOf(factor));

                accumulatedInterest = accumulatedInterest.add(currentInterest);
                totalInterest = totalInterest.add(currentInterest);

                boolean isPayment = paymentDates.contains(date);
                BigDecimal currentAmortization = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
                BigDecimal accumulatedBeforePayment = accumulatedInterest;
                BigDecimal paid = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

                if (isPayment) {
                    currentAmortization = (currentInstallment == installmentsCount) ? balance : amortization;
                    balance = balance.subtract(currentAmortization);
                    paid = accumulatedInterest;
                    accumulatedInterest = BigDecimal.ZERO;
                }

                schedule.add(new ScheduleRow(
                        date,
                        isPayment ? "PAYMENT" : "MONTH_END",
                        isPayment ? currentInstallment++ : null,
                        days,
                        currentInterest.setScale(2, RoundingMode.HALF_UP), // interestProvision
                        accumulatedBeforePayment.setScale(2, RoundingMode.HALF_UP), // interestAccumulated
                        paid.setScale(2, RoundingMode.HALF_UP), // interestPaid
                        currentAmortization.setScale(2, RoundingMode.HALF_UP),
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

    private List<LocalDate> generatePaymentDates(LocalDate end, LocalDate firstPayment, int installmentsCount) {
        List<LocalDate> dates = new ArrayList<>();
        int targetDay = firstPayment.getDayOfMonth();

        for (int i = 0; i < installmentsCount; i++) {
            if (i == installmentsCount - 1) {
                dates.add(end);
            } else {
                YearMonth currentMonth = YearMonth.from(firstPayment).plusMonths(i);
                int validDay = Math.min(targetDay, currentMonth.lengthOfMonth());
                dates.add(currentMonth.atDay(validDay));
            }
        }

        return dates;
    }
}
