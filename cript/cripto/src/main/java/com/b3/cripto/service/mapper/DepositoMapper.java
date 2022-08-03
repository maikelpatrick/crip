package com.b3.cripto.service.mapper;

import com.b3.cripto.domain.Deposito;
import com.b3.cripto.service.dto.DepositoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Deposito} and its DTO {@link DepositoDTO}.
 */
@Mapper(componentModel = "spring")
public interface DepositoMapper extends EntityMapper<DepositoDTO, Deposito> {}
