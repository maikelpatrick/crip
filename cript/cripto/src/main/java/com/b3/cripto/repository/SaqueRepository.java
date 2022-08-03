package com.b3.cripto.repository;

import com.b3.cripto.domain.Saque;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Saque entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaqueRepository extends JpaRepository<Saque, Long> {}
