import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { FormComponent } from './form.component';
import { ActivatedRoute, Router } from '@angular/router';
import { TeacherService } from 'src/app/services/teacher.service';
import { Session } from '../../interfaces/session.interface';
import { of } from 'rxjs';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let httpTestingController: HttpTestingController;

  const mockRoute = { 
    snapshot: { 
      paramMap: { 
        get: jest.fn().mockReturnValue('1') 
      } 
    } 
  };
  
  const mockMatSnackBar = { 
    open: jest.fn() 
  };
  
  const mockRouter = {
    url: '/sessions/create',
    navigate: jest.fn(),
  };
  
  const mockSessionService = {
    sessionInformation: {
      admin: true,
    },
    detail: jest.fn(),
    create: jest.fn(),
    update: jest.fn(),
  };

  const mockTeacherService = { 
    all: jest.fn().mockReturnValue(of([])) 
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule,
      ],
      providers: [
        { provide: ActivatedRoute, useValue: mockRoute },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter },
        { provide: SessionService, useValue: mockSessionService },
        { provide: TeacherService, useValue: mockTeacherService },
        FormBuilder,
      ],
      declarations: [FormComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    jest.clearAllMocks();
    const requests = httpTestingController.match(() => true);
    requests.forEach(req => req.flush({}));
    httpTestingController.verify();
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  describe('When I fill fields correctly in session creation form', () => {
    it('should send a POST request to create a session', () => {
      // Arrange
      fixture.detectChanges();
      const sessionReq: Session = {
        id: 1,
        name: 'New Session',
        description: 'Description',
        date: new Date('2025-01-16'),
        teacher_id: 1,
        users: [],
        createdAt: new Date(),
        updatedAt: new Date(),
      };

      component.sessionForm?.setValue({
        name: sessionReq.name,
        date: sessionReq.date.toISOString().split('T')[0],
        teacher_id: sessionReq.teacher_id,
        description: sessionReq.description,
      });

      component.onUpdate = false;

      // Act
      component.submit();

      // Assert
      const req = httpTestingController.expectOne('api/session');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual({
        name: sessionReq.name,
        date: sessionReq.date.toISOString().split('T')[0],
        teacher_id: sessionReq.teacher_id,
        description: sessionReq.description,
      });

      req.flush(sessionReq);
      expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 });
      expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
    });
  });
  
  describe('When I fill fields correctly in session update form', () => {
    it('should send a PUT request to update a session', () => {
      // Arrange
      fixture.detectChanges();
      const sessionId = '1';
      const sessionReq: Session = {
        id: 1,
        name: 'Updated Session',
        description: 'Updated Description',
        date: new Date('2025-01-16'),
        teacher_id: 2,
        users: [],
        createdAt: new Date(),
        updatedAt: new Date(),
      };

      component.onUpdate = true;
      mockRoute.snapshot.paramMap.get.mockReturnValue(sessionId);

      component.sessionForm?.setValue({
        name: sessionReq.name,
        date: sessionReq.date.toISOString().split('T')[0],
        teacher_id: sessionReq.teacher_id,
        description: sessionReq.description,
      });

      // Act
      component.submit();

      // Assert
      const req = httpTestingController.expectOne(`api/session/${sessionId}`);
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual({
        name: sessionReq.name,
        date: sessionReq.date.toISOString().split('T')[0],
        teacher_id: sessionReq.teacher_id,
        description: sessionReq.description,
      });

      req.flush(sessionReq);
      expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session updated !', 'Close', { duration: 3000 });
      expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
    });
  });

  describe('When I fill fields incorrectly', () => {
    it('should not send a request if form is invalid', () => {
      // Arrange
      fixture.detectChanges();
      component.sessionForm?.setValue({
        name: '',
        date: '',
        teacher_id: '',
        description: '',
      });

      component.onUpdate = false;

      // Act
      component.submit();

      // Assert
      httpTestingController.expectNone('api/session');
      expect(mockMatSnackBar.open).not.toHaveBeenCalled();
      expect(mockRouter.navigate).not.toHaveBeenCalled();
    });
  });

  describe('When initializing the form in update mode', () => {
    it('should load session details and initialize form', () => {
      const sessionId = '1';
      const existingSession: Session = {
        id: 1,
        name: 'Existing Session',
        description: 'Existing Description',
        date: new Date('2025-01-16'),
        teacher_id: 1,
        users: [],
        createdAt: new Date(),
        updatedAt: new Date()
      };

      mockRouter.url = '/sessions/update';
      mockRoute.snapshot.paramMap.get.mockReturnValue(sessionId);
      mockSessionService.detail.mockReturnValue(of(existingSession));

      component.ngOnInit();
      const req = httpTestingController.expectOne(`api/session/${sessionId}`);
      expect(req.request.method).toBe('GET');

      req.flush(existingSession);

      expect(component.sessionForm?.value).toEqual({
        name: existingSession.name,
        date: existingSession.date.toISOString().split('T')[0],
        teacher_id: existingSession.teacher_id,
        description: existingSession.description
      });
    });

    describe('When the user is not an admin', () => {
      it('should redirect to /sessions', () => {
        mockSessionService.sessionInformation.admin = false;
        component.ngOnInit();
        expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
      });
    });
    
    describe('When the URL contains "update"', () => {
      it('should load session details and initialize the form', () => {
        const sessionId = '1';
        const mockSession: Session = {
          id: 1,
          name: 'Existing Session',
          description: 'Description of the session',
          date: new Date('2025-01-16'),
          teacher_id: 1,
          users: [],
          createdAt: new Date(),
          updatedAt: new Date(),
        };

        mockRouter.url = '/sessions/update';
        mockRoute.snapshot.paramMap.get.mockReturnValue(sessionId);
        mockSessionService.detail.mockReturnValue(of(mockSession));

        component.ngOnInit();
        
        expect(mockSessionService.detail).toHaveBeenCalledWith(sessionId);
        expect(component.sessionForm?.value).toEqual({
          name: mockSession.name,
          date: mockSession.date.toISOString().split('T')[0],
          teacher_id: mockSession.teacher_id,
          description: mockSession.description,
        });
      });
    });
    
    describe('When "exitPage" is called', () => {
      it('should display a notification and navigate to /sessions', () => {
        const message = 'Session created!';
        component['exitPage'](message);
        expect(mockMatSnackBar.open).toHaveBeenCalledWith(message, 'Close', { duration: 3000 });
        expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
      });
    });
  });
});