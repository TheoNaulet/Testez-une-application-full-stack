import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { Router } from '@angular/router';
import { of } from 'rxjs';

import { AppComponent } from './app.component';
import { SessionService } from './services/session.service';

describe('AppComponent', () => {
  let sessionServiceMock: any;

  // Set up the test environment before each test
  beforeEach(async () => {
    // Create a mock SessionService with necessary methods
    sessionServiceMock = {
      $isLogged: jest.fn().mockReturnValue(of(true)), // Mocked observable for user login status
      logOut: jest.fn() // Mocked logOut function
    };

    // Configure the testing module with necessary dependencies
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule, // Provides router-related testing utilities
        HttpClientTestingModule, // Allows mocking HTTP requests
        MatToolbarModule // Material toolbar module for UI rendering
      ],
      declarations: [
        AppComponent // Declare the component being tested
      ],
      providers: [
        { provide: SessionService, useValue: sessionServiceMock } // Provide the mocked SessionService
      ]
    }).compileComponents();
  });

  // Test if the AppComponent is created successfully
  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  // Test if the toolbar displays the correct title
  it('should display toolbar with correct title', () => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges(); // Trigger change detection to update UI
    const compiled = fixture.debugElement.nativeElement;
    const toolbar = compiled.querySelector('mat-toolbar span');
    expect(toolbar.textContent).toContain('Yoga app'); // Verify that the title is displayed correctly
  });

  // Test if the correct navigation links are shown when the user is logged in
  it('should show correct links when user is logged in', () => {
    sessionServiceMock.$isLogged.mockReturnValue(of(true)); // Mock user as logged in
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();

    const compiled = fixture.debugElement.nativeElement;
    const links = compiled.querySelectorAll('.link');
    expect(links.length).toBe(3); // Expect three navigation links
    expect(links[0].textContent).toContain('Sessions'); // Verify the first link
    expect(links[1].textContent).toContain('Account'); // Verify the second link
    expect(links[2].textContent).toContain('Logout'); // Verify the third link
  });

  // Test if the correct navigation links are shown when the user is not logged in
  it('should show correct links when user is not logged in', () => {
    sessionServiceMock.$isLogged.mockReturnValue(of(false)); // Mock user as logged out
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();

    const compiled = fixture.debugElement.nativeElement;
    const links = compiled.querySelectorAll('.link');
    expect(links.length).toBe(2); // Expect two navigation links
    expect(links[0].textContent).toContain('Login'); // Verify the first link
    expect(links[1].textContent).toContain('Register'); // Verify the second link
  });

  // Test if logOut() is called and the user is redirected to home when logging out
  it('should call logOut and navigate to home on logout', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    const router = TestBed.inject(Router);
    const routerSpy = jest.spyOn(router, 'navigate'); // Spy on the router navigation method

    app.logout(); // Call the logout method

    expect(sessionServiceMock.logOut).toHaveBeenCalled(); // Verify that logOut() was called
    expect(routerSpy).toHaveBeenCalledWith(['']); // Verify that the user is redirected to the home page
  });
});
