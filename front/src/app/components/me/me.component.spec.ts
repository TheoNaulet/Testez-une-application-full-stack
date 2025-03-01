import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { expect } from '@jest/globals';

import { MeComponent } from './me.component';
import { UserService } from '../../services/user.service';
import { of } from 'rxjs';
import { Router } from '@angular/router';
describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let userService: UserService;
  let snackBar: any;
  let router: any;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    },
    logOut: jest.fn()
  };

  beforeEach(async () => {
    const mockUserService = {
      getById: jest.fn().mockReturnValue(of({
        id: 1,
        email: 'john.doe@test.com',
        lastName: 'Doe',
        firstName: 'John',
        admin: true,
        password: 'password',
        createdAt: new Date(),
        updatedAt: new Date()
      })),
      delete: jest.fn().mockReturnValue(of({}))
    };

    const mockMatSnackBar = { open: jest.fn() };
    const mockRouter = { navigate: jest.fn() };

    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter }
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    userService = TestBed.inject(UserService);
    snackBar = TestBed.inject(MatSnackBar);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should delete the user, show a notification, log out, and redirect to home', () => {
    // Spy on the methods being used
    const deleteSpy = jest.spyOn(userService, 'delete').mockReturnValue(of({})); // Mock user deletion
    const snackBarSpy = jest.spyOn(snackBar, 'open'); // Mock notification display
    const logOutSpy = jest.spyOn(mockSessionService, 'logOut'); // Mock logout function
    const navigateSpy = jest.spyOn(router, 'navigate'); // Mock navigation function

    // Act: Call the delete() method
    component.delete();

    // Assert: Verify that the expected methods were called with correct arguments
    expect(deleteSpy).toHaveBeenCalledWith(mockSessionService.sessionInformation.id.toString()); // Ensure delete() is called with the user ID
    expect(snackBarSpy).toHaveBeenCalledWith('Your account has been deleted !', 'Close', { duration: 3000 }); // Check if the correct notification is displayed
    expect(logOutSpy).toHaveBeenCalled(); // Ensure logOut() was executed
    expect(navigateSpy).toHaveBeenCalledWith(['/']); // Verify redirection to the home page
  });

});
