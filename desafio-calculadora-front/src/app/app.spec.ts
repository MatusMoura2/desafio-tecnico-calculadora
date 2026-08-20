import { ComponentFixture, TestBed } from '@angular/core/testing';
import { App } from './app';
import { LoanInput } from './models/loan.model';

describe('App', () => {
  let component: App;
  let fixture: ComponentFixture<App>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [App],
    }).compileComponents();

    fixture = TestBed.createComponent(App);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  it('should calculate loan schedule when form triggers calculate', () => {
    const input: LoanInput = {
      initialDate: '2024-01-01',
      finalDate: '2024-06-30',
      firstPaymentDate: '2024-01-31',
      amount: 10000,
      interestRate: 2,
    };

    component.onCalculate(input);
    fixture.detectChanges();

    expect(component.calculationResult()).not.toBeNull();
    expect(component.calculationResult()?.schedule.length).toBeGreaterThan(0);

    const element: HTMLElement = fixture.nativeElement;
    const gridElement = element.querySelector('app-loan-grid');
    expect(gridElement).toBeTruthy();
  });
});
