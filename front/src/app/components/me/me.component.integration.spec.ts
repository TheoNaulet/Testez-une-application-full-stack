import { expect } from '@jest/globals';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon'
import { Router } from '@angular/router';

import { MeComponent } from './me.component';
import { UserService } from '../../services/user.service';
import { SessionService } from '../../services/session.service';

describe('MeComponent Integration Test', () => {
    let component: MeComponent;
    let fixture: ComponentFixture<MeComponent>;
    let userService: UserService;
    let sessionService: SessionService;
    let matSnackBar: MatSnackBar;
    let router: Router;
    let httpMock: HttpTestingController;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [MeComponent],
            imports: [
                HttpClientTestingModule, 
                MatSnackBarModule,
                MatCardModule,
                MatIconModule
            ],
            providers: [
                UserService,
                {
                    provide: SessionService,
                    useValue: {
                        sessionInformation: { id: 1 },
                        logOut: jest.fn(),
                    },
                },
            ],
        }).compileComponents();
    });

    beforeEach(fakeAsync(() => {
        fixture = TestBed.createComponent(MeComponent);
        component = fixture.componentInstance;
        httpMock = TestBed.inject(HttpTestingController);
        userService = TestBed.inject(UserService);
        sessionService = TestBed.inject(SessionService);
        matSnackBar = TestBed.inject(MatSnackBar);
        router = TestBed.inject(Router);

        // Mock MatSnackBar.open to track its calls
        jest.spyOn(matSnackBar, 'open');

        fixture.detectChanges(); // Executes ngOnInit()
    }));

    it('should fetch user data from API and display it', fakeAsync(() => {
        // Intercepts the HTTP request
        const req = httpMock.expectOne(`api/user/${sessionService.sessionInformation!.id}`);
        expect(req.request.method).toBe('GET');

        // Responds with mock data
        req.flush({
            id: 1,
            firstName: 'John',
            lastName: 'Doe',
            email: 'john.doe@test.com',
            admin: true,
            createdAt: new Date(),
            updatedAt: new Date(),
        });

        tick(); // Simulates async processing

        // Verifies that the component assigns the data correctly
        expect(component.user).toEqual({
            id: 1,
            firstName: 'John',
            lastName: 'Doe',
            email: 'john.doe@test.com',
            admin: true,
            createdAt: expect.any(Date), 
            updatedAt: expect.any(Date),  
        });
    }));

    afterEach(() => {
        // Ensures no unexpected HTTP requests were made
        httpMock.verify();
    });
});
