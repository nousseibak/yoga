import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';

import { MeComponent } from './me.component';
import { UserService } from 'src/app/services/user.service';
import { of } from 'rxjs';
import { User } from 'src/app/interfaces/user.interface';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let mockSessionService: Partial<SessionService>;
  let mockUserService: Partial<UserService>;
  let mockSnackBar: Partial<MatSnackBar>;
  let router: Router;



  beforeEach(async () => {
    mockSessionService = {
      sessionInformation: {
        token: 'abcd1234',
        type: 'Bearer',
        id: 1,
        username: 'testuser',
        firstName: 'Test',
        lastName: 'User',
        admin: false
      },
      logOut: jest.fn()
    };
    mockUserService = {
      getById: jest.fn().mockReturnValue(of({  id:1, password:'test1234!',firstName: 'John', lastName: 'Doe', email: 'john.doe@example.com', admin: false, createdAt: new Date(), updatedAt: new Date() })),
      delete: jest.fn().mockReturnValue(of(null))
    };
    mockSnackBar = {
      open: jest.fn()
    };

    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        RouterTestingModule 
      ],
      providers: [{ provide: SessionService, useValue: mockSessionService },
      { provide: UserService, useValue: mockUserService },
      { provide: MatSnackBar, useValue: mockSnackBar }
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    router = TestBed.inject(Router);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display user information in the template', () => {
    // Simuler les données utilisateur
    const userData: User = {
      id: 1,
      password: 'test1234!',
      firstName: 'John',
      lastName: 'Doe',
      email: 'john.doe@example.com',
      admin: false,
      createdAt: new Date(),
      updatedAt: new Date()
    };
  
    // Assigner les données utilisateur au composant
    component.user = userData;
    fixture.detectChanges();
  
    // Sélectionner l'élément HTML contenant les informations de l'utilisateur
    const compiled = fixture.nativeElement;
    const userInfoParagraphs = compiled.querySelectorAll('p');
  
    // Vérifier le contenu des paragraphes pour les informations utilisateur
    expect(userInfoParagraphs.length).toBeGreaterThan(0); // Au moins un paragraphe devrait être présent
  
    // Vérifier chaque paragraphe pour les informations spécifiques de l'utilisateur
    let foundEmail = false;
    for (const paragraph of userInfoParagraphs) {
      const textContent = paragraph.textContent;
  
      if (textContent.includes('Name: John DOE')) {
        // Vérifier le nom complet de l'utilisateur
        expect(textContent).toContain('Name: John DOE');
      }
  
      if (textContent.includes('Email: john.doe@example.com')) {
        // Vérifier l'email de l'utilisateur
        expect(textContent).toContain('Email: john.doe@example.com');
        foundEmail = true;
      }
    }
  
    // Vérifier que l'email a été trouvé dans les paragraphes
    expect(foundEmail).toBeTruthy();
  });



  it('should delete user account and navigate to home', () => {
    const deleteSpy = jest.spyOn(component, 'delete');
    component.delete();
    expect(mockUserService.delete).toHaveBeenCalledWith('1');
    expect(mockSnackBar.open).toHaveBeenCalledWith('Your account has been deleted !', 'Close', { duration: 3000 });
    expect(mockSessionService.logOut).toHaveBeenCalled();
    const routerNavigateSpy = jest.spyOn(router, 'navigate');

    // Simuler la résolution de la navigation
    routerNavigateSpy.mockResolvedValue(true);
  
    // Appeler la méthode delete et vérifier la navigation
    component.delete();
    
    // Vérifier que la méthode navigate a été appelée avec ['/']
    expect(routerNavigateSpy).toHaveBeenCalledWith(['/']);

  });
  
});
