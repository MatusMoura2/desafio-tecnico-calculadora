import { ComponentFixture, TestBed } from '@angular/core/testing';
import { registerLocaleData } from '@angular/common';
import localePt from '@angular/common/locales/pt';
import { LoanGridComponent } from './loan-grid';
import { LoanCalculationResult } from '../../models/loan.model';

registerLocaleData(localePt);

describe('LoanGridComponent', () => {
  let component: LoanGridComponent;
  let fixture: ComponentFixture<LoanGridComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoanGridComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(LoanGridComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should format date strings to dd/MM/yyyy', () => {
    expect(component.formatDateString('2024-01-31')).toBe('31/01/2024');
    expect(component.formatDateString('2024-12-05')).toBe('05/12/2024');
  });

  it('should render table rows when result is passed', () => {
    const mockResult: LoanCalculationResult = {
      installmentsCount: 2,
      totalInterest: 150.25,
      schedule: [
        { date: '2024-01-01', type: 'INITIAL', days: 0, interest: 0, amortization: 0, balance: 10000 },
        { date: '2024-01-31', type: 'PAYMENT', installmentNumber: 1, days: 30, interest: 100, amortization: 5000, balance: 5000 },
      ],
    };

    fixture.componentRef.setInput('result', mockResult);
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const rows = compiled.querySelectorAll('tbody tr');
    expect(rows.length).toBe(2);
  });

});

