import {HttpClient, HttpClientModule} from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import {SessionApiService} from "../features/sessions/services/session-api.service";
import {Teacher} from "../interfaces/teacher.interface";
import {of} from "rxjs";

describe('TeacherService', () => {
  let service: TeacherService;
  let httpClientMock: any;
  let teacherMock: Teacher;

  beforeEach(() => {
    httpClientMock = {
      get: jest.fn()
    };
    TestBed.configureTestingModule({
      providers: [
        SessionApiService,
        { provide: HttpClient, useValue: httpClientMock },
      ],
      imports:[
        HttpClientModule
      ]
    });
    service = TestBed.inject(TeacherService);

    teacherMock = {
      id: 1,
      lastName: 'Doe',
      firstName: 'John',
      createdAt: new Date(),
      updatedAt: new Date()
    };

  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  /*
    On simule des appels HTTP des différentes fonctions du fichier teacher.service.ts
    qui sont censé retournés des observables (soit de type Teacher[] ou Teacher)
   */

  it('should return an Observable<Teacher[]> for all', () => {
    const teachersMock: Teacher[] = [teacherMock];
    httpClientMock.get.mockReturnValue(of(teachersMock));

    service.all().subscribe((sessions) => {
      expect(sessions).toEqual(teachersMock);
    });
  });

  it('should return an Observable<Teacher> for detail', () => {
    httpClientMock.get.mockReturnValue(of(teacherMock));

    service.detail('1').subscribe((teacher) => {
      expect(teacher).toEqual(teacherMock);
    });
  });

});