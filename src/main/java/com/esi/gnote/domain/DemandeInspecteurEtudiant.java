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
 * A DemandeInspecteurEtudiant.
 */
@Entity
@Table(name = "demande_inspecteur_etudiant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DemandeInspecteurEtudiant implements Serializable {

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
        name = "rel_demande_inspecteur_etudiant__etudiant",
        joinColumns = @JoinColumn(name = "demande_inspecteur_etudiant_id"),
        inverseJoinColumns = @JoinColumn(name = "etudiant_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "classes", "encadreur", "inspecteurs", "professeurs", "soutenances", "notes", "demandeInspecteurEtudiants" },
        allowSetters = true
    )
    private Set<Etudiant> etudiants = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_demande_inspecteur_etudiant__inspecteur",
        joinColumns = @JoinColumn(name = "demande_inspecteur_etudiant_id"),
        inverseJoinColumns = @JoinColumn(name = "inspecteur_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "professeurs", "etudiants", "demandeInspecteurEtudiants", "demandeInspecteurDES" }, allowSetters = true)
    private Set<Inspecteur> inspecteurs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DemandeInspecteurEtudiant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return this.message;
    }

    public DemandeInspecteurEtudiant message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Set<Etudiant> getEtudiants() {
        return this.etudiants;
    }

    public void setEtudiants(Set<Etudiant> etudiants) {
        this.etudiants = etudiants;
    }

    public DemandeInspecteurEtudiant etudiants(Set<Etudiant> etudiants) {
        this.setEtudiants(etudiants);
        return this;
    }

    public DemandeInspecteurEtudiant addEtudiant(Etudiant etudiant) {
        this.etudiants.add(etudiant);
        etudiant.getDemandeInspecteurEtudiants().add(this);
        return this;
    }

    public DemandeInspecteurEtudiant removeEtudiant(Etudiant etudiant) {
        this.etudiants.remove(etudiant);
        etudiant.getDemandeInspecteurEtudiants().remove(this);
        return this;
    }

    public Set<Inspecteur> getInspecteurs() {
        return this.inspecteurs;
    }

    public void setInspecteurs(Set<Inspecteur> inspecteurs) {
        this.inspecteurs = inspecteurs;
    }

    public DemandeInspecteurEtudiant inspecteurs(Set<Inspecteur> inspecteurs) {
        this.setInspecteurs(inspecteurs);
        return this;
    }

    public DemandeInspecteurEtudiant addInspecteur(Inspecteur inspecteur) {
        this.inspecteurs.add(inspecteur);
        inspecteur.getDemandeInspecteurEtudiants().add(this);
        return this;
    }

    public DemandeInspecteurEtudiant removeInspecteur(Inspecteur inspecteur) {
        this.inspecteurs.remove(inspecteur);
        inspecteur.getDemandeInspecteurEtudiants().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DemandeInspecteurEtudiant)) {
            return false;
        }
        return id != null && id.equals(((DemandeInspecteurEtudiant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DemandeInspecteurEtudiant{" +
            "id=" + getId() +
            ", message='" + getMessage() + "'" +
            "}";
    }
}
