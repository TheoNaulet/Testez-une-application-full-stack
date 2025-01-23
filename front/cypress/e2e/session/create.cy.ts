describe('Session Creation Test', () => {
  // Mock data for teachers
  const mockTeachers = [
      {
          id: 1,
          lastName: "Doe",
          firstName: "John",
          createdAt: new Date(),
          updatedAt: new Date(),
      },
      {
          id: 2,
          lastName: "Dupont",
          firstName: "Louis",
          createdAt: new Date(),
          updatedAt: new Date(),
      }
  ];

  it('Creates a new session', () => {
      // Log in with valid credentials
      cy.login('yoga@studio.com', 'test!1234');

      // Mock the API call to retrieve teacher data
      cy.intercept('GET', '/api/teacher', {
          body: mockTeachers
      });

      // Mock the API call for creating a session
      cy.intercept('POST', '/api/session', {
          statusCode: 200
      });

      // Mock API call to retrieve all sessions
      cy.intercept('GET', '/api/session', [
          {
              id: 1,
              name: "Morning Flow",
              date: new Date(),
              teacher_id: 1,
              description: "A rejuvenating morning session.",
              createdAt: new Date(),
              updatedAt: new Date()
          }
      ]).as('getSessions');

      // Navigate to the create session page
      cy.get('button[routerlink="create"]').click();

      // Verify redirection to the session creation page
      cy.url().should('include', '/sessions/create');

      // Fill out the session creation form
      cy.get('input[formControlName=name]').type("Morning Flow");
      cy.get('input[formControlName=date]').type("2023-07-14");

      // Select a teacher from the dropdown
      cy.get('mat-select[formControlName=teacher_id]').click().then(() => {
          cy.get('.cdk-overlay-container .mat-select-panel .mat-option-text')
            .should('contain', mockTeachers[0].firstName);
          cy.get(`.cdk-overlay-container .mat-select-panel .mat-option-text:contains(${mockTeachers[0].firstName})`)
            .first()
            .click()
            .then(() => {
                cy.get(`[formcontrolname=teacher_id]`).contains(mockTeachers[0].firstName);
            });
      });

      // Add a description
      cy.get('textarea[formControlName=description]').type("A rejuvenating morning session.");

      // Submit the form
      cy.get('button[type=submit]').click();

      // Verify redirection to the sessions list page
      cy.url().should('include', '/sessions');
  });
});
