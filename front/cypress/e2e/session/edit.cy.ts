describe('Session Update Test', () => {
  // Mock data for teachers
  const mockTeachers = [
      {
          id: 1,
          lastName: "Smith",
          firstName: "Jane",
          createdAt: new Date(),
          updatedAt: new Date(),
      },
      {
          id: 2,
          lastName: "Brown",
          firstName: "James",
          createdAt: new Date(),
          updatedAt: new Date(),
      }
  ];

  it('Modifies an existing session', () => {
      // Log in with valid credentials
      cy.login('yoga@studio.com', 'test!1234');

      // Mock the API call to retrieve teacher data
      cy.intercept('GET', '/api/teacher', {
          body: mockTeachers
      });

      // Mock the API call for creating a new session
      cy.intercept('POST', '/api/session', {
          statusCode: 200
      });

      // Mock API call to retrieve all sessions
      cy.intercept('GET', '/api/session', [
          {
              id: 1,
              name: "Morning Yoga",
              date: new Date(),
              teacher_id: 1,
              description: "Relax and energize.",
              users: [],
              createdAt: new Date(),
              updatedAt: new Date()
          }
      ]).as('getSessions');

      // Mock API call to retrieve a specific session
      cy.intercept('GET', '/api/session/1', {
          id: 1,
          name: "Evening Pilates",
          date: new Date(),
          teacher_id: 1,
          description: "Strengthen your core.",
          users: [],
          createdAt: new Date(),
          updatedAt: new Date()
      }).as('getSessionDetails');

      // Mock API call for updating a session
      cy.intercept('PUT', '/api/session/1', {
          statusCode: 200
      });

      // Navigate to the edit page by clicking the "Edit" button
      cy.get('button span').contains("Edit").click();

      // Ensure the URL is updated to the session update page
      cy.url().should('include', '/sessions/update/1');

      // Update the session name
      cy.get('input[formControlName=name]').clear();
      cy.get('input[formControlName=name]').type("Morning Yoga");

      // Submit the form
      cy.get('button[type=submit]').click();

      // Verify redirection to the sessions list page
      cy.url().should('include', '/sessions');
  });
});
