import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let sessionService: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    sessionService = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(sessionService).toBeTruthy();
  });

  it('should initialize with isLogged as false', () => {
    expect(sessionService.isLogged).toBe(false);
  });

  it('should log in a user', () => {
    const user: SessionInformation = {
      token: 'abcd1234',
      type: 'Bearer',
      id: 1,
      username: 'testuser',
      firstName: 'Test',
      lastName: 'User',
      admin: false
    };
    sessionService.logIn(user);

    expect(sessionService.isLogged).toBe(true);
    expect(sessionService.sessionInformation).toEqual(user);
  });

  it('should log out a user', () => {
    const user: SessionInformation = {
      token: 'abcd1234',
      type: 'Bearer',
      id: 1,
      username: 'testuser',
      firstName: 'Test',
      lastName: 'User',
      admin: false
    };
    sessionService.logIn(user);
    sessionService.logOut();

    expect(sessionService.isLogged).toBe(false);
    expect(sessionService.sessionInformation).toBeUndefined();
  });

  it('should emit true after login', (done) => {
    const user: SessionInformation = {
      token: 'abcd1234',
      type: 'Bearer',
      id: 1,
      username: 'testuser',
      firstName: 'Test',
      lastName: 'User',
      admin: false
    };
    sessionService.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(true);
      done();
    });
    sessionService.logIn(user);
  });

  it('should emit false after logout', (done) => {
    const user: SessionInformation = {
      token: 'abcd1234',
      type: 'Bearer',
      id: 1,
      username: 'testuser',
      firstName: 'Test',
      lastName: 'User',
      admin: false
    };
    sessionService.logIn(user);
    sessionService.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(false);
      done();
    });
    sessionService.logOut();
  });
});
