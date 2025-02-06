import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';

describe('UserService', () => {
  let service: UserService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [UserService],
    });

    service = TestBed.inject(UserService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('delete()', () => {
    it('should send a DELETE request to the correct URL', () => {
      // Arrange
      const userId = '1';

      // Act
      service.delete(userId).subscribe(response => {
        // Vérifier que la réponse est bien reçue
        expect(response).toBeTruthy();
      });

      // Assert
      const req = httpTestingController.expectOne(`api/user/${userId}`);
      expect(req.request.method).toBe('DELETE');

      // Simuler une réponse HTTP réussie
      req.flush({});
    });
  });
});
