package com.esi.gnote.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * not an ignored comment
 */
@Entity
@Table(name = "semestre")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Semestre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_semestre", nullable = false)
    private String nomSemestre;

    @ManyToOne
    private AnneeScolaire anneeScolaire;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Semestre id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomSemestre() {
        return this.nomSemestre;
    }

    public Semestre nomSemestre(String nomSemestre) {
        this.setNomSemestre(nomSemestre);
        return this;
    }

    public void setNomSemestre(String nomSemestre) {
        this.nomSemestre = nomSemestre;
    }

    public AnneeScolaire getAnneeScolaire() {
        return this.anneeScolaire;
    }

    public void setAnneeScolaire(AnneeScolaire anneeScolaire) {
        this.anneeScolaire = anneeScolaire;
    }

    public Semestre anneeScolaire(AnneeScolaire anneeScolaire) {
        this.setAnneeScolaire(anneeScolaire);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Semestre)) {
            return false;
        }
        return id != null && id.equals(((Semestre) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Semestre{" +
            "id=" + getId() +
            ", nomSemestre='" + getNomSemestre() + "'" +
            "}";
    }
}
