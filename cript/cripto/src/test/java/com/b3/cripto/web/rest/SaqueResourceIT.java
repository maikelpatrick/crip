package com.b3.cripto.web.rest;

import static com.b3.cripto.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.b3.cripto.IntegrationTest;
import com.b3.cripto.domain.Saque;
import com.b3.cripto.repository.SaqueRepository;
import com.b3.cripto.service.dto.SaqueDTO;
import com.b3.cripto.service.mapper.SaqueMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
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
 * Integration tests for the {@link SaqueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SaqueResourceIT {

    private static final BigDecimal DEFAULT_VOLUME = new BigDecimal(1);
    private static final BigDecimal UPDATED_VOLUME = new BigDecimal(2);

    private static final String DEFAULT_CLIENT_ACCOUNT = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_ACCOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_ENTIDADE_ACCOUNT = "AAAAAAAAAA";
    private static final String UPDATED_ENTIDADE_ACCOUNT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/saques";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SaqueRepository saqueRepository;

    @Autowired
    private SaqueMapper saqueMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSaqueMockMvc;

    private Saque saque;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Saque createEntity(EntityManager em) {
        Saque saque = new Saque().volume(DEFAULT_VOLUME).client_account(DEFAULT_CLIENT_ACCOUNT).entidade_account(DEFAULT_ENTIDADE_ACCOUNT);
        return saque;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Saque createUpdatedEntity(EntityManager em) {
        Saque saque = new Saque().volume(UPDATED_VOLUME).client_account(UPDATED_CLIENT_ACCOUNT).entidade_account(UPDATED_ENTIDADE_ACCOUNT);
        return saque;
    }

    @BeforeEach
    public void initTest() {
        saque = createEntity(em);
    }

    @Test
    @Transactional
    void createSaque() throws Exception {
        int databaseSizeBeforeCreate = saqueRepository.findAll().size();
        // Create the Saque
        SaqueDTO saqueDTO = saqueMapper.toDto(saque);
        restSaqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(saqueDTO)))
            .andExpect(status().isCreated());

        // Validate the Saque in the database
        List<Saque> saqueList = saqueRepository.findAll();
        assertThat(saqueList).hasSize(databaseSizeBeforeCreate + 1);
        Saque testSaque = saqueList.get(saqueList.size() - 1);
        assertThat(testSaque.getVolume()).isEqualByComparingTo(DEFAULT_VOLUME);
        assertThat(testSaque.getClient_account()).isEqualTo(DEFAULT_CLIENT_ACCOUNT);
        assertThat(testSaque.getEntidade_account()).isEqualTo(DEFAULT_ENTIDADE_ACCOUNT);
    }

    @Test
    @Transactional
    void createSaqueWithExistingId() throws Exception {
        // Create the Saque with an existing ID
        saque.setId(1L);
        SaqueDTO saqueDTO = saqueMapper.toDto(saque);

        int databaseSizeBeforeCreate = saqueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSaqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(saqueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Saque in the database
        List<Saque> saqueList = saqueRepository.findAll();
        assertThat(saqueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSaques() throws Exception {
        // Initialize the database
        saqueRepository.saveAndFlush(saque);

        // Get all the saqueList
        restSaqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saque.getId().intValue())))
            .andExpect(jsonPath("$.[*].volume").value(hasItem(sameNumber(DEFAULT_VOLUME))))
            .andExpect(jsonPath("$.[*].client_account").value(hasItem(DEFAULT_CLIENT_ACCOUNT)))
            .andExpect(jsonPath("$.[*].entidade_account").value(hasItem(DEFAULT_ENTIDADE_ACCOUNT)));
    }

    @Test
    @Transactional
    void getSaque() throws Exception {
        // Initialize the database
        saqueRepository.saveAndFlush(saque);

        // Get the saque
        restSaqueMockMvc
            .perform(get(ENTITY_API_URL_ID, saque.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(saque.getId().intValue()))
            .andExpect(jsonPath("$.volume").value(sameNumber(DEFAULT_VOLUME)))
            .andExpect(jsonPath("$.client_account").value(DEFAULT_CLIENT_ACCOUNT))
            .andExpect(jsonPath("$.entidade_account").value(DEFAULT_ENTIDADE_ACCOUNT));
    }

    @Test
    @Transactional
    void getNonExistingSaque() throws Exception {
        // Get the saque
        restSaqueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSaque() throws Exception {
        // Initialize the database
        saqueRepository.saveAndFlush(saque);

        int databaseSizeBeforeUpdate = saqueRepository.findAll().size();

        // Update the saque
        Saque updatedSaque = saqueRepository.findById(saque.getId()).get();
        // Disconnect from session so that the updates on updatedSaque are not directly saved in db
        em.detach(updatedSaque);
        updatedSaque.volume(UPDATED_VOLUME).client_account(UPDATED_CLIENT_ACCOUNT).entidade_account(UPDATED_ENTIDADE_ACCOUNT);
        SaqueDTO saqueDTO = saqueMapper.toDto(updatedSaque);

        restSaqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, saqueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(saqueDTO))
            )
            .andExpect(status().isOk());

        // Validate the Saque in the database
        List<Saque> saqueList = saqueRepository.findAll();
        assertThat(saqueList).hasSize(databaseSizeBeforeUpdate);
        Saque testSaque = saqueList.get(saqueList.size() - 1);
        assertThat(testSaque.getVolume()).isEqualByComparingTo(UPDATED_VOLUME);
        assertThat(testSaque.getClient_account()).isEqualTo(UPDATED_CLIENT_ACCOUNT);
        assertThat(testSaque.getEntidade_account()).isEqualTo(UPDATED_ENTIDADE_ACCOUNT);
    }

    @Test
    @Transactional
    void putNonExistingSaque() throws Exception {
        int databaseSizeBeforeUpdate = saqueRepository.findAll().size();
        saque.setId(count.incrementAndGet());

        // Create the Saque
        SaqueDTO saqueDTO = saqueMapper.toDto(saque);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSaqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, saqueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(saqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Saque in the database
        List<Saque> saqueList = saqueRepository.findAll();
        assertThat(saqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSaque() throws Exception {
        int databaseSizeBeforeUpdate = saqueRepository.findAll().size();
        saque.setId(count.incrementAndGet());

        // Create the Saque
        SaqueDTO saqueDTO = saqueMapper.toDto(saque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(saqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Saque in the database
        List<Saque> saqueList = saqueRepository.findAll();
        assertThat(saqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSaque() throws Exception {
        int databaseSizeBeforeUpdate = saqueRepository.findAll().size();
        saque.setId(count.incrementAndGet());

        // Create the Saque
        SaqueDTO saqueDTO = saqueMapper.toDto(saque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaqueMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(saqueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Saque in the database
        List<Saque> saqueList = saqueRepository.findAll();
        assertThat(saqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSaqueWithPatch() throws Exception {
        // Initialize the database
        saqueRepository.saveAndFlush(saque);

        int databaseSizeBeforeUpdate = saqueRepository.findAll().size();

        // Update the saque using partial update
        Saque partialUpdatedSaque = new Saque();
        partialUpdatedSaque.setId(saque.getId());

        partialUpdatedSaque.volume(UPDATED_VOLUME).entidade_account(UPDATED_ENTIDADE_ACCOUNT);

        restSaqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSaque.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSaque))
            )
            .andExpect(status().isOk());

        // Validate the Saque in the database
        List<Saque> saqueList = saqueRepository.findAll();
        assertThat(saqueList).hasSize(databaseSizeBeforeUpdate);
        Saque testSaque = saqueList.get(saqueList.size() - 1);
        assertThat(testSaque.getVolume()).isEqualByComparingTo(UPDATED_VOLUME);
        assertThat(testSaque.getClient_account()).isEqualTo(DEFAULT_CLIENT_ACCOUNT);
        assertThat(testSaque.getEntidade_account()).isEqualTo(UPDATED_ENTIDADE_ACCOUNT);
    }

    @Test
    @Transactional
    void fullUpdateSaqueWithPatch() throws Exception {
        // Initialize the database
        saqueRepository.saveAndFlush(saque);

        int databaseSizeBeforeUpdate = saqueRepository.findAll().size();

        // Update the saque using partial update
        Saque partialUpdatedSaque = new Saque();
        partialUpdatedSaque.setId(saque.getId());

        partialUpdatedSaque.volume(UPDATED_VOLUME).client_account(UPDATED_CLIENT_ACCOUNT).entidade_account(UPDATED_ENTIDADE_ACCOUNT);

        restSaqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSaque.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSaque))
            )
            .andExpect(status().isOk());

        // Validate the Saque in the database
        List<Saque> saqueList = saqueRepository.findAll();
        assertThat(saqueList).hasSize(databaseSizeBeforeUpdate);
        Saque testSaque = saqueList.get(saqueList.size() - 1);
        assertThat(testSaque.getVolume()).isEqualByComparingTo(UPDATED_VOLUME);
        assertThat(testSaque.getClient_account()).isEqualTo(UPDATED_CLIENT_ACCOUNT);
        assertThat(testSaque.getEntidade_account()).isEqualTo(UPDATED_ENTIDADE_ACCOUNT);
    }

    @Test
    @Transactional
    void patchNonExistingSaque() throws Exception {
        int databaseSizeBeforeUpdate = saqueRepository.findAll().size();
        saque.setId(count.incrementAndGet());

        // Create the Saque
        SaqueDTO saqueDTO = saqueMapper.toDto(saque);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSaqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, saqueDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(saqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Saque in the database
        List<Saque> saqueList = saqueRepository.findAll();
        assertThat(saqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSaque() throws Exception {
        int databaseSizeBeforeUpdate = saqueRepository.findAll().size();
        saque.setId(count.incrementAndGet());

        // Create the Saque
        SaqueDTO saqueDTO = saqueMapper.toDto(saque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(saqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Saque in the database
        List<Saque> saqueList = saqueRepository.findAll();
        assertThat(saqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSaque() throws Exception {
        int databaseSizeBeforeUpdate = saqueRepository.findAll().size();
        saque.setId(count.incrementAndGet());

        // Create the Saque
        SaqueDTO saqueDTO = saqueMapper.toDto(saque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaqueMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(saqueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Saque in the database
        List<Saque> saqueList = saqueRepository.findAll();
        assertThat(saqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSaque() throws Exception {
        // Initialize the database
        saqueRepository.saveAndFlush(saque);

        int databaseSizeBeforeDelete = saqueRepository.findAll().size();

        // Delete the saque
        restSaqueMockMvc
            .perform(delete(ENTITY_API_URL_ID, saque.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Saque> saqueList = saqueRepository.findAll();
        assertThat(saqueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
