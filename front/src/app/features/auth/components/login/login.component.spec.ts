import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { AuthService } from '../../services/auth.service';
import { of } from 'rxjs';
import { Router } from '@angular/router';




describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [SessionService],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should disable the submit button if the form is invalid', () => {
    const form = component.form; // Get the form instance

    // Ensure the form is invalid
    expect(form.valid).toBeFalsy();

    // Select the submit button
    const submitButton = fixture.nativeElement.querySelector('button[type="submit"]');

    // Verify that the submit button is disabled when the form is invalid
    expect(submitButton.disabled).toBeTruthy(); 
  });

  it('should log in successfully with valid credentials', () => {
      // Inject required services
      const authService = TestBed.inject(AuthService);
      const sessionService = TestBed.inject(SessionService);
      const router = TestBed.inject(Router);

      // Mock router navigation to return a resolved promise
      jest.spyOn(router, 'navigate').mockImplementation(() => Promise.resolve(true));

      // Mock session information that will be returned after a successful login
      const mockSessionInformation: SessionInformation = {
        token: 'fake-jwt-token',
        type: 'Bearer',
        id: 1,
        username: 'testuser',
        firstName: 'Test',
        lastName: 'User',
        admin: false
      };

      // Spy on the login method and return a mock session
      jest.spyOn(authService, 'login').mockReturnValue(of(mockSessionInformation)); 
      jest.spyOn(sessionService, 'logIn'); // Spy on the session login method

      // Set valid credentials in the form
      component.form.controls['email'].setValue('test@example.com');
      component.form.controls['password'].setValue('password123');

      // Call the submit method
      component.submit();

      // Ensure the login method is called with the correct credentials
      expect(authService.login).toHaveBeenCalledWith({
        email: 'test@example.com',
        password: 'password123'
      });

      // Ensure sessionService.logIn() is called with the mock session information
      expect(sessionService.logIn).toHaveBeenCalledWith(mockSessionInformation);

      // Verify that the user is redirected to the sessions page after login
      expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
  });

});
