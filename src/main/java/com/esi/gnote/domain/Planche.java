package com.esi.gnote.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Planche.
 */
@Entity
@Table(name = "planche")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Planche implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "observation", nullable = false)
    private String observation;

    @ManyToOne
    @JsonIgnoreProperties(value = { "anneeScolaire" }, allowSetters = true)
    private Semestre semestre;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Planche id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservation() {
        return this.observation;
    }

    public Planche observation(String observation) {
        this.setObservation(observation);
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Semestre getSemestre() {
        return this.semestre;
    }

    public void setSemestre(Semestre semestre) {
        this.semestre = semestre;
    }

    public Planche semestre(Semestre semestre) {
        this.setSemestre(semestre);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Planche)) {
            return false;
        }
        return id != null && id.equals(((Planche) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Planche{" +
            "id=" + getId() +
            ", observation='" + getObservation() + "'" +
            "}";
    }
}
