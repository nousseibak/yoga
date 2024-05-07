import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RegisterComponent } from './register.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { RouterTestingModule } from '@angular/router/testing';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        RouterTestingModule  
      ],
      providers: [
        { provide: AuthService, useValue: { register: jest.fn() } }, // Mock AuthService with register method
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should successfully register with valid form data', () => {
    const registerSpy = jest.spyOn(authService, 'register').mockReturnValue(of(undefined));
    const navigateSpy = jest.spyOn(router, 'navigate').mockResolvedValue(true);

    const validFormData = {
      firstName: 'John',
      lastName: 'Doe',
      email: 'john.doe@example.com',
      password: 'password123'
    };

    // Set form values
    component.form.setValue(validFormData);

    // Simulate form submission
    component.submit();

    expect(component.onError).toBe(false); // No error should occur
    expect(registerSpy).toHaveBeenCalledWith(validFormData);
    expect(navigateSpy).toHaveBeenCalledWith(['/login']); // Use array for router navigate argument
    

  });

  it('should handle registration error', () => {
    const error = new Error('Registration failed');
    const registerSpy = jest.spyOn(authService, 'register').mockReturnValue(throwError(error));


    const validFormData = {
      firstName: 'John',
      lastName: 'Doe',
      email: 'john.doe@example.com',
      password: 'password123'
    };

    // Set form values
    component.form.setValue(validFormData);

    // Simulate form submission
    component.submit();

    expect(component.onError).toBe(true); // Error should occur
    expect(registerSpy).toHaveBeenCalledWith(validFormData);
  });


  // it('should not submit with invalid form data', () => {
  //   const registerSpy = jest.spyOn(authService, 'register').mockReturnValue(of(undefined));

  //   // Set invalid form values (missing required fields)
  //   component.form.setValue({
  //     firstName: '', // Invalid: required field
  //     lastName: 'Doe',
  //     email: 'john.doe@example.com',
  //     password: 'password123'
  //   });

  //   expect(component.form.valid).toBeFalsy();


  //  // Simulate form submission
  //  component.submit();

  //   expect(registerSpy).not.toHaveBeenCalled(); // Register method should not be called

  //   expect(component.onError).toBe(true);
  // });
});
