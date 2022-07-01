package com.esi.gnote.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * not an ignored comment
 */
@Entity
@Table(name = "horaire")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Horaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date_sout", nullable = false)
    private LocalDate dateSout;

    @Column(name = "date_effet")
    private LocalDate dateEffet;

    @ManyToMany(mappedBy = "horaires")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "horaires" }, allowSetters = true)
    private Set<Salle> salles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Horaire id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateSout() {
        return this.dateSout;
    }

    public Horaire dateSout(LocalDate dateSout) {
        this.setDateSout(dateSout);
        return this;
    }

    public void setDateSout(LocalDate dateSout) {
        this.dateSout = dateSout;
    }

    public LocalDate getDateEffet() {
        return this.dateEffet;
    }

    public Horaire dateEffet(LocalDate dateEffet) {
        this.setDateEffet(dateEffet);
        return this;
    }

    public void setDateEffet(LocalDate dateEffet) {
        this.dateEffet = dateEffet;
    }

    public Set<Salle> getSalles() {
        return this.salles;
    }

    public void setSalles(Set<Salle> salles) {
        if (this.salles != null) {
            this.salles.forEach(i -> i.removeHoraire(this));
        }
        if (salles != null) {
            salles.forEach(i -> i.addHoraire(this));
        }
        this.salles = salles;
    }

    public Horaire salles(Set<Salle> salles) {
        this.setSalles(salles);
        return this;
    }

    public Horaire addSalle(Salle salle) {
        this.salles.add(salle);
        salle.getHoraires().add(this);
        return this;
    }

    public Horaire removeSalle(Salle salle) {
        this.salles.remove(salle);
        salle.getHoraires().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Horaire)) {
            return false;
        }
        return id != null && id.equals(((Horaire) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Horaire{" +
            "id=" + getId() +
            ", dateSout='" + getDateSout() + "'" +
            ", dateEffet='" + getDateEffet() + "'" +
            "}";
    }
}
