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

  describe('When initializing the form in creation mode', () => {
    it('should initialize the form with default values', () => {
      // Arrange
      mockRouter.url = '/sessions/create'; 
      fixture.detectChanges();
  
      // Act
      component.ngOnInit();
  
      // Assert
      expect(component.sessionForm).toBeDefined();
      expect(component.sessionForm?.value).toEqual({
        name: '',
        date: '',
        teacher_id: '',
        description: '',
      });
    });
  });


  describe('When submitting the form in update mode', () => {
    it('should call sessionApiService.update with the correct data', () => {
      // Arrange
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

      // Simuler l'ID de session et l'URL de mise à jour
      mockRoute.snapshot.paramMap.get.mockReturnValue(sessionId);
      mockRouter.url = '/sessions/update';

      // Simuler la réponse de sessionApiService.detail()
      const mockSessionApiService = TestBed.inject(SessionApiService);
      jest.spyOn(mockSessionApiService, 'detail').mockReturnValue(of(sessionReq));

      // Déclencher ngOnInit
      fixture.detectChanges();

      // Vider les requêtes existantes avant d'exécuter le test
      httpTestingController.match(() => true).forEach(req => req.flush({}));

      component.onUpdate = true; 

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


    describe('When the user is not an admin', () => {
      it('should redirect to /sessions', () => {
        mockSessionService.sessionInformation.admin = false;
        component.ngOnInit();
        expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
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