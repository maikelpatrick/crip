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

describe('Saque e2e test', () => {
  const saquePageUrl = '/saque';
  const saquePageUrlPattern = new RegExp('/saque(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const saqueSample = { volume: 29927 };

  let saque: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/cripto/api/saques+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/cripto/api/saques').as('postEntityRequest');
    cy.intercept('DELETE', '/services/cripto/api/saques/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (saque) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/cripto/api/saques/${saque.id}`,
      }).then(() => {
        saque = undefined;
      });
    }
  });

  it('Saques menu should load Saques page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('saque');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Saque').should('exist');
    cy.url().should('match', saquePageUrlPattern);
  });

  describe('Saque page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(saquePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Saque page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/saque/new$'));
        cy.getEntityCreateUpdateHeading('Saque');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', saquePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/cripto/api/saques',
          body: saqueSample,
        }).then(({ body }) => {
          saque = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/cripto/api/saques+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/cripto/api/saques?page=0&size=20>; rel="last",<http://localhost/services/cripto/api/saques?page=0&size=20>; rel="first"',
              },
              body: [saque],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(saquePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Saque page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('saque');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', saquePageUrlPattern);
      });

      it('edit button click should load edit Saque page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Saque');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', saquePageUrlPattern);
      });

      it('last delete button click should delete instance of Saque', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('saque').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', saquePageUrlPattern);

        saque = undefined;
      });
    });
  });

  describe('new Saque page', () => {
    beforeEach(() => {
      cy.visit(`${saquePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Saque');
    });

    it('should create an instance of Saque', () => {
      cy.get(`[data-cy="volume"]`).type('17458').should('have.value', '17458');

      cy.get(`[data-cy="client_account"]`).type('cross-platform Bebê').should('have.value', 'cross-platform Bebê');

      cy.get(`[data-cy="entidade_account"]`).type('COM').should('have.value', 'COM');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        saque = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', saquePageUrlPattern);
    });
  });
});
