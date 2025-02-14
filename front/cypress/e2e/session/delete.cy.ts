describe('Session Deletion Test', () => {
  // Define mock data for teachers
  const mockTeachers = [
      {
          id: 1,
          lastName: "Pierre",
          firstName: "Louis",
          createdAt: new Date(),
          updatedAt: new Date(),
      },
      {
          id: 2,
          lastName: "Martin",
          firstName: "Jean",
          createdAt: new Date(),
          updatedAt: new Date(),
      }
  ];

  it('Successfully removes a session', () => {
      // Log in with valid credentials
      cy.login('yoga@studio.com', 'test!1234');

      // Intercept API call to fetch teacher data
      cy.intercept('GET', '/api/teacher', {
          body: mockTeachers
      });

      // Intercept API call for session deletion
      cy.intercept('DELETE', '/api/session/1', {
          statusCode: 200
      });

      // Mock API call to retrieve all sessions (returns an empty list)
      cy.intercept('GET', '/api/session', []).as('getAllSessions');

      // Mock API call to retrieve a specific session's details
      cy.intercept('GET', '/api/session/1', {
          id: 1,
          name: 'Yoga Session',
          date: new Date(),
          teacher_id: 1,
          description: "An engaging yoga class.",
          users: [],
          createdAt: new Date(),
          updatedAt: new Date()
      }).as('getSessionDetails');

      // Navigate to session details by simulating button click
      cy.get('button span').contains("Detail").click();

      // Ensure the URL contains the session detail path
      cy.url().should('include', '/sessions/detail/1');

      // Trigger the deletion process by clicking the delete button
      cy.get('button span').contains("Delete").click();

      // Verify that the user is redirected to the sessions list page
      cy.url().should('include', '/sessions');
  });
});
