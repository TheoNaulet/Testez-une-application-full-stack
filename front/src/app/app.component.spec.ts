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

  beforeEach(async () => {
    sessionServiceMock = {
      $isLogged: jest.fn().mockReturnValue(of(true)),
      logOut: jest.fn()
    };

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
      providers: [
        { provide: SessionService, useValue: sessionServiceMock }
      ]
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should display toolbar with correct title', () => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    const toolbar = compiled.querySelector('mat-toolbar span');
    expect(toolbar.textContent).toContain('Yoga app');
  });

  it('should show correct links when user is logged in', () => {
    sessionServiceMock.$isLogged.mockReturnValue(of(true));
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();

    const compiled = fixture.debugElement.nativeElement;
    const links = compiled.querySelectorAll('.link');
    expect(links.length).toBe(3);
    expect(links[0].textContent).toContain('Sessions');
    expect(links[1].textContent).toContain('Account');
    expect(links[2].textContent).toContain('Logout');
  });

  it('should show correct links when user is not logged in', () => {
    sessionServiceMock.$isLogged.mockReturnValue(of(false));
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();

    const compiled = fixture.debugElement.nativeElement;
    const links = compiled.querySelectorAll('.link');
    expect(links.length).toBe(2);
    expect(links[0].textContent).toContain('Login');
    expect(links[1].textContent).toContain('Register');
  });

  it('should call logOut and navigate to home on logout', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    const router = TestBed.inject(Router);
    const routerSpy = jest.spyOn(router, 'navigate');

    app.logout();

    expect(sessionServiceMock.logOut).toHaveBeenCalled();
    expect(routerSpy).toHaveBeenCalledWith(['']);
  });
});
