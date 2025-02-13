import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  // Set up the test environment before each test
  beforeEach(() => {
    TestBed.configureTestingModule({}); // Initialize the testing module
    service = TestBed.inject(SessionService); // Inject the SessionService instance
  });

  // Basic test to check if the service is created successfully
  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // Test the logIn() method
  describe('logIn()', () => {
    it('should set sessionInformation and update isLogged to true', () => {
      // Arrange: Create a mock session information object
      const user: SessionInformation = {
        token: 'test-token',
        type: 'Bearer',
        id: 1,
        username: 'johndoe',
        firstName: 'John',
        lastName: 'Doe',
        admin: true,
      };

      // Act: Call the logIn() method with the mock user data
      service.logIn(user);

      // Assert: Verify that sessionInformation is correctly set and isLogged is true
      expect(service.sessionInformation).toEqual(user);
      expect(service.isLogged).toBe(true);
    });

    it('should emit true in $isLogged()', (done) => {
      // Arrange: Create a mock session information object
      const user: SessionInformation = {
        token: 'test-token',
        type: 'Bearer',
        id: 2,
        username: 'janedoe',
        firstName: 'Jane',
        lastName: 'Doe',
        admin: false,
      };

      // Subscribe to the observable to listen for login status changes
      service.$isLogged().subscribe((isLogged) => {
        expect(isLogged).toBe(true); // Ensure that login status is emitted as true
        done();
      });

      // Act: Call logIn() to update session state
      service.logIn(user);
    });
  });

  // Test the logOut() method
  describe('logOut()', () => {
    it('should clear sessionInformation and set isLogged to false', () => {
      // Arrange: Initialize the session with mock user data
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

      // Act: Call the logOut() method to clear session data
      service.logOut();

      // Assert: Verify that sessionInformation is cleared and isLogged is false
      expect(service.sessionInformation).toBeUndefined();
      expect(service.isLogged).toBe(false);
    });

    it('should emit false in $isLogged()', (done) => {
      // Act: Call logOut() to update session state
      service.logOut();

      // Subscribe to the observable to listen for login status changes
      service.$isLogged().subscribe((isLogged) => {
        expect(isLogged).toBe(false); // Ensure that login status is emitted as false
        done();
      });
    });
  });
});
