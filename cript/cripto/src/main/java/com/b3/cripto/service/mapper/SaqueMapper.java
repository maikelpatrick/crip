package com.b3.cripto.service.mapper;

import com.b3.cripto.domain.Saque;
import com.b3.cripto.service.dto.SaqueDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Saque} and its DTO {@link SaqueDTO}.
 */
@Mapper(componentModel = "spring")
public interface SaqueMapper extends EntityMapper<SaqueDTO, Saque> {}
