import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';

describe('UserService', () => {
  let service: UserService;
  let httpTestingController: HttpTestingController;

  // Set up the test environment before each test
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule], // Import the testing module for HTTP requests
      providers: [UserService], // Provide the UserService for testing
    });

    // Inject the UserService and HttpTestingController
    service = TestBed.inject(UserService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  // Ensure there are no outstanding HTTP requests after each test
  afterEach(() => {
    httpTestingController.verify();
  });

  // Basic test to check if the service is created successfully
  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // Test the delete() method of UserService
  describe('delete()', () => {
    it('should send a DELETE request to the correct URL', () => {
      // Arrange: Define a sample user ID to delete
      const userId = '1';

      // Act: Call the delete method and subscribe to the response
      service.delete(userId).subscribe(response => {
        // Ensure that a response is received (it should not be null or undefined)
        expect(response).toBeTruthy();
      });

      // Assert: Expect a DELETE request to the API endpoint with the correct user ID
      const req = httpTestingController.expectOne(`api/user/${userId}`);
      expect(req.request.method).toBe('DELETE'); // Ensure the request method is DELETE

      // Simulate a successful HTTP response
      req.flush({});
    });
  });
});
