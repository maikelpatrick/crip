package com.b3.cripto.service;

import com.b3.cripto.service.dto.SaqueDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.b3.cripto.domain.Saque}.
 */
public interface SaqueService {
    /**
     * Save a saque.
     *
     * @param saqueDTO the entity to save.
     * @return the persisted entity.
     */
    SaqueDTO save(SaqueDTO saqueDTO);

    /**
     * Updates a saque.
     *
     * @param saqueDTO the entity to update.
     * @return the persisted entity.
     */
    SaqueDTO update(SaqueDTO saqueDTO);

    /**
     * Partially updates a saque.
     *
     * @param saqueDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SaqueDTO> partialUpdate(SaqueDTO saqueDTO);

    /**
     * Get all the saques.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SaqueDTO> findAll(Pageable pageable);

    /**
     * Get the "id" saque.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SaqueDTO> findOne(Long id);

    /**
     * Delete the "id" saque.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
