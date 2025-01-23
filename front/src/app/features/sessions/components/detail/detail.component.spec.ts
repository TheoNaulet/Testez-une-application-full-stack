import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';
import { DetailComponent } from './detail.component';
import { ActivatedRoute, Router } from '@angular/router';
import { TeacherService } from 'src/app/services/teacher.service';
import { SessionApiService } from '../../services/session-api.service';
import { of, throwError } from 'rxjs';
import { Session } from '../../interfaces/session.interface';
import { Teacher } from '../../../../interfaces/teacher.interface';

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let service: SessionService;

  // Mocks for constructor parameters
  let mockRoute: any = { snapshot: { paramMap: { get: jest.fn(() => '1') } } };
  let mockFormBuilder: FormBuilder = new FormBuilder();
  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1,
    },
  };
  let mockSessionApiService: any = {
    delete: jest.fn().mockReturnValue(of({})),
    participate: jest.fn().mockReturnValue(of({})),
    unParticipate: jest.fn().mockReturnValue(of({})),
    detail: jest.fn().mockReturnValue(of({})),
  };
  let mockTeacherService: any = {
    detail: jest.fn().mockReturnValue(of({})),
  };
  let mockMatSnackBar: any = {
    open: jest.fn(),
  };
  let mockRouter: any = {
    navigate: jest.fn(),
  };

  // Mock session data
  const mockSession: Session = {
    id: 1,
    name: 'Session 1',
    description: 'Description',
    date: new Date(),
    createdAt: new Date(),
    updatedAt: new Date(),
    teacher_id: 1,
    users: [1, 2, 3], // Ensure `users` is defined
  };

  const mockTeacher: Teacher = {
    id: 1,
    firstName: 'John',
    lastName: 'Doe',
    createdAt:  new Date(),
    updatedAt:  new Date(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: ActivatedRoute, useValue: mockRoute },
        { provide: FormBuilder, useValue: mockFormBuilder },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter },
      ],
    }).compileComponents();

    service = TestBed.inject(SessionService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;

    // Mock the session and teacher data
    mockSessionApiService.detail.mockReturnValue(of(mockSession));
    mockTeacherService.detail.mockReturnValue(of(mockTeacher));

    // Initialize the component
    component.ngOnInit();
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('delete', () => {
    it('should delete a session and show a success message', () => {
      component.delete();
      expect(mockSessionApiService.delete).toHaveBeenCalledWith(component.sessionId);
      expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
      expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
    });

    it('should show an error message when delete fails', () => {
      jest.spyOn(mockSessionApiService, 'delete').mockReturnValue(throwError(() => new Error('Delete failed')));
      component.delete();
      expect(mockSessionApiService.delete).toHaveBeenCalledWith(component.sessionId);
      expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
    });
  });

  describe('participate', () => {
    it('should call participate and fetchSession', () => {
      const fetchSessionSpy = jest.spyOn(component as any, 'fetchSession');
      component.participate();
      expect(mockSessionApiService.participate).toHaveBeenCalledWith(component.sessionId, component.userId);
      expect(fetchSessionSpy).toHaveBeenCalled();
    });
  });

  describe('unParticipate', () => {
    it('should call unParticipate and fetchSession', () => {
      const fetchSessionSpy = jest.spyOn(component as any, 'fetchSession');
      component.unParticipate();
      expect(mockSessionApiService.unParticipate).toHaveBeenCalledWith(component.sessionId, component.userId);
      expect(fetchSessionSpy).toHaveBeenCalled();
    });
  });

  describe('when user is not admin', () => {
    beforeEach(() => {
      mockSessionService.sessionInformation.admin = false;
      fixture.detectChanges();
    });

    it('should not show delete button', () => {
      const deleteButton = fixture.nativeElement.querySelector('button[color="warn"]');
      expect(deleteButton).toBeNull();
    });
  });

  describe('when user is not participating', () => {
    beforeEach(() => {
      component.isParticipate = false;
      fixture.detectChanges();
    });

    it('should show participate button', () => {
      const participateButton = fixture.nativeElement.querySelector('button[color="primary"]');
      expect(participateButton).toBeTruthy();
    });
  });

  describe('when user is participating', () => {
    beforeEach(() => {
      component.isParticipate = true;
      fixture.detectChanges();
    });

    it('should show unParticipate button', () => {
      const unParticipateButton = fixture.nativeElement.querySelector('button[color="warn"]');
      expect(unParticipateButton).toBeTruthy();
    });
  });
});