import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { HttpTestingController } from '@angular/common/http/testing';
import { DebugElement } from '@angular/core';
import { By } from '@angular/platform-browser';
import { of } from 'rxjs';



describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let sessionService: SessionService;
  let router: Router;

  let emailInput : HTMLInputElement;
  let passwordInput : HTMLInputElement;
  let loginForm : DebugElement;
  let httpTestingController: HttpTestingController;
  let mockAuthService: any;
  let mockSessionService: any;
  let mockRouter: any;


  const mockSessionData = {
    token: 'randomTokenString',
    type: 'type',
    id: 101010,
    username: 'username',
    firstName: 'firstName',
    lastName: 'lastName',
    admin: false,
  };
  
  beforeEach(async () => {
    mockSessionService = { logIn: jest.fn() };
    mockAuthService = { login: jest.fn() };
    mockRouter = { navigate: jest.fn() };
    await TestBed.configureTestingModule({

      declarations: [LoginComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter },
        FormBuilder,
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
        
    })
      .compileComponents();
      fixture = TestBed.createComponent(LoginComponent);
      component = fixture.componentInstance;
      authService = TestBed.inject(AuthService);
      sessionService = TestBed.inject(SessionService);
      router = TestBed.inject(Router);
      fixture.detectChanges();
  
      emailInput = fixture.nativeElement.querySelector('#email');
      passwordInput = fixture.nativeElement.querySelector('#password');
      loginForm = fixture.debugElement.query(By.css('.login-form'));

  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });


//   it('should submit the login form with valid credentials', () => {
//     jest.spyOn(router, 'navigate').mockImplementation();

//     // Définir des valeurs de formulaire valides
//     const validFormData = {
//       email: 'yoga@studio.com',
//       password: 'test!1234'
//     };
  
//     // Simuler la soumission du formulaire
//     component.form.setValue(validFormData);

//     component.submit();

//     //expect(component.form.valid).toBeTruthy;
//     //expect(component.onError).toBe(false);
//     expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
// });

test('should call sessionService and redirect user on login success', () => {
  mockAuthService.login.mockReturnValue(of(mockSessionData));
  component.submit();
  expect(component.onError).toBe(false);
  expect(mockSessionService.logIn).toHaveBeenCalledWith(mockSessionData);
  expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
});

// it('should submit the login form with valid credentials', async() => {
// jest.spyOn(router, 'navigate').mockImplementation();
//     emailInput.value = 'test@test.com';
//     emailInput.dispatchEvent(new Event('input'));
//     passwordInput.value = 'test!123';
//     passwordInput.dispatchEvent(new Event('input'));
//     loginForm.triggerEventHandler('ngSubmit', null);
//     await fixture.whenStable();
//     fixture.detectChanges();
//     const req = httpTestingController.expectOne('api/auth/login');
//     req.flush({
//       token: 'token',
//       type: 'userSession',
//       id: 1,
//       username: 'username',
//       firstName: 'firstname',
//       lastName: 'lastname',
//       admin: false
//     });
//     expect(component.form.valid).toBeTruthy();
//     expect(sessionService.isLogged).toBeTruthy();
//     expect(sessionService.sessionInformation?.token).toBe('token');
//     expect(component.onError).toBeFalsy();
//     expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
//     // After every test, assert that there are no more pending requests.
//     httpTestingController.verify();
//   });
  
  
  // it('should display an error message when form fields are empty', () => {
  //   // Simulate form submission without filling required fields
  //   component.submit();

  //   // Expect onError to be true after form submission
  //   expect(component.onError).toBe(true);

  //   // Trigger change detection to update the view
  //   fixture.detectChanges();

  //   // Check if the error message is displayed in the DOM
  //   const errorMessage = fixture.nativeElement.querySelector('.error');
  //   expect(errorMessage.textContent).toContain('An error occurred');
  // });

  // it('should display an error message on invalid credentials', () => {
  //   const invalidFormData = {
  //     email: 'test@example.com',
  //     password: 'invalid'
  //   };
  
  //   // Créer un espion sur authService.login avec un retour d'informations de session simulées
  //   const sessionInfo: SessionInformation = {
  //     token: 'token_value',
  //     type: 'user',
  //     id: 1234,
  //     username: 'username',
  //     firstName: 'firstname',
  //     lastName: 'lastname',
  //     admin: true
  //   };

  //   component.form.setValue(invalidFormData);
  //   component.submit();

  //   expect(component.onError).toBe(true);

  // });
  
});
