export type ScheduleRowType = 'INITIAL' | 'PAYMENT' | 'END_OF_MONTH';

export interface LoanInput {
  initialDate: string;
  finalDate: string;
  firstPaymentDate: string;
  amount: number;
  interestRate: number;
}

export interface ScheduleRow {
  date: string;
  type: ScheduleRowType;
  installmentNumber?: number;
  days: number;
  interestProvision: number;
  interestAccumulated: number;
  interestPaid: number;
  amortization: number;
  balance: number;
}

export interface LoanCalculationResult {
  installmentsCount: number;
  totalInterest: number;
  schedule: ScheduleRow[];
}
