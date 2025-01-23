describe('Logout spec', () => {
  it('Logout successfully', () => {
      // Log in with valid credentials using a reusable login command
      cy.login('yoga@studio.com', 'test!1234');

      // Click the Logout link
      cy.get('span[class=link]').contains("Logout").click();

      // Verify that the user is redirected to the homepage after logging out
      cy.url().should('eq', 'http://localhost:4200/');
  });
});
