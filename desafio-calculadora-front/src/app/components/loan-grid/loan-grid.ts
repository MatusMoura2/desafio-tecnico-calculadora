import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoanCalculationResult, ScheduleRow } from '../../models/loan.model';

@Component({
  selector: 'app-loan-grid',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './loan-grid.html',
  styleUrl: './loan-grid.scss',
})
export class LoanGridComponent {
  @Input() public result: LoanCalculationResult | null = null;

  public formatCurrency(val: number): string {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(val || 0);
  }

  public getRowBadgeClass(type: ScheduleRow['type']): string {
    switch (type) {
      case 'INITIAL':
        return 'badge-initial';
      case 'PAYMENT':
        return 'badge-payment';
      case 'END_OF_MONTH':
        return 'badge-end-month';
      default:
        return '';
    }
  }

  public getRowLabel(type: ScheduleRow['type']): string {
    switch (type) {
      case 'INITIAL':
        return 'Data Inicial';
      case 'PAYMENT':
        return 'Pagamento Parcela';
      case 'END_OF_MONTH':
        return 'Fim de Mês';
      default:
        return '';
    }
  }

  public formatDateString(dateStr: string): string {
    if (!dateStr) return '';
    const parts = dateStr.split('-');
    if (parts.length !== 3) return dateStr;
    return `${parts[2]}/${parts[1]}/${parts[0]}`;
  }
}

