import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { expect } from '@jest/globals';

describe('AuthService', () => {
  let service: AuthService;
  let httpTestingController: HttpTestingController;

  // Set up the test environment before each test
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule], // Import the testing module for HTTP requests
      providers: [AuthService], // Provide the AuthService for testing
    });

    // Inject the AuthService and HttpTestingController
    service = TestBed.inject(AuthService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  // Ensure there are no outstanding HTTP requests after each test
  afterEach(() => {
    httpTestingController.verify();
  });

  // Test the register method
  describe('#register', () => {
    it('should send a POST request to register a user', () => {
      // Define a mock request body for user registration
      const mockRegisterRequest: RegisterRequest = {
        firstName: 'testuser',
        lastName: 'testuser',
        email: 'test@example.com',
        password: 'securepassword',
      };

      // Call the register method and subscribe to the result
      service.register(mockRegisterRequest).subscribe({
        next: () => {
          // No specific response expected
        },
      });

      // Expect a POST request to the API endpoint
      const req = httpTestingController.expectOne('api/auth/register');
      expect(req.request.method).toBe('POST'); // Ensure the request method is POST
      expect(req.request.body).toEqual(mockRegisterRequest); // Ensure the request body matches

      // Simulate a successful response with no data
      req.flush(null);
    });
  });

  // Test the login method
  describe('#login', () => {
    it('should send a POST request to log in a user and return session information', () => {
      // Define a mock request body for user login
      const mockLoginRequest: LoginRequest = {
        email: 'test@example.com',
        password: 'securepassword',
      };

      // Define a mock session response from the API
      const mockSessionInformation: SessionInformation = {
        token: 'mock-jwt-token',
        id: 1,
        username: 'testuser',
        type: 'some-type',
        firstName: 'John',
        lastName: 'Doe',
        admin: true,
      };

      // Call the login method and subscribe to the result
      service.login(mockLoginRequest).subscribe((sessionInfo) => {
        expect(sessionInfo).toEqual(mockSessionInformation); // Ensure the response matches the expected session info
      });

      // Expect a POST request to the API endpoint
      const req = httpTestingController.expectOne('api/auth/login');
      expect(req.request.method).toBe('POST'); // Ensure the request method is POST
      expect(req.request.body).toEqual(mockLoginRequest); // Ensure the request body matches

      // Simulate a successful response with session information
      req.flush(mockSessionInformation);
    });
  });
});
