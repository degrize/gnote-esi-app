package com.esi.gnote.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Salle entity.\n@author The Luis-Borges.
 */
@Entity
@Table(name = "salle")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Salle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "numero_salle", nullable = false)
    private String numeroSalle;

    @Column(name = "nbre_place")
    private Integer nbrePlace;

    @Column(name = "etat")
    private String etat;

    @ManyToMany
    @JoinTable(
        name = "rel_salle__horaire",
        joinColumns = @JoinColumn(name = "salle_id"),
        inverseJoinColumns = @JoinColumn(name = "horaire_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "salles" }, allowSetters = true)
    private Set<Horaire> horaires = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Salle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroSalle() {
        return this.numeroSalle;
    }

    public Salle numeroSalle(String numeroSalle) {
        this.setNumeroSalle(numeroSalle);
        return this;
    }

    public void setNumeroSalle(String numeroSalle) {
        this.numeroSalle = numeroSalle;
    }

    public Integer getNbrePlace() {
        return this.nbrePlace;
    }

    public Salle nbrePlace(Integer nbrePlace) {
        this.setNbrePlace(nbrePlace);
        return this;
    }

    public void setNbrePlace(Integer nbrePlace) {
        this.nbrePlace = nbrePlace;
    }

    public String getEtat() {
        return this.etat;
    }

    public Salle etat(String etat) {
        this.setEtat(etat);
        return this;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Set<Horaire> getHoraires() {
        return this.horaires;
    }

    public void setHoraires(Set<Horaire> horaires) {
        this.horaires = horaires;
    }

    public Salle horaires(Set<Horaire> horaires) {
        this.setHoraires(horaires);
        return this;
    }

    public Salle addHoraire(Horaire horaire) {
        this.horaires.add(horaire);
        horaire.getSalles().add(this);
        return this;
    }

    public Salle removeHoraire(Horaire horaire) {
        this.horaires.remove(horaire);
        horaire.getSalles().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Salle)) {
            return false;
        }
        return id != null && id.equals(((Salle) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Salle{" +
            "id=" + getId() +
            ", numeroSalle='" + getNumeroSalle() + "'" +
            ", nbrePlace=" + getNbrePlace() +
            ", etat='" + getEtat() + "'" +
            "}";
    }
}
