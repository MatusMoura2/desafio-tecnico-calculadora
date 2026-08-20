import { LoanInput, ScheduleRow, LoanCalculationResult } from './loan.model';

describe('LoanModel', () => {
  it('should instantiate valid LoanInput data structure', () => {
    const input: LoanInput = {
      initialDate: '2024-01-01',
      finalDate: '2024-12-31',
      firstPaymentDate: '2024-01-31',
      amount: 10000,
      interestRate: 1.5,
    };

    expect(input.amount).toBe(10000);
    expect(input.interestRate).toBe(1.5);
  });

  it('should support schedule row mapping', () => {
    const row: ScheduleRow = {
      date: '2024-01-01',
      type: 'INITIAL',
      days: 0,
      interest: 0,
      amortization: 0,
      balance: 10000,
    };

    const result: LoanCalculationResult = {
      installmentsCount: 12,
      totalInterest: 500,
      schedule: [row],
    };

    expect(result.schedule.length).toBe(1);
    expect(result.schedule[0].type).toBe('INITIAL');
  });
});
