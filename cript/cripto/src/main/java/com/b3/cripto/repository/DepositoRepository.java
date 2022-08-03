package com.b3.cripto.repository;

import com.b3.cripto.domain.Deposito;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Deposito entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepositoRepository extends JpaRepository<Deposito, Long> {}
