package com.b3.cripto.service.impl;

import com.b3.cripto.domain.Deposito;
import com.b3.cripto.repository.DepositoRepository;
import com.b3.cripto.service.DepositoService;
import com.b3.cripto.service.dto.DepositoDTO;
import com.b3.cripto.service.mapper.DepositoMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Deposito}.
 */
@Service
@Transactional
public class DepositoServiceImpl implements DepositoService {

    private final Logger log = LoggerFactory.getLogger(DepositoServiceImpl.class);

    private final DepositoRepository depositoRepository;

    private final DepositoMapper depositoMapper;

    public DepositoServiceImpl(DepositoRepository depositoRepository, DepositoMapper depositoMapper) {
        this.depositoRepository = depositoRepository;
        this.depositoMapper = depositoMapper;
    }

    @Override
    public DepositoDTO save(DepositoDTO depositoDTO) {
        log.debug("Request to save Deposito : {}", depositoDTO);
        Deposito deposito = depositoMapper.toEntity(depositoDTO);
        deposito = depositoRepository.save(deposito);
        return depositoMapper.toDto(deposito);
    }

    @Override
    public DepositoDTO update(DepositoDTO depositoDTO) {
        log.debug("Request to save Deposito : {}", depositoDTO);
        Deposito deposito = depositoMapper.toEntity(depositoDTO);
        deposito = depositoRepository.save(deposito);
        return depositoMapper.toDto(deposito);
    }

    @Override
    public Optional<DepositoDTO> partialUpdate(DepositoDTO depositoDTO) {
        log.debug("Request to partially update Deposito : {}", depositoDTO);

        return depositoRepository
            .findById(depositoDTO.getId())
            .map(existingDeposito -> {
                depositoMapper.partialUpdate(existingDeposito, depositoDTO);

                return existingDeposito;
            })
            .map(depositoRepository::save)
            .map(depositoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepositoDTO> findAll() {
        log.debug("Request to get all Depositos");
        return depositoRepository.findAll().stream().map(depositoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DepositoDTO> findOne(Long id) {
        log.debug("Request to get Deposito : {}", id);
        return depositoRepository.findById(id).map(depositoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Deposito : {}", id);
        depositoRepository.deleteById(id);
    }
}
