import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { of, throwError } from 'rxjs';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,  
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create an account successfully and redirect to login', () => {
    const authService = TestBed.inject(AuthService);
    const router = TestBed.inject(Router);
  
    jest.spyOn(authService, 'register').mockReturnValue(of(undefined));
  
    jest.spyOn(router, 'navigate').mockImplementation(() => Promise.resolve(true));
  
    component.form.controls['email'].setValue('test@example.com');
    component.form.controls['password'].setValue('password123');
    component.form.controls['firstName'].setValue('Test');
    component.form.controls['lastName'].setValue('User');
  
    component.submit();
  
    expect(authService.register).toHaveBeenCalledWith({
      email: 'test@example.com',
      password: 'password123',
      firstName: 'Test',
      lastName: 'User'
    });
  
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should display an error message if account creation fails', () => {
    const authService = TestBed.inject(AuthService);
  
    jest.spyOn(authService, 'register').mockReturnValue(
      throwError(() => new Error('Email already exists'))
    );
  

    component.form.controls['email'].setValue('existing@example.com');
    component.form.controls['password'].setValue('password123');
    component.form.controls['firstName'].setValue('Test');
    component.form.controls['lastName'].setValue('User');
  

    component.submit();
    fixture.detectChanges();
  

    const errorMessage = fixture.nativeElement.querySelector('.error');
    expect(errorMessage).toBeTruthy(); 
    expect(errorMessage.textContent).toContain('An error occurred'); 
  });
  
});
