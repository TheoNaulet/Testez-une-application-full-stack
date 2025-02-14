import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Router } from '@angular/router';
import { expect } from '@jest/globals';
import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';

describe('RegisterComponent Integration Test', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let httpMock: HttpTestingController;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatInputModule,
        MatButtonModule,
        HttpClientTestingModule,
        NoopAnimationsModule,
      ],
      providers: [
        AuthService,
        {
          provide: Router,
          useValue: { navigate: jest.fn() }, // Mock Router navigation
        },
      ],
    }).compileComponents();
  

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  afterEach(() => {
    httpMock.verify(); // Ensures no unresolved HTTP requests
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the form with empty values', () => {
    expect(component.form.value).toEqual({
      email: '',
      firstName: '',
      lastName: '',
      password: '',
    });
  });

  it('should require all fields to be filled', () => {
    component.form.setValue({
      email: '',
      firstName: '',
      lastName: '',
      password: '',
    });

    expect(component.form.valid).toBeFalsy();

    component.form.setValue({
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123',
    });

    expect(component.form.valid).toBeTruthy();
  });

  it('should call register API and navigate on successful registration', fakeAsync(() => {
    // Fill the form
    component.form.setValue({
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123',
    });

    component.submit();

    // Intercept the HTTP request
    const req = httpMock.expectOne('api/auth/register');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123',
    });

    // Simulate a successful response
    req.flush({});

    tick(); // Simulate async processing

    // Ensure redirection occurs
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  }));

  it('should set onError to true on registration failure', fakeAsync(() => {
    // Fill the form
    component.form.setValue({
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123',
    });

    component.submit();

    // Intercept the HTTP request
    const req = httpMock.expectOne('api/auth/register');
    expect(req.request.method).toBe('POST');

    // Simulate an error response
    req.flush('Error', { status: 400, statusText: 'Bad Request' });

    tick();

    // Verify error handling
    expect(component.onError).toBeTruthy();
  }));
});
