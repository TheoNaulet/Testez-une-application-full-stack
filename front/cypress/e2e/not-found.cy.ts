describe('Not Found Page', () => {
    it('should display the 404 page for an unknown route', () => {
      // Visit a non-existing page
      cy.visit('/random-page', { failOnStatusCode: false });
  
      // Ensure the URL is redirected to /404
      cy.url().should('include', '/404');
  
      // Check that the page displays the correct "Page not found!" message
      cy.get('h1').should('contain.text', 'Page not found !');
  
      // Ensure the container has the correct CSS classes for styling
      cy.get('div').should('have.class', 'flex')
        .and('have.class', 'justify-center')
        .and('have.class', 'mt3');
    });
  });
  