package com.b3.cripto.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.b3.cripto.domain.Deposito} entity.
 */
public class DepositoDTO implements Serializable {

    private Long id;

    private BigDecimal volume;

    private UUID id_deposito;

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

    public UUID getId_deposito() {
        return id_deposito;
    }

    public void setId_deposito(UUID id_deposito) {
        this.id_deposito = id_deposito;
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
        if (!(o instanceof DepositoDTO)) {
            return false;
        }

        DepositoDTO depositoDTO = (DepositoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, depositoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepositoDTO{" +
            "id=" + getId() +
            ", volume=" + getVolume() +
            ", id_deposito='" + getId_deposito() + "'" +
            ", client_account='" + getClient_account() + "'" +
            ", entidade_account='" + getEntidade_account() + "'" +
            "}";
    }
}
