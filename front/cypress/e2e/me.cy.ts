describe('User e2e me test', () => {
    it('Me', () => {
  
      let sessionUsers: Number[] = [];
      cy.login('yoga@studio.com','test!1234');
  
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
          password: "password",
          createdAt: new Date(),
          updatedAt: new Date()
  
        },
      ).as('user')
  
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
  
      cy.get('span[routerLink=me]').click().then(()=>{
        cy.url().should('include', '/me').then(()=>{
          cy.get('p').contains("Name: John "+("Doe").toUpperCase())
          cy.get('p').contains("Email: yoga@studio.com")
        })
      })

      cy.get('button').first().click()
      cy.url().should('include', '/sessions')
    })
  });