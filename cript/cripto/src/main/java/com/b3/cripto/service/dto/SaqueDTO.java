package com.b3.cripto.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.b3.cripto.domain.Saque} entity.
 */
public class SaqueDTO implements Serializable {

    private Long id;

    private BigDecimal volume;

    private String client_account;

    private String entidade_account;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public String getClient_account() {
        return client_account;
    }

    public void setClient_account(String client_account) {
        this.client_account = client_account;
    }

    public String getEntidade_account() {
        return entidade_account;
    }

    public void setEntidade_account(String entidade_account) {
        this.entidade_account = entidade_account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SaqueDTO)) {
            return false;
        }

        SaqueDTO saqueDTO = (SaqueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, saqueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SaqueDTO{" +
            "id=" + getId() +
            ", volume=" + getVolume() +
            ", client_account='" + getClient_account() + "'" +
            ", entidade_account='" + getEntidade_account() + "'" +
            "}";
    }
}
