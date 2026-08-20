import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LoanFormComponent } from './loan-form';
import { LoanInput } from '../../models/loan.model';

describe('LoanFormComponent', () => {
  let component: LoanFormComponent;
  let fixture: ComponentFixture<LoanFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoanFormComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(LoanFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create form with 5 controls disabled initially', () => {
    expect(component).toBeTruthy();
    expect(component.form.valid).toBe(false);
  });

  it('should invalidate when finalDate is before initialDate', () => {
    component.form.setValue({
      initialDate: '2024-05-10',
      finalDate: '2024-05-01',
      firstPaymentDate: '2024-05-05',
      amount: 1000,
      interestRate: 2,
    });

    expect(component.form.valid).toBe(false);
    expect(component.form.errors?.['finalDateInvalid']).toBeTruthy();
  });

  it('should invalidate when firstPaymentDate is outside range', () => {
    component.form.setValue({
      initialDate: '2024-05-01',
      finalDate: '2024-05-31',
      firstPaymentDate: '2024-06-05',
      amount: 1000,
      interestRate: 2,
    });

    expect(component.form.valid).toBe(false);
    expect(component.form.errors?.['firstPaymentDateInvalid']).toBeTruthy();
  });

  it('should validate and emit calculate event when form is valid', () => {
    let emittedValue: LoanInput | undefined;
    component.calculate.subscribe((val) => {
      emittedValue = val;
    });

    component.form.setValue({
      initialDate: '2024-01-01',
      finalDate: '2024-12-31',
      firstPaymentDate: '2024-01-31',
      amount: 5000,
      interestRate: 1.5,
    });

    expect(component.form.valid).toBe(true);

    component.onSubmit();
    expect(emittedValue).toEqual({
      initialDate: '2024-01-01',
      finalDate: '2024-12-31',
      firstPaymentDate: '2024-01-31',
      amount: 5000,
      interestRate: 1.5,
    });
  });
});

