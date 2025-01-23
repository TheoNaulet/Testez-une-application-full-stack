describe('User participate spec', () => {

  it('Login successfully', () => {
      // Mock array to hold session participants
      let sessionUsers: Number[] = [];

      // Navigate to the login page
      cy.visit('/login');

      // Intercept API call for login
      cy.intercept('POST', '/api/auth/login', {
          body: {
              id: 1,
              username: 'userName',
              firstName: 'firstName',
              lastName: 'lastName',
              admin: false // Mock non-admin user
          },
      });

      // Intercept API call for fetching all sessions
      cy.intercept(
          {
              method: 'GET',
              url: '/api/session',
          },
          [
              {
                  id: 1,
                  name: "Test",
                  date: new Date(),
                  teacher_id: 1,
                  description: "Test description",
                  users: [],
                  createdAt: new Date(),
                  updatedAt: new Date()
              }
          ]).as('session');

      // Intercept API call for fetching a specific session
      cy.intercept(
          {
              method: 'GET',
              url: '/api/session/1',
          },
          {
              id: 1,
              name: "Test",
              date: new Date(),
              teacher_id: 1,
              description: "Test description",
              users: sessionUsers,
              createdAt: new Date(),
              updatedAt: new Date()
          }
      ).as('session');

      // Fill out login form and submit
      cy.get('input[formControlName=email]').type("yoga@studio.com");
      cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);

      // Verify redirection to the sessions page
      cy.url().should('include', '/sessions');
  });

  it('Account details', () => {
      // Mock array to hold session participants
      let sessionUsers: Number[] = [];

      // Intercept API call for fetching user details
      cy.intercept(
          {
              method: 'GET',
              url: '/api/user/1',
          },
          {
              id: 1,
              username: 'userName',
              firstName: 'firstName',
              lastName: 'lastName',
              email: "test@mail.com",
              admin: false,
              password: "password",
              createdAt: new Date(),
              updatedAt: new Date()
          },
      ).as('user');

      // Intercept API calls for fetching sessions
      cy.intercept(
          {
              method: 'GET',
              url: '/api/session',
          },
          [
              {
                  id: 1,
                  name: "Test",
                  date: new Date(),
                  teacher_id: 1,
                  description: "Test description",
                  users: [],
                  createdAt: new Date(),
                  updatedAt: new Date()
              }
          ]).as('session');

      cy.intercept(
          {
              method: 'GET',
              url: '/api/session/1',
          },
          {
              id: 1,
              name: "Test",
              date: new Date(),
              teacher_id: 1,
              description: "Test description",
              users: sessionUsers,
              createdAt: new Date(),
              updatedAt: new Date()
          }
      ).as('session');

      // Access account details page
      cy.get('span[routerLink=me]').click().then(() => {
          cy.url().should('include', '/me').then(() => {
              cy.get('p').contains("Name: firstName " + ("lastName").toUpperCase());
              cy.get('p').contains("Email: test@mail.com");
          });
      });

      // Navigate back to sessions
      cy.get('button').first().click();
      cy.url().should('include', '/sessions');

      // View session details
      cy.get('button span').contains("Detail").click();
      cy.url().should('include', '/sessions/detail/1');
  });

  it('Participate in a session', () => {
      // Mock array to represent session attendees
      let sessionUsers: Number[] = [1];

      // Intercept API call for fetching teacher details
      cy.intercept('GET', '/api/teacher/1', {
          body: {
              id: 1,
              lastName: "Test",
              firstName: "TEST",
              createdAt: new Date(),
              updatedAt: new Date(),
          },
      });

      // Intercept API call for participating in a session
      cy.intercept('POST', '/api/session/1/participate/1', {
          status: 200,
      });

      // Intercept API call for fetching session details
      cy.intercept(
          {
              method: 'GET',
              url: '/api/session/1',
          },
          {
              id: 1,
              name: "Test",
              date: new Date(),
              teacher_id: 1,
              description: "Test description",
              users: sessionUsers,
              createdAt: new Date(),
              updatedAt: new Date()
          }
      ).as('session');

      // Participate in session and verify changes
      cy.get('h1').contains("Test").then(() => {
          sessionUsers.push(1);
          cy.get('button span').contains("Participate").click().then(() => {
              cy.wait(500);
              cy.get('button span').contains('Do not participate');
              cy.get('span[class=ml1]').contains("1 attendees");
          });
      });
  });

  it('Do not participate in a session', () => {
      // Intercept API call for fetching teachers
      cy.intercept('GET', '/api/teacher', {
          body: [
              {
                  id: 1,
                  lastName: "Test",
                  firstName: "TEST",
                  createdAt: new Date(),
                  updatedAt: new Date(),
              },
              {
                  id: 2,
                  lastName: "Test2",
                  firstName: "TEST2",
                  createdAt: new Date(),
                  updatedAt: new Date(),
              }
          ]
      });

      // Intercept API call for removing participation
      cy.intercept('DELETE', '/api/session/1/participate/1', {
          status: 200,
      });

      // Intercept API calls for sessions
      cy.intercept(
          {
              method: 'GET',
              url: '/api/session',
          },
          []
      ).as('session');

      cy.intercept(
          {
              method: 'GET',
              url: '/api/session/1',
          },
          {
              id: 1,
              name: "Test",
              date: new Date(),
              teacher_id: 1,
              description: "Test description",
              users: [],
              createdAt: new Date(),
              updatedAt: new Date()
          }
      ).as('session');

      // Opt out of session and verify changes
      cy.get('button span').contains("Do not participate").click();
      cy.get('span[class=ml1]').contains("0 attendees");
  });

  it('Logout successfully', () => {
      // Click the logout link
      cy.get('span[class=link]').contains("Logout").click();

      // Verify redirection to the homepage
      cy.url().should('eq', 'http://localhost:4200/');
  });
});
