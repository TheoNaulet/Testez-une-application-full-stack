describe('User e2e me test', () => {
  it('Me', () => {
    let sessionUsers: Number[] = []; // Initialize an empty array for session users

    // Log in as a user with email and password
    cy.login('yoga@studio.com','test!1234');

    // Intercept the API call to fetch user details and return mock data
    cy.intercept(
      {
        method: 'GET',
        url: '/api/user/1',
      },
      {
        id: 1,
        username: 'JohnDoe',
        firstName: 'John',
        lastName: 'Doe',
        email: "yoga@studio.com",
        admin: false,
        password: "password", // Mocked password (should not be stored like this in production)
        createdAt: new Date(),
        updatedAt: new Date()
      },
    ).as('user')

    // Intercept the API call to fetch session details and return mock data
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      [
        {
          id: 1,
          name: 'Session name',
          date: new Date(),
          teacher_id: 1,
          description: "A small description",
          users: [],
          createdAt: new Date(),
          updatedAt: new Date()
        }
      ]).as('session')

    // Intercept the API call to fetch session details for a specific session
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/1',
      },
      {
        id: 1,
        name: 'Session name',
        date: new Date(),
        teacher_id: 1,
        description: "A small description",
        users: sessionUsers,
        createdAt: new Date(),
        updatedAt: new Date()
      }
    ).as('session')

    // Navigate to the "Me" page by clicking the respective link
    cy.get('span[routerLink=me]').click().then(()=>{
      // Verify that the URL includes "/me"
      cy.url().should('include', '/me').then(()=>{
        // Verify that the correct user details are displayed
        cy.get('p').contains("Name: John "+("Doe").toUpperCase()) // Ensure the last name is in uppercase
        cy.get('p').contains("Email: yoga@studio.com")
      })
    })

    // Click the first button on the page (assumed to be a navigation button)
    cy.get('button').first().click()
    // Verify that the URL changes to the sessions page
    cy.url().should('include', '/sessions')
  })

  it('should delete the user account', () => {
    // Log in as a user
    cy.login('yoga@studio.com', 'test!1234');

    // Intercept the API call to fetch user details
    cy.intercept('GET', '/api/user/1', {
      id: 1,
      firstName: 'John',
      lastName: 'Doe',
      email: "yoga@studio.com",
      admin: false,
      createdAt: new Date(),
      updatedAt: new Date()
    }).as('user');

    // Intercept the DELETE request to simulate account deletion
    cy.intercept('DELETE', '/api/user/1', {}).as('deleteUser');

    // Navigate to the "Me" page
    cy.get('span[routerLink=me]').click();
    cy.url().should('include', '/me');

    // Verify the delete button is visible and click it
    cy.get('button').contains('Detail').click();

    // Wait for the DELETE request and confirm it was called
    cy.wait('@deleteUser').its('request.method').should('eq', 'DELETE');

    // Check if the snack-bar appears with the correct message
    cy.get('.mat-snack-bar-container').should('contain.text', 'Your account has been deleted !');

    // Verify that the user is redirected to the home page
    cy.url().should('eq', Cypress.config().baseUrl);
  });
});
