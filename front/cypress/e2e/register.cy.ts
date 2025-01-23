describe('Register spec', () => {
  it('Should allow submission for valid inputs', () => {
      // Navigate to the registration page
      cy.visit('/register');

      // Intercept the API call for registration
      cy.intercept('POST', '/api/auth/register', {
        statusCode: 201, // Mock successful response
      }).as('register');

      // Fill out the form with valid inputs
      cy.get('input[formControlName=firstName]').type('Joe'); // First name field
      cy.get('input[formControlName=lastName]').type('Jones'); // Last name field
      cy.get('input[formControlName=email]').type('joe.jones@email.com'); // Email field
      cy.get('input[formControlName=password]').type('SecuredPassword123'); // Password field

      // Ensure the "Submit" button is enabled
      cy.get('button[type="submit"]').should('not.be.disabled');

      // Click the submit button
      cy.get('button[type="submit"]').click();

      // Verify the API was called and returned a 201 status code
      cy.wait('@register').its('response.statusCode').should('eq', 201);

      // Confirm redirection to the login page
      cy.url().should('include', '/login');
  });

  it('Should disable submit button if form is incomplete', () => {
      // Navigate to the registration page
      cy.visit('/register');

      // Verify that fields are initially empty
      cy.get('input[formControlName=firstName]').should('have.value', ''); // First name field
      cy.get('input[formControlName=lastName]').should('have.value', ''); // Last name field
      cy.get('input[formControlName=email]').should('have.value', ''); // Email field
      cy.get('input[formControlName=password]').should('have.value', ''); // Password field

      // Ensure the "Submit" button is disabled
      cy.get('button[type="submit"]').should('be.disabled');
  });

  it('Should keep submit button disabled for invalid email', () => {
      // Navigate to the registration page
      cy.visit('/register');

      // Fill out other fields with valid inputs
      cy.get('input[formControlName=firstName]').type('Joe'); // First name field
      cy.get('input[formControlName=lastName]').type('Jones'); // Last name field
      cy.get('input[formControlName=password]').type('SecuredPassword123'); // Password field

      // Enter an invalid email format
      cy.get('input[formControlName=email]').type('invalid-email'); // Email field

      // Ensure the "Submit" button remains disabled
      cy.get('button[type="submit"]').should('be.disabled');
  });
});
