package com.b3.cripto.web.rest;

import static com.b3.cripto.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.b3.cripto.IntegrationTest;
import com.b3.cripto.domain.Deposito;
import com.b3.cripto.repository.DepositoRepository;
import com.b3.cripto.service.dto.DepositoDTO;
import com.b3.cripto.service.mapper.DepositoMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DepositoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DepositoResourceIT {

    private static final BigDecimal DEFAULT_VOLUME = new BigDecimal(1);
    private static final BigDecimal UPDATED_VOLUME = new BigDecimal(2);

    private static final UUID DEFAULT_ID_DEPOSITO = UUID.randomUUID();
    private static final UUID UPDATED_ID_DEPOSITO = UUID.randomUUID();

    private static final String DEFAULT_CLIENT_ACCOUNT = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_ACCOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_ENTIDADE_ACCOUNT = "AAAAAAAAAA";
    private static final String UPDATED_ENTIDADE_ACCOUNT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/depositos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DepositoRepository depositoRepository;

    @Autowired
    private DepositoMapper depositoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepositoMockMvc;

    private Deposito deposito;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deposito createEntity(EntityManager em) {
        Deposito deposito = new Deposito()
            .volume(DEFAULT_VOLUME)
            .id_deposito(DEFAULT_ID_DEPOSITO)
            .client_account(DEFAULT_CLIENT_ACCOUNT)
            .entidade_account(DEFAULT_ENTIDADE_ACCOUNT);
        return deposito;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deposito createUpdatedEntity(EntityManager em) {
        Deposito deposito = new Deposito()
            .volume(UPDATED_VOLUME)
            .id_deposito(UPDATED_ID_DEPOSITO)
            .client_account(UPDATED_CLIENT_ACCOUNT)
            .entidade_account(UPDATED_ENTIDADE_ACCOUNT);
        return deposito;
    }

    @BeforeEach
    public void initTest() {
        deposito = createEntity(em);
    }

    @Test
    @Transactional
    void createDeposito() throws Exception {
        int databaseSizeBeforeCreate = depositoRepository.findAll().size();
        // Create the Deposito
        DepositoDTO depositoDTO = depositoMapper.toDto(deposito);
        restDepositoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(depositoDTO)))
            .andExpect(status().isCreated());

        // Validate the Deposito in the database
        List<Deposito> depositoList = depositoRepository.findAll();
        assertThat(depositoList).hasSize(databaseSizeBeforeCreate + 1);
        Deposito testDeposito = depositoList.get(depositoList.size() - 1);
        assertThat(testDeposito.getVolume()).isEqualByComparingTo(DEFAULT_VOLUME);
        assertThat(testDeposito.getId_deposito()).isEqualTo(DEFAULT_ID_DEPOSITO);
        assertThat(testDeposito.getClient_account()).isEqualTo(DEFAULT_CLIENT_ACCOUNT);
        assertThat(testDeposito.getEntidade_account()).isEqualTo(DEFAULT_ENTIDADE_ACCOUNT);
    }

    @Test
    @Transactional
    void createDepositoWithExistingId() throws Exception {
        // Create the Deposito with an existing ID
        deposito.setId(1L);
        DepositoDTO depositoDTO = depositoMapper.toDto(deposito);

        int databaseSizeBeforeCreate = depositoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepositoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(depositoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Deposito in the database
        List<Deposito> depositoList = depositoRepository.findAll();
        assertThat(depositoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDepositos() throws Exception {
        // Initialize the database
        depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList
        restDepositoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deposito.getId().intValue())))
            .andExpect(jsonPath("$.[*].volume").value(hasItem(sameNumber(DEFAULT_VOLUME))))
            .andExpect(jsonPath("$.[*].id_deposito").value(hasItem(DEFAULT_ID_DEPOSITO.toString())))
            .andExpect(jsonPath("$.[*].client_account").value(hasItem(DEFAULT_CLIENT_ACCOUNT)))
            .andExpect(jsonPath("$.[*].entidade_account").value(hasItem(DEFAULT_ENTIDADE_ACCOUNT)));
    }

    @Test
    @Transactional
    void getDeposito() throws Exception {
        // Initialize the database
        depositoRepository.saveAndFlush(deposito);

        // Get the deposito
        restDepositoMockMvc
            .perform(get(ENTITY_API_URL_ID, deposito.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deposito.getId().intValue()))
            .andExpect(jsonPath("$.volume").value(sameNumber(DEFAULT_VOLUME)))
            .andExpect(jsonPath("$.id_deposito").value(DEFAULT_ID_DEPOSITO.toString()))
            .andExpect(jsonPath("$.client_account").value(DEFAULT_CLIENT_ACCOUNT))
            .andExpect(jsonPath("$.entidade_account").value(DEFAULT_ENTIDADE_ACCOUNT));
    }

    @Test
    @Transactional
    void getNonExistingDeposito() throws Exception {
        // Get the deposito
        restDepositoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDeposito() throws Exception {
        // Initialize the database
        depositoRepository.saveAndFlush(deposito);

        int databaseSizeBeforeUpdate = depositoRepository.findAll().size();

        // Update the deposito
        Deposito updatedDeposito = depositoRepository.findById(deposito.getId()).get();
        // Disconnect from session so that the updates on updatedDeposito are not directly saved in db
        em.detach(updatedDeposito);
        updatedDeposito
            .volume(UPDATED_VOLUME)
            .id_deposito(UPDATED_ID_DEPOSITO)
            .client_account(UPDATED_CLIENT_ACCOUNT)
            .entidade_account(UPDATED_ENTIDADE_ACCOUNT);
        DepositoDTO depositoDTO = depositoMapper.toDto(updatedDeposito);

        restDepositoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depositoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depositoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Deposito in the database
        List<Deposito> depositoList = depositoRepository.findAll();
        assertThat(depositoList).hasSize(databaseSizeBeforeUpdate);
        Deposito testDeposito = depositoList.get(depositoList.size() - 1);
        assertThat(testDeposito.getVolume()).isEqualByComparingTo(UPDATED_VOLUME);
        assertThat(testDeposito.getId_deposito()).isEqualTo(UPDATED_ID_DEPOSITO);
        assertThat(testDeposito.getClient_account()).isEqualTo(UPDATED_CLIENT_ACCOUNT);
        assertThat(testDeposito.getEntidade_account()).isEqualTo(UPDATED_ENTIDADE_ACCOUNT);
    }

    @Test
    @Transactional
    void putNonExistingDeposito() throws Exception {
        int databaseSizeBeforeUpdate = depositoRepository.findAll().size();
        deposito.setId(count.incrementAndGet());

        // Create the Deposito
        DepositoDTO depositoDTO = depositoMapper.toDto(deposito);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepositoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depositoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depositoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deposito in the database
        List<Deposito> depositoList = depositoRepository.findAll();
        assertThat(depositoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeposito() throws Exception {
        int databaseSizeBeforeUpdate = depositoRepository.findAll().size();
        deposito.setId(count.incrementAndGet());

        // Create the Deposito
        DepositoDTO depositoDTO = depositoMapper.toDto(deposito);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepositoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depositoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deposito in the database
        List<Deposito> depositoList = depositoRepository.findAll();
        assertThat(depositoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeposito() throws Exception {
        int databaseSizeBeforeUpdate = depositoRepository.findAll().size();
        deposito.setId(count.incrementAndGet());

        // Create the Deposito
        DepositoDTO depositoDTO = depositoMapper.toDto(deposito);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepositoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(depositoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deposito in the database
        List<Deposito> depositoList = depositoRepository.findAll();
        assertThat(depositoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDepositoWithPatch() throws Exception {
        // Initialize the database
        depositoRepository.saveAndFlush(deposito);

        int databaseSizeBeforeUpdate = depositoRepository.findAll().size();

        // Update the deposito using partial update
        Deposito partialUpdatedDeposito = new Deposito();
        partialUpdatedDeposito.setId(deposito.getId());

        partialUpdatedDeposito.id_deposito(UPDATED_ID_DEPOSITO);

        restDepositoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeposito.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeposito))
            )
            .andExpect(status().isOk());

        // Validate the Deposito in the database
        List<Deposito> depositoList = depositoRepository.findAll();
        assertThat(depositoList).hasSize(databaseSizeBeforeUpdate);
        Deposito testDeposito = depositoList.get(depositoList.size() - 1);
        assertThat(testDeposito.getVolume()).isEqualByComparingTo(DEFAULT_VOLUME);
        assertThat(testDeposito.getId_deposito()).isEqualTo(UPDATED_ID_DEPOSITO);
        assertThat(testDeposito.getClient_account()).isEqualTo(DEFAULT_CLIENT_ACCOUNT);
        assertThat(testDeposito.getEntidade_account()).isEqualTo(DEFAULT_ENTIDADE_ACCOUNT);
    }

    @Test
    @Transactional
    void fullUpdateDepositoWithPatch() throws Exception {
        // Initialize the database
        depositoRepository.saveAndFlush(deposito);

        int databaseSizeBeforeUpdate = depositoRepository.findAll().size();

        // Update the deposito using partial update
        Deposito partialUpdatedDeposito = new Deposito();
        partialUpdatedDeposito.setId(deposito.getId());

        partialUpdatedDeposito
            .volume(UPDATED_VOLUME)
            .id_deposito(UPDATED_ID_DEPOSITO)
            .client_account(UPDATED_CLIENT_ACCOUNT)
            .entidade_account(UPDATED_ENTIDADE_ACCOUNT);

        restDepositoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeposito.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeposito))
            )
            .andExpect(status().isOk());

        // Validate the Deposito in the database
        List<Deposito> depositoList = depositoRepository.findAll();
        assertThat(depositoList).hasSize(databaseSizeBeforeUpdate);
        Deposito testDeposito = depositoList.get(depositoList.size() - 1);
        assertThat(testDeposito.getVolume()).isEqualByComparingTo(UPDATED_VOLUME);
        assertThat(testDeposito.getId_deposito()).isEqualTo(UPDATED_ID_DEPOSITO);
        assertThat(testDeposito.getClient_account()).isEqualTo(UPDATED_CLIENT_ACCOUNT);
        assertThat(testDeposito.getEntidade_account()).isEqualTo(UPDATED_ENTIDADE_ACCOUNT);
    }

    @Test
    @Transactional
    void patchNonExistingDeposito() throws Exception {
        int databaseSizeBeforeUpdate = depositoRepository.findAll().size();
        deposito.setId(count.incrementAndGet());

        // Create the Deposito
        DepositoDTO depositoDTO = depositoMapper.toDto(deposito);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepositoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, depositoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depositoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deposito in the database
        List<Deposito> depositoList = depositoRepository.findAll();
        assertThat(depositoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeposito() throws Exception {
        int databaseSizeBeforeUpdate = depositoRepository.findAll().size();
        deposito.setId(count.incrementAndGet());

        // Create the Deposito
        DepositoDTO depositoDTO = depositoMapper.toDto(deposito);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepositoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depositoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deposito in the database
        List<Deposito> depositoList = depositoRepository.findAll();
        assertThat(depositoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeposito() throws Exception {
        int databaseSizeBeforeUpdate = depositoRepository.findAll().size();
        deposito.setId(count.incrementAndGet());

        // Create the Deposito
        DepositoDTO depositoDTO = depositoMapper.toDto(deposito);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepositoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(depositoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deposito in the database
        List<Deposito> depositoList = depositoRepository.findAll();
        assertThat(depositoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeposito() throws Exception {
        // Initialize the database
        depositoRepository.saveAndFlush(deposito);

        int databaseSizeBeforeDelete = depositoRepository.findAll().size();

        // Delete the deposito
        restDepositoMockMvc
            .perform(delete(ENTITY_API_URL_ID, deposito.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Deposito> depositoList = depositoRepository.findAll();
        assertThat(depositoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
