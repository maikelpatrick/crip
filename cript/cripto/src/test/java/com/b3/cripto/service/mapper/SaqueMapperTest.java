package com.b3.cripto.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SaqueMapperTest {

    private SaqueMapper saqueMapper;

    @BeforeEach
    public void setUp() {
        saqueMapper = new SaqueMapperImpl();
    }
}
