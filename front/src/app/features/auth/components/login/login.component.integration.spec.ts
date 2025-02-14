import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { expect } from '@jest/globals';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { LoginComponent } from './login.component';
import { SessionService } from 'src/app/services/session.service';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let httpMock: HttpTestingController;
  let router: Router;
  let sessionService: SessionService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatInputModule,
        MatIconModule,
        MatButtonModule,
        NoopAnimationsModule,
        HttpClientTestingModule, // Used to intercept HTTP requests
      ],
      providers: [
        SessionService,
        {
          provide: Router,
          useValue: { navigate: jest.fn() }, // Mocking `navigate` using jest.fn()
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController); // Inject HttpTestingController
    router = TestBed.inject(Router);
    sessionService = TestBed.inject(SessionService);
    fixture.detectChanges();
  });

  afterEach(() => {
    httpMock.verify(); // Ensures there are no unresolved HTTP requests
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the form with empty values', () => {
    expect(component.form.value).toEqual({ email: '', password: '' });
  });

  it('should require email and password', () => {
    const emailControl = component.form.get('email');
    const passwordControl = component.form.get('password');

    // Set empty values
    emailControl?.setValue('');
    passwordControl?.setValue('');
    expect(emailControl?.valid).toBeFalsy();
    expect(passwordControl?.valid).toBeFalsy();

    // Set valid values
    emailControl?.setValue('test@example.com');
    passwordControl?.setValue('password');
    expect(emailControl?.valid).toBeTruthy();
    expect(passwordControl?.valid).toBeTruthy();
  });

  it('should call login API and navigate on successful login', () => {
    const loginResponse: SessionInformation = {
      token: 'fake-token',
      type: 'user',
      id: 1,
      username: 'testuser',
      firstName: 'Test',
      lastName: 'User',
      admin: false,
    };

    // Fill the form
    component.form.setValue({ email: 'test@example.com', password: 'password' });
    component.submit();

    // Intercept the HTTP request
    const req = httpMock.expectOne('api/auth/login');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({
      email: 'test@example.com',
      password: 'password',
    });

    // Simulate a successful response
    req.flush(loginResponse);

    // Verify that the user is logged in and redirected
    expect(sessionService.isLogged).toBeTruthy();
    expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('should set onError to true on login failure', () => {
    // Fill the form
    component.form.setValue({ email: 'test@example.com', password: 'password' });
    component.submit();

    // Intercept the HTTP request
    const req = httpMock.expectOne('api/auth/login');
    expect(req.request.method).toBe('POST');

    // Simulate an error response
    req.flush('Error', { status: 401, statusText: 'Unauthorized' });

    // Verify that onError is set to true
    expect(component.onError).toBeTruthy();
  });
});
