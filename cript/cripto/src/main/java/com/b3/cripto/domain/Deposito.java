package com.b3.cripto.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Deposito.
 */
@Entity
@Table(name = "deposito")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Deposito implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "volume", precision = 21, scale = 2)
    private BigDecimal volume;

    @Column(name = "id_deposito")
    private UUID id_deposito;

    @Column(name = "client_account")
    private String client_account;

    @Column(name = "entidade_account")
    private String entidade_account;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Deposito id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getVolume() {
        return this.volume;
    }

    public Deposito volume(BigDecimal volume) {
        this.setVolume(volume);
        return this;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public UUID getId_deposito() {
        return this.id_deposito;
    }

    public Deposito id_deposito(UUID id_deposito) {
        this.setId_deposito(id_deposito);
        return this;
    }

    public void setId_deposito(UUID id_deposito) {
        this.id_deposito = id_deposito;
    }

    public String getClient_account() {
        return this.client_account;
    }

    public Deposito client_account(String client_account) {
        this.setClient_account(client_account);
        return this;
    }

    public void setClient_account(String client_account) {
        this.client_account = client_account;
    }

    public String getEntidade_account() {
        return this.entidade_account;
    }

    public Deposito entidade_account(String entidade_account) {
        this.setEntidade_account(entidade_account);
        return this;
    }

    public void setEntidade_account(String entidade_account) {
        this.entidade_account = entidade_account;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Deposito)) {
            return false;
        }
        return id != null && id.equals(((Deposito) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Deposito{" +
            "id=" + getId() +
            ", volume=" + getVolume() +
            ", id_deposito='" + getId_deposito() + "'" +
            ", client_account='" + getClient_account() + "'" +
            ", entidade_account='" + getEntidade_account() + "'" +
            "}";
    }
}
