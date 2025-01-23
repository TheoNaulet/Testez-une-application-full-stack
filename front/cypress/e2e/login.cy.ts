describe('Login spec', () => {
  it('Login successfully', () => {
    // Navigate to the login page
    cy.visit('/login');

    // Mock the API call for login
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,                // Mocked user ID
        username: 'userName', // Mocked username
        firstName: 'firstName', // Mocked first name
        lastName: 'lastName', // Mocked last name
        admin: true           // Indicates if the user is an admin
      },
    });

    // Mock the API call for fetching sessions after login
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      [] // Mock response as an empty array for sessions
    ).as('session');

    // Fill in the login form with email and password
    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);

    // Verify that the user is redirected to the sessions page after login
    cy.url().should('include', '/sessions');
  });
});
