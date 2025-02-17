import { expect } from '@jest/globals';
import { TestBed } from '@angular/core/testing';
import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService Integration Test', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SessionService],
    });

    service = TestBed.inject(SessionService);
  });

  it('should initialize with isLogged as false', () => {
    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  });

  it('should log in a user and update session state', (done) => {
    const mockSession: SessionInformation = {
      token: 'fake-token',
      type: 'Bearer',
      id: 1,
      username: 'testuser',
      firstName: 'Test',
      lastName: 'User',
      admin: true,
    };

    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(true);
      expect(service.sessionInformation).toEqual(mockSession);
      done();
    });

    service.logIn(mockSession);
  });

  it('should log out a user and reset session state', (done) => {
    const mockSession: SessionInformation = {
      token: 'fake-token',
      type: 'Bearer',
      id: 1,
      username: 'testuser',
      firstName: 'Test',
      lastName: 'User',
      admin: true,
    };

    service.logIn(mockSession);

    service.$isLogged().subscribe((isLogged) => {
      if (!isLogged) {
        expect(service.sessionInformation).toBeUndefined();
        done();
      }
    });

    service.logOut();
  });
});
