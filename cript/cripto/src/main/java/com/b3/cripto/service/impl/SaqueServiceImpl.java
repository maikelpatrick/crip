package com.b3.cripto.service.impl;

import com.b3.cripto.domain.Saque;
import com.b3.cripto.repository.SaqueRepository;
import com.b3.cripto.service.SaqueService;
import com.b3.cripto.service.dto.SaqueDTO;
import com.b3.cripto.service.mapper.SaqueMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Saque}.
 */
@Service
@Transactional
public class SaqueServiceImpl implements SaqueService {

    private final Logger log = LoggerFactory.getLogger(SaqueServiceImpl.class);

    private final SaqueRepository saqueRepository;

    private final SaqueMapper saqueMapper;

    public SaqueServiceImpl(SaqueRepository saqueRepository, SaqueMapper saqueMapper) {
        this.saqueRepository = saqueRepository;
        this.saqueMapper = saqueMapper;
    }

    @Override
    public SaqueDTO save(SaqueDTO saqueDTO) {
        log.debug("Request to save Saque : {}", saqueDTO);
        Saque saque = saqueMapper.toEntity(saqueDTO);
        saque = saqueRepository.save(saque);
        return saqueMapper.toDto(saque);
    }

    @Override
    public SaqueDTO update(SaqueDTO saqueDTO) {
        log.debug("Request to save Saque : {}", saqueDTO);
        Saque saque = saqueMapper.toEntity(saqueDTO);
        saque = saqueRepository.save(saque);
        return saqueMapper.toDto(saque);
    }

    @Override
    public Optional<SaqueDTO> partialUpdate(SaqueDTO saqueDTO) {
        log.debug("Request to partially update Saque : {}", saqueDTO);

        return saqueRepository
            .findById(saqueDTO.getId())
            .map(existingSaque -> {
                saqueMapper.partialUpdate(existingSaque, saqueDTO);

                return existingSaque;
            })
            .map(saqueRepository::save)
            .map(saqueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SaqueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Saques");
        return saqueRepository.findAll(pageable).map(saqueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SaqueDTO> findOne(Long id) {
        log.debug("Request to get Saque : {}", id);
        return saqueRepository.findById(id).map(saqueMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Saque : {}", id);
        saqueRepository.deleteById(id);
    }
}
