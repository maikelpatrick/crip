import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Deposito e2e test', () => {
  const depositoPageUrl = '/deposito';
  const depositoPageUrlPattern = new RegExp('/deposito(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const depositoSample = { volume: 97074 };

  let deposito: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/cripto/api/depositos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/cripto/api/depositos').as('postEntityRequest');
    cy.intercept('DELETE', '/services/cripto/api/depositos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (deposito) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/cripto/api/depositos/${deposito.id}`,
      }).then(() => {
        deposito = undefined;
      });
    }
  });

  it('Depositos menu should load Depositos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('deposito');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Deposito').should('exist');
    cy.url().should('match', depositoPageUrlPattern);
  });

  describe('Deposito page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(depositoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Deposito page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/deposito/new$'));
        cy.getEntityCreateUpdateHeading('Deposito');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', depositoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/cripto/api/depositos',
          body: depositoSample,
        }).then(({ body }) => {
          deposito = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/cripto/api/depositos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [deposito],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(depositoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Deposito page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('deposito');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', depositoPageUrlPattern);
      });

      it('edit button click should load edit Deposito page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Deposito');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', depositoPageUrlPattern);
      });

      it('last delete button click should delete instance of Deposito', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('deposito').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', depositoPageUrlPattern);

        deposito = undefined;
      });
    });
  });

  describe('new Deposito page', () => {
    beforeEach(() => {
      cy.visit(`${depositoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Deposito');
    });

    it('should create an instance of Deposito', () => {
      cy.get(`[data-cy="volume"]`).type('42659').should('have.value', '42659');

      cy.get(`[data-cy="id_deposito"]`)
        .type('3f7c8734-216e-41ee-9b81-d88ff9cce273')
        .invoke('val')
        .should('match', new RegExp('3f7c8734-216e-41ee-9b81-d88ff9cce273'));

      cy.get(`[data-cy="client_account"]`).type('neural Marginal Savings').should('have.value', 'neural Marginal Savings');

      cy.get(`[data-cy="entidade_account"]`).type('Honduras Checking').should('have.value', 'Honduras Checking');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        deposito = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', depositoPageUrlPattern);
    });
  });
});
