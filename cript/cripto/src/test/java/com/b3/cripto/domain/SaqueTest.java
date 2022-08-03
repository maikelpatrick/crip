package com.b3.cripto.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.b3.cripto.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SaqueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Saque.class);
        Saque saque1 = new Saque();
        saque1.setId(1L);
        Saque saque2 = new Saque();
        saque2.setId(saque1.getId());
        assertThat(saque1).isEqualTo(saque2);
        saque2.setId(2L);
        assertThat(saque1).isNotEqualTo(saque2);
        saque1.setId(null);
        assertThat(saque1).isNotEqualTo(saque2);
    }
}
