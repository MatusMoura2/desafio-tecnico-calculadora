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
        amount: [null, [Validators.required, Validators.min(0.01)]],
        interestRate: [null, [Validators.required, Validators.min(0)]],
      },
      { validators: this.dateValidator }
    );
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
      this.calculate.emit(this.form.value as LoanInput);
    }
  }
}
