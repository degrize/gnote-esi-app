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
 * A DemandeInspecteurDE.
 */
@Entity
@Table(name = "demande_inspecteur_de")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DemandeInspecteurDE implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "message", nullable = false)
    private String message;

    @ManyToMany
    @JoinTable(
        name = "rel_demande_inspecteur_de__professeur",
        joinColumns = @JoinColumn(name = "demande_inspecteur_de_id"),
        inverseJoinColumns = @JoinColumn(name = "professeur_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "etudiants", "classes", "matieres", "inspecteurs", "bulletins", "juries", "demandeInspecteurDES" },
        allowSetters = true
    )
    private Set<Professeur> professeurs = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_demande_inspecteur_de__inspecteur",
        joinColumns = @JoinColumn(name = "demande_inspecteur_de_id"),
        inverseJoinColumns = @JoinColumn(name = "inspecteur_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "professeurs", "etudiants", "demandeInspecteurEtudiants", "demandeInspecteurDES" }, allowSetters = true)
    private Set<Inspecteur> inspecteurs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DemandeInspecteurDE id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return this.message;
    }

    public DemandeInspecteurDE message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Set<Professeur> getProfesseurs() {
        return this.professeurs;
    }

    public void setProfesseurs(Set<Professeur> professeurs) {
        this.professeurs = professeurs;
    }

    public DemandeInspecteurDE professeurs(Set<Professeur> professeurs) {
        this.setProfesseurs(professeurs);
        return this;
    }

    public DemandeInspecteurDE addProfesseur(Professeur professeur) {
        this.professeurs.add(professeur);
        professeur.getDemandeInspecteurDES().add(this);
        return this;
    }

    public DemandeInspecteurDE removeProfesseur(Professeur professeur) {
        this.professeurs.remove(professeur);
        professeur.getDemandeInspecteurDES().remove(this);
        return this;
    }

    public Set<Inspecteur> getInspecteurs() {
        return this.inspecteurs;
    }

    public void setInspecteurs(Set<Inspecteur> inspecteurs) {
        this.inspecteurs = inspecteurs;
    }

    public DemandeInspecteurDE inspecteurs(Set<Inspecteur> inspecteurs) {
        this.setInspecteurs(inspecteurs);
        return this;
    }

    public DemandeInspecteurDE addInspecteur(Inspecteur inspecteur) {
        this.inspecteurs.add(inspecteur);
        inspecteur.getDemandeInspecteurDES().add(this);
        return this;
    }

    public DemandeInspecteurDE removeInspecteur(Inspecteur inspecteur) {
        this.inspecteurs.remove(inspecteur);
        inspecteur.getDemandeInspecteurDES().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DemandeInspecteurDE)) {
            return false;
        }
        return id != null && id.equals(((DemandeInspecteurDE) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DemandeInspecteurDE{" +
            "id=" + getId() +
            ", message='" + getMessage() + "'" +
            "}";
    }
}
