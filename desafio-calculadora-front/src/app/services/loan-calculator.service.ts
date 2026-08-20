import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoanInput, LoanCalculationResult } from '../models/loan.model';

@Injectable({
  providedIn: 'root',
})
export class LoanCalculatorService {
  private apiUrl = 'http://localhost:8080/api/emprestimos/calcular';

  constructor(private http: HttpClient) {}

  public calculate(input: LoanInput): Observable<LoanCalculationResult> {
    return this.http.post<LoanCalculationResult>(this.apiUrl, input);
  }
}
