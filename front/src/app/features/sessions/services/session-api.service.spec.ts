import { HttpTestingController, HttpClientTestingModule } from "@angular/common/http/testing";
import { TestBed } from "@angular/core/testing";
import { Session } from "../interfaces/session.interface";
import { SessionApiService } from "./session-api.service";

describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionApiService]
    });

    // Inject the service and the http backend
    service = TestBed.inject(SessionApiService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    // After every test, verify that there are no outstanding HTTP requests
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve all sessions', () => {
    const dummySessions: Session[] = [
      { id: 1, name: 'Session 1', date: new Date(), description:'',teacher_id:1,users:[] },
      { id: 2, name: 'Session 2', date: new Date(), description:'',teacher_id:1,users:[] }
    ];

    service.all().subscribe((sessions) => {
      expect(sessions).toEqual(dummySessions);
    });

    const req = httpTestingController.expectOne('api/session');
    expect(req.request.method).toEqual('GET');

    req.flush(dummySessions);
  });

  it('should retrieve session details', () => {
    const sessionId = 1;
    const dummySession: Session = { id: sessionId, name: 'Session 1', date: new Date(), description:'',teacher_id:1,users:[]  };

    service.detail(sessionId.toString()).subscribe((session) => {
      expect(session).toEqual(dummySession);
    });

    const req = httpTestingController.expectOne(`api/session/${sessionId}`);
    expect(req.request.method).toEqual('GET');

    req.flush(dummySession);
  });

  it('should create a new session', () => {
    const newSession: Session = {
      id: 1,
      name: 'New Session',
      date: new Date(),
      description: 'New session description',
      teacher_id: 1,
      users: []
    };
  
    service.create(newSession).subscribe((createdSession) => {
      expect(createdSession).toEqual(newSession);
    });
  
    const req = httpTestingController.expectOne(`api/session`);
    expect(req.request.method).toEqual('POST');
    expect(req.request.body).toEqual(newSession);
  
    req.flush(newSession); // Simulate successful creation
  });

  it('should update an existing session', () => {
    const sessionId =1;
    const updatedSession: Session = {
      id: sessionId,
      name: 'Updated Session',
      date: new Date(),
      description: 'Updated session description',
      teacher_id: 1,
      users: []
    };
  
    service.update(sessionId.toString(), updatedSession).subscribe((updated) => {
      expect(updated).toEqual(updatedSession);
    });
  
    const req = httpTestingController.expectOne(`api/session/${sessionId}`);
    expect(req.request.method).toEqual('PUT');
    expect(req.request.body).toEqual(updatedSession);
  
    req.flush(updatedSession); // Simulate successful update
  });
  
  it('should add a user to session participation', () => {
    const sessionId = '1';
    const userId = '123';
  
    service.participate(sessionId, userId).subscribe(() => {
      // If the request is successful, there is no need to assert anything specific
      // You can optionally check other behaviors here based on your application logic
    });
  
    const req = httpTestingController.expectOne(`api/session/${sessionId}/participate/${userId}`);
    expect(req.request.method).toEqual('POST');
  
    req.flush(null); // Simulate successful participation
  });
  
  it('should remove a user from session participation', () => {
    const sessionId = '1';
    const userId = '123';
  
    service.unParticipate(sessionId, userId).subscribe(() => {
    });
  
    const req = httpTestingController.expectOne(`api/session/${sessionId}/participate/${userId}`);
    expect(req.request.method).toEqual('DELETE');
  
    req.flush(null); // Simulate successful un-participation
  });
  
});
