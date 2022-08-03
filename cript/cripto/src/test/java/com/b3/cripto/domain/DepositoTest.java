package com.b3.cripto.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.b3.cripto.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DepositoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Deposito.class);
        Deposito deposito1 = new Deposito();
        deposito1.setId(1L);
        Deposito deposito2 = new Deposito();
        deposito2.setId(deposito1.getId());
        assertThat(deposito1).isEqualTo(deposito2);
        deposito2.setId(2L);
        assertThat(deposito1).isNotEqualTo(deposito2);
        deposito1.setId(null);
        assertThat(deposito1).isNotEqualTo(deposito2);
    }
}
