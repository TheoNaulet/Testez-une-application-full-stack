import {HttpClient, HttpClientModule} from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import {Session} from "../interfaces/session.interface";
import {of} from "rxjs";

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpClientMock: any;
  let sessionMock: Session;

  beforeEach(() => {
    httpClientMock = {
      get: jest.fn(),
      delete: jest.fn(),
      post: jest.fn(),
      put: jest.fn(),
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
    service = TestBed.inject(SessionApiService);

    sessionMock = { id: 1, name: 'Test Session 1', description: 'Test Description 1', date: new Date(), teacher_id: 1, users: [1, 2] };

  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // On simule des appels HTTP qui sont censé retournés des observables (soit de type Session[], Session, any ou void)

  it('should return an Observable<Session[]> for all', () => {
    const sessionsMock: Session[] = [sessionMock];
    httpClientMock.get.mockReturnValue(of(sessionsMock));

    service.all().subscribe((sessions) => {
      expect(sessions).toEqual(sessionsMock);
    });
  });

  it('should return an Observable<Session> for detail', () => {
    httpClientMock.get.mockReturnValue(of(sessionMock));

    service.detail('1').subscribe((session) => {
      expect(session).toEqual(sessionMock);
    });
  });

  it('should return an Observable<any> for delete', () => {
    httpClientMock.delete.mockReturnValue(of(null));

    service.delete('1').subscribe((response) => {
      expect(response).toBeDefined();
    });
  });

  it('should return an Observable<Session> for create', () => {
    httpClientMock.post.mockReturnValue(of(sessionMock));

    service.create(sessionMock).subscribe((session) => {
      expect(session).toEqual(sessionMock);
    });
  });

  it('should return an Observable<Session> for update', () => {
    httpClientMock.put.mockReturnValue(of(sessionMock));

    service.update('1', sessionMock).subscribe((session) => {
      expect(session).toEqual(sessionMock);
    });
  });

  it('should return an Observable<void> for participate', () => {
    httpClientMock.post.mockReturnValue(of(null));

    service.participate('1', '1').subscribe((response) => {
      expect(response).toBeDefined();
    });
  });

  it('should return an Observable<void> for unParticipate', () => {
    httpClientMock.delete.mockReturnValue(of(null));

    service.unParticipate('1', '1').subscribe((response) => {
      expect(response).toBeDefined();
    });
  });

});