import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import { SessionApiService } from "../features/sessions/services/session-api.service";
import { Teacher } from "../interfaces/teacher.interface";
import { of } from "rxjs";

describe('TeacherService', () => {
  let service: TeacherService;
  let httpClientMock: any;
  let teacherMock: Teacher;

  // Set up the test environment before each test
  beforeEach(() => {
    // Create a mock HttpClient with a jest function for GET requests
    httpClientMock = {
      get: jest.fn()
    };

    TestBed.configureTestingModule({
      providers: [
        SessionApiService, // Provide the session API service
        { provide: HttpClient, useValue: httpClientMock }, // Replace HttpClient with the mock
      ],
      imports: [
        HttpClientModule // Import HttpClientModule for HTTP operations
      ]
    });

    // Inject the TeacherService for testing
    service = TestBed.inject(TeacherService);

    // Define a mock teacher object
    teacherMock = {
      id: 1,
      lastName: 'Doe',
      firstName: 'John',
      createdAt: new Date(),
      updatedAt: new Date()
    };
  });

  // Test to ensure the TeacherService is created successfully
  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // Test the `all()` method to retrieve all teachers
  it('should return an Observable<Teacher[]> for all', () => {
    const teachersMock: Teacher[] = [teacherMock];

    // Mock the HTTP GET request to return an observable of the teachers array
    httpClientMock.get.mockReturnValue(of(teachersMock));

    // Call the service method and verify it returns the expected mock data
    service.all().subscribe((teachers) => {
      expect(teachers).toEqual(teachersMock);
    });
  });

  // Test the `detail()` method to retrieve a specific teacher by ID
  it('should return an Observable<Teacher> for detail', () => {
    // Mock the HTTP GET request to return an observable of a single teacher
    httpClientMock.get.mockReturnValue(of(teacherMock));

    // Call the service method with a teacher ID and verify the expected result
    service.detail('1').subscribe((teacher) => {
      expect(teacher).toEqual(teacherMock);
    });
  });
});
