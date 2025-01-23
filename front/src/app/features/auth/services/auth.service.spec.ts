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

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService],
    });
    service = TestBed.inject(AuthService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify(); // Vérifie qu'il n'y a pas de requêtes non gérées
  });

  describe('#register', () => {
    it('should send a POST request to register a user', () => {
      const mockRegisterRequest: RegisterRequest = {
        firstName: 'testuser',
        lastName:'testuser',
        email: 'test@example.com',
        password: 'securepassword',
      };

      service.register(mockRegisterRequest).subscribe({
        next: () => {
          // Pas de réponse spécifique attendue
        },
      });

      const req = httpTestingController.expectOne('api/auth/register');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(mockRegisterRequest);
      req.flush(null); // Simule une réponse vide
    });
  });

  describe('#login', () => {
    it('should send a POST request to log in a user and return session information', () => {
      const mockLoginRequest: LoginRequest = {
        email: 'test@example.com',
        password: 'securepassword',
      };


      const mockSessionInformation: SessionInformation = {
        token: 'mock-jwt-token',

          id: 1,
          username: 'testuser',
          type: 'some-type',
          firstName: 'John',
          lastName: 'Doe',
          admin: true

      };

      service.login(mockLoginRequest).subscribe((sessionInfo) => {
        expect(sessionInfo).toEqual(mockSessionInformation);
      });

      const req = httpTestingController.expectOne('api/auth/login');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(mockLoginRequest);
      req.flush(mockSessionInformation); // Simule une réponse avec des données de session
    });
  });
});
