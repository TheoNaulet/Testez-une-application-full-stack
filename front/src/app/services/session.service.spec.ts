import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('logIn()', () => {
    it('should set sessionInformation and update isLogged to true', () => {
      // Arrange
      const user: SessionInformation = {
        token: 'test-token',
        type: 'Bearer',
        id: 1,
        username: 'johndoe',
        firstName: 'John',
        lastName: 'Doe',
        admin: true,
      };

      // Act
      service.logIn(user);

      // Assert
      expect(service.sessionInformation).toEqual(user);
      expect(service.isLogged).toBe(true);
    });

    it('should emit true in $isLogged()', (done) => {
      const user: SessionInformation = {
        token: 'test-token',
        type: 'Bearer',
        id: 2,
        username: 'janedoe',
        firstName: 'Jane',
        lastName: 'Doe',
        admin: false,
      };

      service.$isLogged().subscribe((isLogged) => {
        expect(isLogged).toBe(true);
        done();
      });

      service.logIn(user);
    });
  });

  describe('logOut()', () => {
    it('should clear sessionInformation and set isLogged to false', () => {
      // Arrange
      service.sessionInformation = {
        token: 'test-token',
        type: 'Bearer',
        id: 1,
        username: 'johndoe',
        firstName: 'John',
        lastName: 'Doe',
        admin: true,
      };
      service.isLogged = true;

      // Act
      service.logOut();

      // Assert
      expect(service.sessionInformation).toBeUndefined();
      expect(service.isLogged).toBe(false);
    });

    it('should emit false in $isLogged()', (done) => {
      service.logOut();

      service.$isLogged().subscribe((isLogged) => {
        expect(isLogged).toBe(false);
        done();
      });
    });
  });
});
