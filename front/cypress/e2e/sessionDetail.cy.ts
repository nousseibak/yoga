// Créer des utilisateurs fictifs pour les tests
let user2 = {
    id: '1',
    firstName: 'John',
    lastName: 'Doe',
    email: 'johndoe@example.com',
    admin: false,
    createdAt: new Date(),
    updatedAt: new Date()
};

let admin2 = {
    id: '2',
    firstName: 'John',
    lastName: 'Doe',
    email: 'johndoe@example.com',
    admin: true,
    createdAt: new Date(),
    updatedAt: new Date()
};

let teacher1 = {
    id: '1',
    firstName: 'John',
    lastName: 'Doe',
    email: 'johndoe@example.com',
    admin: false,
    createdAt: new Date(),
    updatedAt: new Date()
};

let session1 = {
    id: 1,
    name: 'session1',
    description: 'desc1',
    date: new Date(),
    teacher_id: 1,
    users: [],
    createdAt: new Date(),
    updatedAt: new Date()
}
let session2 = {
    id: 2,
    name: 'session2',
    description: 'desc2',
    date: new Date(),
    teacher_id: 2,
    users: [],
    createdAt: new Date(),
    updatedAt: new Date()
}

describe('SessionDetail', () => {
    beforeEach(() => {
        cy.visit('/login');

        cy.intercept(
            {
                method: 'GET',
                url: '/api/session',

            },
            [
                session1, session2

            ]).as('session')


    });

    it('Doit afficher les détails de la session', () => {
        cy.intercept('POST', '/api/auth/login', {
            body: user2
        })

        cy.intercept('GET', 'api/user/1', {
            body: user2
        }).as('user');


        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

        cy.url().should('include', '/sessions')

        cy.intercept('GET', 'api/session/1', {
            body: session1
        }).as('session1');

        cy.intercept('GET', 'api/teacher/1', {
            body: teacher1
        }).as('teacher1');


        cy.contains('.item', 'session1').within(() => {
            cy.contains('button', 'Detail').click();
        });

        // Vérifiez que vous êtes maintenant sur la page de détails de la session 1
        cy.url().should('include', '/sessions/detail/1');

        cy.contains('h1', 'Session1').should('exist');

        // Vérifier la présence de la description de la session
        cy.contains('.description', 'Description:').should('exist');


        // Vérifier la non-présence des boutons pour un utilisateur non administrateur
        cy.contains('button', 'Participate').should('exist');

    });

    it('Suppression pour un admin', () => {
        cy.intercept('POST', '/api/auth/login', {
            body: admin2
        })

        cy.intercept('GET', 'api/user/1', {
            body: admin2
        }).as('user');


        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

        cy.url().should('include', '/sessions')

        cy.intercept('GET', 'api/session/1', {
            body: session1
        }).as('session1');

        cy.intercept('GET', 'api/teacher/1', {
            body: teacher1
        }).as('teacher1');


        cy.contains('.item', 'session1').within(() => {
            cy.contains('button', 'Detail').click();
        });

        // Vérifiez que vous êtes maintenant sur la page de détails de la session 1
        cy.url().should('include', '/sessions/detail/1');

        // Vérifie la présence du bouton de suppression
        cy.get('button[mat-raised-button][color="warn"]').should('exist');



        cy.intercept('DELETE', 'api/session/1', {
            body: session1
        }).as('session1');
        // Clique sur le bouton de suppression
        cy.get('button[mat-raised-button][color="warn"]').click();

        // Vérifie le message de confirmation après la suppression
        cy.contains('Session deleted !').should('exist');

    });

    it('Doit permettre à un utilisateur de participer à une session', () => {
        cy.intercept('POST', '/api/auth/login', {
            body: user2
        })

        cy.intercept('GET', 'api/user/1', {
            body: user2
        }).as('user');


        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

        cy.url().should('include', '/sessions')

        cy.intercept('GET', 'api/session/1', {
            body: session1
        }).as('session1');

        cy.intercept('GET', 'api/teacher/1', {
            body: teacher1
        }).as('teacher1');


        cy.contains('.item', 'session1').within(() => {
            cy.contains('button', 'Detail').click();
        });

        // Vérifiez que vous êtes maintenant sur la page de détails de la session 1
        cy.url().should('include', '/sessions/detail/1');

        // Vérifie la présence du bouton de participation
        cy.get('button[mat-raised-button][color="primary"]').should('exist');

        cy.intercept('POST', '/api/session/1/participate/1', {
            statusCode: 200,
        }).as('participate1');

        cy.intercept('GET', 'api/session/1', {
            
                id: 1,
                name: 'session1',
                description: 'desc1',
                date: new Date(),
                teacher_id: 1,
                users: [1],
                createdAt: new Date(),
                updatedAt: new Date()
            
        }).as('session1');

        cy.intercept('GET', 'api/teacher/1', {
            body: teacher1
        }).as('teacher1');

        // Clique sur le bouton de participation
        cy.get('button[mat-raised-button][color="primary"]').click();

    });
});