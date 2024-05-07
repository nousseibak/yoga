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

describe('SessionList', () => {
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

    it('Affichage de la liste des sessions apres connexion pour un utilisateur', () => {
        cy.intercept('POST', '/api/auth/login', {
            body: user2
        })

        cy.intercept('GET', 'api/user/1', {
            body: user2
        }).as('user');


        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

        cy.url().should('include', '/sessions')

        // Vérifiez que la liste des sessions est affichée correctement dans le composant ListComponent
        cy.get('.list mat-card').should('exist'); // Vérifie la présence du conteneur des sessions
        cy.get('.item').should('have.length', 2); // Vérifie qu'il y a deux sessions affichées

        // Vérifiez que le contenu des sessions est correctement rendu
        cy.contains('.item', session1.name).should('exist');
        cy.contains('.item', session1.description).should('exist');
        cy.contains('.item', session2.name).should('exist');
        cy.contains('.item', session2.description).should('exist');

        // Vérifier la visibilité du bouton "Create" pour l'utilisateur non administrateur
        cy.get('button[routerLink="create"]').should('not.exist');

    });

    it('Affichage du bouton "Create" pour un utilisateur administrateur', () => {
        // Intercepter la requête de connexion et renvoyer l'utilisateur administrateur
        cy.intercept('POST', '/api/auth/login', {
            body: admin2
        });

        // Intercepter la requête pour obtenir les informations de l'utilisateur administrateur
        cy.intercept('GET', 'api/user/2', {
            body: admin2
        }).as('adminUser');

        cy.get('input[formControlName=email]').type("admin@studio.com");
        cy.get('input[formControlName=password]').type(`${"admin!1234"}{enter}{enter}`);

        cy.url().should('include', '/sessions');

        // Vérifier que le bouton "Create" est visible pour l'utilisateur administrateur
        cy.get('button[routerLink="create"]').should('exist');
    });


    it('Affiche le bouton "Detail" pour chaque session', () => {
        // Intercepter la requête de connexion et renvoyer l'utilisateur administrateur
        cy.intercept('POST', '/api/auth/login', {
            body: admin2
        });

        // Intercepter la requête pour obtenir les informations de l'utilisateur administrateur
        cy.intercept('GET', '/api/user/2', {
            body: admin2
        }).as('adminUser');

        // Remplir le formulaire de connexion avec l'utilisateur administrateur
        cy.get('input[formControlName=email]').type("admin@studio.com");
        cy.get('input[formControlName=password]').type(`${"admin!1234"}{enter}{enter}`);

        // Vérifier que l'URL contient '/sessions' après connexion
        cy.url().should('include', '/sessions');

        // Vérifier la présence du bouton "Detail" pour chaque session
        cy.contains('.item', session1.name).within(() => {
            cy.contains('button', 'Detail').should('exist');
        });

        cy.contains('.item', session2.name).within(() => {
            cy.contains('button', 'Detail').should('exist');
        });
    });


    it('Affiche le bouton "Edit" pour chaque session pour un admin', () => {
        // Intercepter la requête de connexion et renvoyer l'utilisateur administrateur
        cy.intercept('POST', '/api/auth/login', {
            body: admin2
        });

        // Intercepter la requête pour obtenir les informations de l'utilisateur administrateur
        cy.intercept('GET', '/api/user/2', {
            body: admin2
        }).as('adminUser');

        // Remplir le formulaire de connexion avec l'utilisateur administrateur
        cy.get('input[formControlName=email]').type("admin@studio.com");
        cy.get('input[formControlName=password]').type(`${"admin!1234"}{enter}{enter}`);

        // Vérifier que l'URL contient '/sessions' après connexion
        cy.url().should('include', '/sessions');

        // Vérifier la présence du bouton "Detail" pour chaque session
        cy.contains('.item', session1.name).within(() => {
            cy.contains('button', 'Edit').should('exist');
        });

        cy.contains('.item', session2.name).within(() => {
            cy.contains('button', 'Edit').should('exist');
        });
    });

});