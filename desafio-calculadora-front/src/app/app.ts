import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoanFormComponent } from './components/loan-form/loan-form';
import { LoanGridComponent } from './components/loan-grid/loan-grid';
import { LoanCalculatorService } from './services/loan-calculator.service';
import { LoanInput, LoanCalculationResult } from './models/loan.model';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, LoanFormComponent, LoanGridComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  public readonly calculationResult = signal<LoanCalculationResult | null>(null);

  constructor(private calculatorService: LoanCalculatorService) {}

  public onCalculate(input: LoanInput): void {
    this.calculatorService.calculate(input).subscribe({
      next: (result) => {
        this.calculationResult.set(result);
      },
      error: (err) => {
        console.error('Erro ao calcular no backend', err);
        // Opcional: mostrar uma mensagem de erro na tela
      }
    });
  }
}
