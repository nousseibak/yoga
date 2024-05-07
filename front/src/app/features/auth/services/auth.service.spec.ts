import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { RegisterRequest } from '../interfaces/registerRequest.interface';

describe('AuthService', () => {
  let authService: AuthService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });

    authService = TestBed.inject(AuthService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify(); // Vérifiez qu'il n'y a pas de requêtes HTTP non résolues
  });

  it('should be created', () => {
    expect(authService).toBeTruthy();
  });

  it('should send login request', () => {
    const loginRequest: LoginRequest = { email: 'test@example.com', password: 'password123' };
    const mockSessionInfo: SessionInformation = {
      token: 'token_value',
      type: 'user',
      id: 1234,
      username: 'username',
      firstName: 'John',
      lastName: 'Doe',
      admin: false
    };

    authService.login(loginRequest).subscribe((sessionInfo) => {
      expect(sessionInfo).toEqual(mockSessionInfo); // Vérifiez que les données renvoyées correspondent aux attentes
    });

    const req = httpTestingController.expectOne('api/auth/login');
    expect(req.request.method).toEqual('POST');
    expect(req.request.body).toEqual(loginRequest);

    req.flush(mockSessionInfo); // Simulez la réponse HTTP avec les données de session simulées
  });

  it('should send register request', () => {
    const registerRequest: RegisterRequest = {
      email: 'test@example.com',
      password: 'password123',
      firstName: 'firstname',
      lastName: 'lastname'
    };

    authService.register(registerRequest).subscribe(() => {
    });

    const req = httpTestingController.expectOne('api/auth/register');
    expect(req.request.method).toEqual('POST');
    expect(req.request.body).toEqual(registerRequest);

    req.flush(null); // Simulez la réponse HTTP avec null (ou une réponse vide)
  });
});
