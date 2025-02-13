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
    // Inject required services
    const authService = TestBed.inject(AuthService);
    const router = TestBed.inject(Router);

    // Mock the register method to simulate a successful registration (returns an observable of undefined)
    jest.spyOn(authService, 'register').mockReturnValue(of(undefined));

    // Mock router navigation to prevent actual redirection
    jest.spyOn(router, 'navigate').mockImplementation(() => Promise.resolve(true));

    // Set valid user details in the form fields
    component.form.controls['email'].setValue('test@example.com');
    component.form.controls['password'].setValue('password123');
    component.form.controls['firstName'].setValue('Test');
    component.form.controls['lastName'].setValue('User');

    // Call the submit method (which triggers the registration process)
    component.submit();

    // Verify that authService.register() was called with the correct user details
    expect(authService.register).toHaveBeenCalledWith({
      email: 'test@example.com',
      password: 'password123',
      firstName: 'Test',
      lastName: 'User'
    });

    // Verify that the user is redirected to the login page after successful registration
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should display an error message if account creation fails', () => {
      // Inject the authentication service
      const authService = TestBed.inject(AuthService);

      // Mock the register method to simulate a failed registration attempt (throws an error)
      jest.spyOn(authService, 'register').mockReturnValue(
        throwError(() => new Error('Email already exists'))
      );

      // Set user details in the form fields (using an already existing email)
      component.form.controls['email'].setValue('existing@example.com');
      component.form.controls['password'].setValue('password123');
      component.form.controls['firstName'].setValue('Test');
      component.form.controls['lastName'].setValue('User');

      // Call the submit method (which triggers the registration process)
      component.submit();
      
      // Trigger change detection to update the view
      fixture.detectChanges();

      // Select the error message element from the DOM
      const errorMessage = fixture.nativeElement.querySelector('.error');

      // Ensure that the error message is displayed
      expect(errorMessage).toBeTruthy(); 
      expect(errorMessage.textContent).toContain('An error occurred'); 
  });
});
