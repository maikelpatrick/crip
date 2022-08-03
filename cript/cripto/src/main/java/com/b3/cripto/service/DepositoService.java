package com.b3.cripto.service;

import com.b3.cripto.service.dto.DepositoDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b3.cripto.domain.Deposito}.
 */
public interface DepositoService {
    /**
     * Save a deposito.
     *
     * @param depositoDTO the entity to save.
     * @return the persisted entity.
     */
    DepositoDTO save(DepositoDTO depositoDTO);

    /**
     * Updates a deposito.
     *
     * @param depositoDTO the entity to update.
     * @return the persisted entity.
     */
    DepositoDTO update(DepositoDTO depositoDTO);

    /**
     * Partially updates a deposito.
     *
     * @param depositoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DepositoDTO> partialUpdate(DepositoDTO depositoDTO);

    /**
     * Get all the depositos.
     *
     * @return the list of entities.
     */
    List<DepositoDTO> findAll();

    /**
     * Get the "id" deposito.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DepositoDTO> findOne(Long id);

    /**
     * Delete the "id" deposito.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
