package com.b3.cripto.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DepositoMapperTest {

    private DepositoMapper depositoMapper;

    @BeforeEach
    public void setUp() {
        depositoMapper = new DepositoMapperImpl();
    }
}
