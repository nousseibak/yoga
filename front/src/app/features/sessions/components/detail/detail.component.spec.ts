import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { SessionService } from '../../../../services/session.service';
import { DetailComponent } from './detail.component';
import { Router } from '@angular/router';
import { SessionApiService } from '../../services/session-api.service';
import { Session } from '../../interfaces/session.interface';

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let sessionApiService: SessionApiService;
  let sessionService: SessionService;
  let router: Router;
  let mockSessionId = '123';


  const mockSession = {

    id: '123',
    users: [1, 2, 3]
  };

  let mockSessionApiService = {
    sessionInformation: {
      admin: true,
      id: 1
    },
    detail: jest.fn().mockReturnValue(of(mockSession)),
    delete: jest.fn().mockReturnValue(of({})),
  };

  let mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    },
    logOut: jest.fn()
  };

  const mockSnackBar = {
    open: jest.fn()
  };
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        RouterTestingModule
      ],
      declarations: [DetailComponent],
      providers: [{ provide: SessionService, useValue: mockSessionService },
      { provide: SessionApiService, useValue: mockSessionApiService },
      { provide: MatSnackBar, useValue: mockSnackBar }
      ],
    }).compileComponents();

    sessionApiService = TestBed.inject(SessionApiService);
    sessionService = TestBed.inject(SessionService);

    router = TestBed.inject(Router);
    let matSnackBarMock = TestBed.inject(MatSnackBar);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with correct session information', () => {
    component.ngOnInit();

    // After ngOnInit, session and teacher should be initialized
    expect(component.session).toEqual(mockSession);
    expect(component.isParticipate).toBe(true); // Assuming user ID 1 is in the list of users
    // Add expectations for teacher initialization if needed
  });

  it('should delete the session and navigate after deletion', () => {

    component.sessionId = mockSessionId;
    let routerNavigateSpy = jest.spyOn(router, 'navigate');

    // Simuler la résolution de la navigation
    routerNavigateSpy.mockResolvedValue(true);
    component.delete();


    // Simulate session deletion and verify effects
    fixture.detectChanges(); // Trigger change detection after async operation

    // Verify that matSnackBar.open and router.navigate are called appropriately
    expect(mockSnackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });



    // Vérifier que la méthode navigate a été appelée avec ['/']
    expect(routerNavigateSpy).toHaveBeenCalledWith(['sessions']);
  });

  // Add more test cases for other component methods as needed
});
