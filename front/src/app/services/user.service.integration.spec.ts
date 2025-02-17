import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { UserService } from './user.service';
import { User } from '../interfaces/user.interface';
import { expect } from '@jest/globals';

describe('UserService Integration Test', () => {
  let service: UserService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [UserService],
    });

    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Ensures no unexpected HTTP calls
  });

  it('should retrieve a user by ID from API', () => {
    const mockUser: User = {
      id: 1,
      email: 'john.doe@test.com',
      lastName: 'Doe',
      firstName: 'John',
      admin: false,
      password: 'securepassword',
      createdAt: new Date(),
      updatedAt: new Date(),
    };

    service.getById('1').subscribe((user) => {
      expect(user).toEqual(mockUser);
    });

    const req = httpMock.expectOne('api/user/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockUser); // Simulate API response
  });

  it('should send a DELETE request to delete a user', () => {
    service.delete('1').subscribe((response) => {
      expect(response).toEqual({});
    });

    const req = httpMock.expectOne('api/user/1');
    expect(req.request.method).toBe('DELETE');
    req.flush({}); // Simulate API response
  });
});
