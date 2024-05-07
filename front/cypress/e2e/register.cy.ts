import { faker } from '@faker-js/faker';

describe('Register Component', () => {

  it('Affiche une erreur en l’absence d’un champ obligatoire', () => {
    cy.visit('/register');
    cy.get('input[formControlName=firstName]').clear();

    cy.contains('Last name *');

    cy.get('button[type="submit"]').should('be.disabled');
  });

it('Soumet avec succès le formulaire d\'enregistrement', () => {
    cy.visit('/register');

    cy.intercept('POST', '/api/auth/register', {
      statusCode: 200, 
      body: {
        message: 'User registered successfully!'
      }
    });

    cy.get('input[formControlName=firstName]').type(faker.person.firstName());
    cy.get('input[formControlName=lastName]').type(faker.person.lastName());
    cy.get('input[formControlName=email]').type("test@gmail.com");
    cy.get('input[formControlName=password]').type(faker.internet.password());


    cy.get('button[type="submit"]').click();

    cy.url().should('include', '/login');
  });

  it('Affiche une erreur si l\'e-mail existe déjà', () => {
    cy.visit('/register');

    // Stubber la requête POST vers le service d'authentification avec un e-mail existant
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 400, // Simuler une erreur de conflit (e-mail déjà existant)
      body: {
        error: 'Error: Email is already taken!'
      },
      delayMs: 500 // Optionnel : ajouter un délai pour simuler une réponse asynchrone
    }).as('registerRequest');


    cy.get('input[formControlName=firstName]').type('John');
    cy.get('input[formControlName=lastName]').type('Doe');
    cy.get('input[formControlName=email]').type('existing@example.com');
    cy.get('input[formControlName=password]').type('password123');

    cy.get('button[type="submit"]').click();

    // Attendre que la requête vers le service d'authentification soit interceptée
    cy.wait('@registerRequest');

    cy.contains('An error occurred');

  });
});
