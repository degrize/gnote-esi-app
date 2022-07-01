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
 * The Jury entity.
 */
@Entity
@Table(name = "jury")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Jury implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * The firstname attribute.
     */
    @NotNull
    @Column(name = "president_jury", nullable = false)
    private String presidentJury;

    @ManyToMany
    @JoinTable(
        name = "rel_jury__professeur",
        joinColumns = @JoinColumn(name = "jury_id"),
        inverseJoinColumns = @JoinColumn(name = "professeur_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "etudiants", "classes", "matieres", "inspecteurs", "bulletins", "juries", "demandeInspecteurDES" },
        allowSetters = true
    )
    private Set<Professeur> professeurs = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "juries", "salle", "horaire", "etudiants" }, allowSetters = true)
    private Soutenance soutenance;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Jury id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPresidentJury() {
        return this.presidentJury;
    }

    public Jury presidentJury(String presidentJury) {
        this.setPresidentJury(presidentJury);
        return this;
    }

    public void setPresidentJury(String presidentJury) {
        this.presidentJury = presidentJury;
    }

    public Set<Professeur> getProfesseurs() {
        return this.professeurs;
    }

    public void setProfesseurs(Set<Professeur> professeurs) {
        this.professeurs = professeurs;
    }

    public Jury professeurs(Set<Professeur> professeurs) {
        this.setProfesseurs(professeurs);
        return this;
    }

    public Jury addProfesseur(Professeur professeur) {
        this.professeurs.add(professeur);
        professeur.getJuries().add(this);
        return this;
    }

    public Jury removeProfesseur(Professeur professeur) {
        this.professeurs.remove(professeur);
        professeur.getJuries().remove(this);
        return this;
    }

    public Soutenance getSoutenance() {
        return this.soutenance;
    }

    public void setSoutenance(Soutenance soutenance) {
        this.soutenance = soutenance;
    }

    public Jury soutenance(Soutenance soutenance) {
        this.setSoutenance(soutenance);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Jury)) {
            return false;
        }
        return id != null && id.equals(((Jury) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Jury{" +
            "id=" + getId() +
            ", presidentJury='" + getPresidentJury() + "'" +
            "}";
    }
}
