import { Injectable } from '@angular/core';
import { LoanInput, LoanCalculationResult, ScheduleRow } from '../models/loan.model';

@Injectable({
  providedIn: 'root',
})
export class LoanCalculatorService {
  public calculate(input: LoanInput): LoanCalculationResult {
    const startDate = this.parseDate(input.initialDate);
    const endDate = this.parseDate(input.finalDate);
    const firstPayDate = this.parseDate(input.firstPaymentDate);

    const paymentDates = this.generatePaymentDates(firstPayDate, endDate);
    const installmentsCount = paymentDates.length;

    const allDatesMap = new Map<string, { isPayment: boolean; installmentNum?: number; isEndOfMonth: boolean }>();

    allDatesMap.set(this.formatDate(startDate), { isPayment: false, isEndOfMonth: false });

    for (let i = 0; i < paymentDates.length; i++) {
      const pDate = paymentDates[i];
      const key = this.formatDate(pDate);
      allDatesMap.set(key, { isPayment: true, installmentNum: i + 1, isEndOfMonth: false });
    }

    const endOfMonths = this.generateEndOfMonthDates(startDate, endDate);
    for (const eDate of endOfMonths) {
      const key = this.formatDate(eDate);
      if (allDatesMap.has(key)) {
        const existing = allDatesMap.get(key)!;
        existing.isEndOfMonth = true;
      } else {
        allDatesMap.set(key, { isPayment: false, isEndOfMonth: true });
      }
    }

    const sortedDates = Array.from(allDatesMap.entries())
      .map(([dateStr, info]) => ({ date: this.parseDate(dateStr), dateStr, ...info }))
      .sort((a, b) => a.date.getTime() - b.date.getTime());

    const schedule: ScheduleRow[] = [];
    let currentBalance = input.amount;
    let totalInterest = 0;
    let prevDate = startDate;

    const fixedAmortization = installmentsCount > 0 ? input.amount / installmentsCount : 0;

    for (let i = 0; i < sortedDates.length; i++) {
      const item = sortedDates[i];
      const days = i === 0 ? 0 : this.getDaysDifference(prevDate, item.date);

      const interestRatePerDay = (input.interestRate / 100) / 360;
      const periodInterest = i === 0 ? 0 : currentBalance * interestRatePerDay * days;
      const roundedInterest = Math.round(periodInterest * 100) / 100;

      totalInterest += roundedInterest;

      let amortization = 0;
      let rowType: ScheduleRow['type'] = 'INITIAL';

      if (i === 0) {
        rowType = 'INITIAL';
      } else if (item.isPayment) {
        rowType = 'PAYMENT';
        amortization = i === sortedDates.length - 1 ? currentBalance : Math.min(fixedAmortization, currentBalance);
      } else {
        rowType = 'END_OF_MONTH';
      }

      currentBalance = Math.max(0, currentBalance - amortization);

      schedule.push({
        date: item.dateStr,
        type: rowType,
        installmentNumber: item.installmentNum,
        days,
        interest: roundedInterest,
        amortization: Math.round(amortization * 100) / 100,
        balance: Math.round(currentBalance * 100) / 100,
      });

      prevDate = item.date;
    }

    return {
      installmentsCount,
      totalInterest: Math.round(totalInterest * 100) / 100,
      schedule,
    };
  }

  public generatePaymentDates(firstPaymentDate: Date, endDate: Date): Date[] {
    const dates: Date[] = [];
    const targetDay = firstPaymentDate.getDate();
    let currentYear = firstPaymentDate.getFullYear();
    let currentMonth = firstPaymentDate.getMonth();

    while (true) {
      const maxDaysInMonth = new Date(currentYear, currentMonth + 1, 0).getDate();
      const actualDay = Math.min(targetDay, maxDaysInMonth);
      const currentDate = new Date(currentYear, currentMonth, actualDay);

      if (currentDate.getTime() > endDate.getTime()) {
        break;
      }

      dates.push(currentDate);

      if (currentDate.getTime() === endDate.getTime()) {
        break;
      }

      currentMonth++;
      if (currentMonth > 11) {
        currentMonth = 0;
        currentYear++;
      }
    }

    if (dates.length > 0) {
      const lastDate = dates[dates.length - 1];
      if (lastDate.getTime() !== endDate.getTime()) {
        dates.push(new Date(endDate.getTime()));
      }
    } else {
      dates.push(new Date(endDate.getTime()));
    }

    return dates;
  }

  public generateEndOfMonthDates(startDate: Date, endDate: Date): Date[] {
    const result: Date[] = [];
    let currentYear = startDate.getFullYear();
    let currentMonth = startDate.getMonth();

    while (true) {
      const endOfMonthDay = new Date(currentYear, currentMonth + 1, 0).getDate();
      const endOfMonthDate = new Date(currentYear, currentMonth, endOfMonthDay);

      if (endOfMonthDate.getTime() > startDate.getTime() && endOfMonthDate.getTime() < endDate.getTime()) {
        result.push(endOfMonthDate);
      }

      if (endOfMonthDate.getTime() >= endDate.getTime()) {
        break;
      }

      currentMonth++;
      if (currentMonth > 11) {
        currentMonth = 0;
        currentYear++;
      }
    }

    return result;
  }

  public getDaysDifference(d1: Date, d2: Date): number {
    const timeDiff = d2.getTime() - d1.getTime();
    return Math.round(timeDiff / (1000 * 3600 * 24));
  }

  public parseDate(dateStr: string): Date {
    const parts = dateStr.split('-');
    return new Date(parseInt(parts[0], 10), parseInt(parts[1], 10) - 1, parseInt(parts[2], 10));
  }

  public formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }
}
