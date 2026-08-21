import { Component, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { LoanInput } from '../../models/loan.model';

@Component({
  selector: 'app-loan-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './loan-form.html',
  styleUrl: './loan-form.scss',
})
export class LoanFormComponent {
  @Output() public calculate = new EventEmitter<LoanInput>();

  public form: FormGroup;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group(
      {
        initialDate: ['', Validators.required],
        finalDate: ['', Validators.required],
        firstPaymentDate: ['', Validators.required],
        amountDisplay: ['', [Validators.required]],
        interestRate: [null, [Validators.required, Validators.min(0)]],
      },
      { validators: this.dateValidator }
    );
  }

  public onAmountBlur(): void {
    const val = this.form.get('amountDisplay')?.value;
    if (val) {
      const numericStr = String(val).replace(/\./g, '').replace(',', '.');
      const num = parseFloat(numericStr);
      if (!isNaN(num)) {
        const formatted = num.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
        this.form.get('amountDisplay')?.setValue(formatted, { emitEvent: false });
      }
    }
  }

  public dateValidator(control: AbstractControl): ValidationErrors | null {
    const initialDateVal = control.get('initialDate')?.value;
    const finalDateVal = control.get('finalDate')?.value;
    const firstPaymentDateVal = control.get('firstPaymentDate')?.value;

    if (!initialDateVal || !finalDateVal || !firstPaymentDateVal) {
      return null;
    }

    const errors: ValidationErrors = {};

    const initial = new Date(initialDateVal);
    const final = new Date(finalDateVal);
    const firstPayment = new Date(firstPaymentDateVal);

    if (final <= initial) {
      errors['finalDateInvalid'] = 'A data final deve ser maior que a data inicial.';
    }

    if (firstPayment <= initial || firstPayment >= final) {
      errors['firstPaymentDateInvalid'] = 'A data do primeiro pagamento deve ser maior que a data inicial e menor que a data final.';
    }

    return Object.keys(errors).length > 0 ? errors : null;
  }

  public onSubmit(): void {
    if (this.form.valid) {
      const payload = { ...this.form.value };
      const amountStr = String(payload.amountDisplay).replace(/\./g, '').replace(',', '.');
      payload.amount = parseFloat(amountStr);
      delete payload.amountDisplay;
      this.calculate.emit(payload as LoanInput);
    }
  }
}
