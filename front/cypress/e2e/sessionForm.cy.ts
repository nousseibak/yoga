describe('FormComponent', () => {
    let teacher1 = {
        id: '1',
        firstName: 'John',
        lastName: 'Doe',
        createdAt: new Date(),
        updatedAt: new Date()
    };
  
    let teachers = [
        { id: '1', firstName: 'John', lastName: 'Doe' },
        { id: '2', firstName: 'Jane', lastName: 'Smith' }
    ];
  
    const newSession = {
        id: '3',
        name: 'New Session',
        description: 'Description of the new session',
        date: new Date(),
        teacher_id: '1',
        users: [],
        createdAt: new Date(),
        updatedAt: new Date()
    };


    beforeEach(() => {
        cy.visit('/login');

        // Intercepter la requête de connexion et renvoyer un utilisateur administrateur
        cy.intercept('POST', '/api/auth/login', {
            body: {
                id: '2',
                firstName: 'John',
                lastName: 'Doe',
                email: 'admin@studio.com',
                admin: true,
                createdAt: new Date(),
                updatedAt: new Date()
            }
        });

        // Intercepter la requête pour obtenir les informations de l'utilisateur administrateur
        cy.intercept('GET', '/api/user/2', {
            body: {
                id: '2',
                firstName: 'John',
                lastName: 'Doe',
                email: 'admin@studio.com',
                admin: true,
                createdAt: new Date(),
                updatedAt: new Date()
            }
        }).as('adminUser');

        // Remplir le formulaire de connexion avec l'utilisateur administrateur
        cy.get('input[formControlName=email]').type("admin@studio.com");
        cy.get('input[formControlName=password]').type(`${"admin!1234"}{enter}{enter}`);

        // Vérifier que l'URL contient '/sessions' après connexion
        cy.url().should('include', '/sessions');
    });

    it('Création d\'une nouvelle session', () => {

        cy.intercept('GET', 'api/teacher', teachers).as('getTeachers');
        // Cliquer sur le bouton "Create"
        cy.contains('button', 'Create').click();

        // Vérifier que le formulaire de création est affiché
        cy.get('h1').should('contain', 'Create session');

        // Remplir le formulaire
        cy.get('input[formControlName=name]').type('New Session');
        cy.get('input[formControlName=date]').type('2024-04-20');
        cy.get('mat-select[formControlName=teacher_id]').click();
        cy.get('.mat-option').contains('John Doe').click();
        cy.get('textarea[formControlName=description]').type('This is a new session.');

        cy.intercept('POST', 'api/session', newSession).as('createSession');

   

        // Cliquer sur le bouton "Save" pour soumettre le formulaire
        cy.get('button[type=submit]').click();

        // Vérifier que la session a été créée avec succès
        cy.contains('.mat-snack-bar-container', 'Session created !').should('exist');

        // Vérifier que l'utilisateur est redirigé vers la liste des sessions
        cy.url().should('include', '/sessions');
    });

    it('Affichage d\'une erreur en l\'absence d\'un champ obligatoire', () => {
        cy.intercept('GET', 'api/teacher', teachers).as('getTeachers');

        // Cliquer sur le bouton "Create"
        cy.contains('button', 'Create').click();

        // Vérifier que le formulaire de création est affiché
        cy.get('h1').should('contain', 'Create session');

        // Cliquer sur le bouton "Save" pour soumettre le formulaire sans remplir les champs obligatoires
        cy.get('button[type="submit"]').should('be.disabled'); 

    });
});
