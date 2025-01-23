import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { expect } from '@jest/globals';

import { MeComponent } from './me.component';
import { UserService } from '../../services/user.service';
import { of } from 'rxjs';
import { User } from '../../interfaces/user.interface';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    },
    logOut: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [{ provide: SessionService, useValue: mockSessionService }],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    // Verifies that the component is successfully created
    expect(component).toBeTruthy();
  });

  it('should call history.back when back method is executed', () => {
    // Spy on the history.back method of the window object
    const spy = jest.spyOn(window.history, 'back');
    // Call the back method of the component
    component.back();
    // Expect the history.back method to have been called
    expect(spy).toHaveBeenCalled();
  });

  it('should call userService.getById with the correct ID on ngOnInit and set the user property with the returned User object', () => {
    // Inject the UserService
    const userService = TestBed.inject(UserService);
    // Create a mock user object
    const mockUser: User = {
      id: 1,
      email: 'john.doe@test.com',
      lastName: 'Doe',
      firstName: 'John',
      admin: true,
      password: 'password',
      createdAt: new Date(),
      updatedAt: new Date()
    };
    // Spy on the getById method of UserService and return an Observable with the mock user
    const spy = jest.spyOn(userService, 'getById').mockReturnValue(of(mockUser));

    // Call ngOnInit method
    component.ngOnInit();

    // Expect getById to have been called with the session ID
    expect(spy).toHaveBeenCalledWith(mockSessionService.sessionInformation.id.toString());
    // Expect the user property of the component to match the mock user
    expect(component.user).toEqual(mockUser);
  });

});
