import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { expect } from '@jest/globals';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('AuthService Integration Test', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule], 
      providers: [AuthService], 
    });

    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Ensure there are no pending requests
  });

  describe('#register', () => {
    it('should send a POST request to register a user', () => {
      const mockRegisterRequest: RegisterRequest = {
        firstName: 'John',
        lastName: 'Doe',
        email: 'john.doe@example.com',
        password: 'securepassword',
      };

      service.register(mockRegisterRequest).subscribe(response => {
        expect(response).toBeUndefined(); // API does not return data
      });

      const req = httpMock.expectOne('api/auth/register');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(mockRegisterRequest);

      req.flush(null); // Simulate a successful response
    });

    it('should handle a registration failure', () => {
      const mockRegisterRequest: RegisterRequest = {
        firstName: 'John',
        lastName: 'Doe',
        email: 'john.doe@example.com',
        password: 'securepassword',
      };

      service.register(mockRegisterRequest).subscribe({
        next: () => fail('Should have failed with 400 error'),
        error: (error) => {
          expect(error.status).toBe(400);
          expect(error.statusText).toBe('Bad Request');
        },
      });

      const req = httpMock.expectOne('api/auth/register');
      expect(req.request.method).toBe('POST');

      req.flush({ message: 'Invalid data' }, { status: 400, statusText: 'Bad Request' });
    });
  });

  describe('#login', () => {
    it('should send a POST request to log in a user and return session information', () => {
      const mockLoginRequest: LoginRequest = {
        email: 'john.doe@example.com',
        password: 'securepassword',
      };

      const mockSessionInformation: SessionInformation = {
        token: 'mock-token',
        id: 1,
        username: 'johndoe',
        type: 'user',
        firstName: 'John',
        lastName: 'Doe',
        admin: false,
      };

      service.login(mockLoginRequest).subscribe(sessionInfo => {
        expect(sessionInfo).toEqual(mockSessionInformation);
      });

      const req = httpMock.expectOne('api/auth/login');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(mockLoginRequest);

      req.flush(mockSessionInformation);
    });

    it('should handle a login failure', () => {
      const mockLoginRequest: LoginRequest = {
        email: 'john.doe@example.com',
        password: 'wrongpassword',
      };

      service.login(mockLoginRequest).subscribe({
        next: () => fail('Should have failed with 401 error'),
        error: (error) => {
          expect(error.status).toBe(401);
          expect(error.statusText).toBe('Unauthorized');
        },
      });

      const req = httpMock.expectOne('api/auth/login');
      expect(req.request.method).toBe('POST');

      req.flush({ message: 'Invalid credentials' }, { status: 401, statusText: 'Unauthorized' });
    });
  });
});
