// Créer des utilisateurs fictifs pour les tests
let user1 = {
    id: '1',
    firstName: 'John',
    lastName: 'Doe',
    email: 'johndoe@example.com',
    admin: false,
    createdAt: new Date(),
    updatedAt: new Date()
};



describe('Logout', () => {
    beforeEach(() => {
        cy.visit('/login');

        cy.intercept(
            {
                method: 'GET',
                url: '/api/session',
            },
            []).as('session')
            
        cy.intercept('POST', '/api/auth/login', {
            body: user1
        })

        cy.intercept('GET', 'api/user/1', {
            body: user1
        }).as('user');
    });

    it('Se déconnecte quand on clique sur Lougout', () => {


        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
  
        cy.url().should('include', '/sessions')
        cy.get('span.link').contains('Logout').click();

        cy.url().should('include', '');

        cy.get('span.link').contains('Login').should('be.visible');
        cy.get('span.link').contains('Register').should('be.visible');

    });


});