package com.b3.cripto.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Saque.
 */
@Entity
@Table(name = "saque")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Saque implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "volume", precision = 21, scale = 2)
    private BigDecimal volume;

    @Column(name = "client_account")
    private String client_account;

    @Column(name = "entidade_account")
    private String entidade_account;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Saque id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getVolume() {
        return this.volume;
    }

    public Saque volume(BigDecimal volume) {
        this.setVolume(volume);
        return this;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public String getClient_account() {
        return this.client_account;
    }

    public Saque client_account(String client_account) {
        this.setClient_account(client_account);
        return this;
    }

    public void setClient_account(String client_account) {
        this.client_account = client_account;
    }

    public String getEntidade_account() {
        return this.entidade_account;
    }

    public Saque entidade_account(String entidade_account) {
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
        if (!(o instanceof Saque)) {
            return false;
        }
        return id != null && id.equals(((Saque) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Saque{" +
            "id=" + getId() +
            ", volume=" + getVolume() +
            ", client_account='" + getClient_account() + "'" +
            ", entidade_account='" + getEntidade_account() + "'" +
            "}";
    }
}
