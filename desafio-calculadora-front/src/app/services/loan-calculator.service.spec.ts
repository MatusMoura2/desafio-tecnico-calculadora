import { TestBed } from '@angular/core/testing';
import { LoanCalculatorService } from './loan-calculator.service';
import { LoanInput } from '../models/loan.model';

describe('LoanCalculatorService', () => {
  let service: LoanCalculatorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LoanCalculatorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should handle leap year end of month payment dates correctly (Jan 31 -> Feb 29)', () => {
    const firstPay = new Date(2024, 0, 31);
    const end = new Date(2024, 2, 31);
    const dates = service.generatePaymentDates(firstPay, end);

    expect(dates.length).toBe(3);
    expect(service.formatDate(dates[0])).toBe('2024-01-31');
    expect(service.formatDate(dates[1])).toBe('2024-02-29');
    expect(service.formatDate(dates[2])).toBe('2024-03-31');
  });

  it('should calculate loan schedule with initial date, payment dates, and end of month rows', () => {
    const input: LoanInput = {
      initialDate: '2024-01-15',
      finalDate: '2024-03-15',
      firstPaymentDate: '2024-01-31',
      amount: 10000,
      interestRate: 12,
    };

    const result = service.calculate(input);

    expect(result.installmentsCount).toBeGreaterThan(0);
    expect(result.schedule.length).toBeGreaterThan(0);
    expect(result.schedule[0].type).toBe('INITIAL');
    expect(result.schedule[0].date).toBe('2024-01-15');
    expect(result.schedule[result.schedule.length - 1].date).toBe('2024-03-15');
  });
});
