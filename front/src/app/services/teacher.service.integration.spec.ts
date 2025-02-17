import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TeacherService } from './teacher.service';
import { Teacher } from '../interfaces/teacher.interface';
import { expect } from '@jest/globals';


describe('TeacherService Integration Test', () => {
  let service: TeacherService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TeacherService],
    });

    service = TestBed.inject(TeacherService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); 
  });

  it('should retrieve all teachers from API', () => {
    const mockTeachers: Teacher[] = [
      { id: 1, lastName: 'Smith', firstName: 'John', createdAt: new Date(), updatedAt: new Date() },
      { id: 2, lastName: 'Doe', firstName: 'Jane', createdAt: new Date(), updatedAt: new Date() }
    ];

    service.all().subscribe((teachers) => {
      expect(teachers.length).toBe(2);
      expect(teachers).toEqual(mockTeachers);
    });

    const req = httpMock.expectOne('api/teacher');
    expect(req.request.method).toBe('GET');
    req.flush(mockTeachers); 
  });

  it('should retrieve a teacher by ID from API', () => {
    const mockTeacher: Teacher = { id: 1, lastName: 'Smith', firstName: 'John', createdAt: new Date(), updatedAt: new Date() };

    service.detail('1').subscribe((teacher) => {
      expect(teacher).toEqual(mockTeacher);
    });

    const req = httpMock.expectOne('api/teacher/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockTeacher); 
  });
});
