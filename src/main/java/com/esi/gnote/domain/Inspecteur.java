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
 * The Employee entity.
 */
@Entity
@Table(name = "inspecteur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Inspecteur implements Serializable {

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
    @Column(name = "nom_inspecteur", nullable = false)
    private String nomInspecteur;

    @Column(name = "prenom_inspecteur")
    private String prenomInspecteur;

    @Column(name = "contact_inspecteur")
    private String contactInspecteur;

    @ManyToMany
    @JoinTable(
        name = "rel_inspecteur__professeur",
        joinColumns = @JoinColumn(name = "inspecteur_id"),
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
        name = "rel_inspecteur__etudiant",
        joinColumns = @JoinColumn(name = "inspecteur_id"),
        inverseJoinColumns = @JoinColumn(name = "etudiant_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "classes", "encadreur", "inspecteurs", "professeurs", "soutenances", "notes", "demandeInspecteurEtudiants" },
        allowSetters = true
    )
    private Set<Etudiant> etudiants = new HashSet<>();

    @ManyToMany(mappedBy = "inspecteurs")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "etudiants", "inspecteurs" }, allowSetters = true)
    private Set<DemandeInspecteurEtudiant> demandeInspecteurEtudiants = new HashSet<>();

    @ManyToMany(mappedBy = "inspecteurs")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "professeurs", "inspecteurs" }, allowSetters = true)
    private Set<DemandeInspecteurDE> demandeInspecteurDES = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Inspecteur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomInspecteur() {
        return this.nomInspecteur;
    }

    public Inspecteur nomInspecteur(String nomInspecteur) {
        this.setNomInspecteur(nomInspecteur);
        return this;
    }

    public void setNomInspecteur(String nomInspecteur) {
        this.nomInspecteur = nomInspecteur;
    }

    public String getPrenomInspecteur() {
        return this.prenomInspecteur;
    }

    public Inspecteur prenomInspecteur(String prenomInspecteur) {
        this.setPrenomInspecteur(prenomInspecteur);
        return this;
    }

    public void setPrenomInspecteur(String prenomInspecteur) {
        this.prenomInspecteur = prenomInspecteur;
    }

    public String getContactInspecteur() {
        return this.contactInspecteur;
    }

    public Inspecteur contactInspecteur(String contactInspecteur) {
        this.setContactInspecteur(contactInspecteur);
        return this;
    }

    public void setContactInspecteur(String contactInspecteur) {
        this.contactInspecteur = contactInspecteur;
    }

    public Set<Professeur> getProfesseurs() {
        return this.professeurs;
    }

    public void setProfesseurs(Set<Professeur> professeurs) {
        this.professeurs = professeurs;
    }

    public Inspecteur professeurs(Set<Professeur> professeurs) {
        this.setProfesseurs(professeurs);
        return this;
    }

    public Inspecteur addProfesseur(Professeur professeur) {
        this.professeurs.add(professeur);
        professeur.getInspecteurs().add(this);
        return this;
    }

    public Inspecteur removeProfesseur(Professeur professeur) {
        this.professeurs.remove(professeur);
        professeur.getInspecteurs().remove(this);
        return this;
    }

    public Set<Etudiant> getEtudiants() {
        return this.etudiants;
    }

    public void setEtudiants(Set<Etudiant> etudiants) {
        this.etudiants = etudiants;
    }

    public Inspecteur etudiants(Set<Etudiant> etudiants) {
        this.setEtudiants(etudiants);
        return this;
    }

    public Inspecteur addEtudiant(Etudiant etudiant) {
        this.etudiants.add(etudiant);
        etudiant.getInspecteurs().add(this);
        return this;
    }

    public Inspecteur removeEtudiant(Etudiant etudiant) {
        this.etudiants.remove(etudiant);
        etudiant.getInspecteurs().remove(this);
        return this;
    }

    public Set<DemandeInspecteurEtudiant> getDemandeInspecteurEtudiants() {
        return this.demandeInspecteurEtudiants;
    }

    public void setDemandeInspecteurEtudiants(Set<DemandeInspecteurEtudiant> demandeInspecteurEtudiants) {
        if (this.demandeInspecteurEtudiants != null) {
            this.demandeInspecteurEtudiants.forEach(i -> i.removeInspecteur(this));
        }
        if (demandeInspecteurEtudiants != null) {
            demandeInspecteurEtudiants.forEach(i -> i.addInspecteur(this));
        }
        this.demandeInspecteurEtudiants = demandeInspecteurEtudiants;
    }

    public Inspecteur demandeInspecteurEtudiants(Set<DemandeInspecteurEtudiant> demandeInspecteurEtudiants) {
        this.setDemandeInspecteurEtudiants(demandeInspecteurEtudiants);
        return this;
    }

    public Inspecteur addDemandeInspecteurEtudiant(DemandeInspecteurEtudiant demandeInspecteurEtudiant) {
        this.demandeInspecteurEtudiants.add(demandeInspecteurEtudiant);
        demandeInspecteurEtudiant.getInspecteurs().add(this);
        return this;
    }

    public Inspecteur removeDemandeInspecteurEtudiant(DemandeInspecteurEtudiant demandeInspecteurEtudiant) {
        this.demandeInspecteurEtudiants.remove(demandeInspecteurEtudiant);
        demandeInspecteurEtudiant.getInspecteurs().remove(this);
        return this;
    }

    public Set<DemandeInspecteurDE> getDemandeInspecteurDES() {
        return this.demandeInspecteurDES;
    }

    public void setDemandeInspecteurDES(Set<DemandeInspecteurDE> demandeInspecteurDES) {
        if (this.demandeInspecteurDES != null) {
            this.demandeInspecteurDES.forEach(i -> i.removeInspecteur(this));
        }
        if (demandeInspecteurDES != null) {
            demandeInspecteurDES.forEach(i -> i.addInspecteur(this));
        }
        this.demandeInspecteurDES = demandeInspecteurDES;
    }

    public Inspecteur demandeInspecteurDES(Set<DemandeInspecteurDE> demandeInspecteurDES) {
        this.setDemandeInspecteurDES(demandeInspecteurDES);
        return this;
    }

    public Inspecteur addDemandeInspecteurDE(DemandeInspecteurDE demandeInspecteurDE) {
        this.demandeInspecteurDES.add(demandeInspecteurDE);
        demandeInspecteurDE.getInspecteurs().add(this);
        return this;
    }

    public Inspecteur removeDemandeInspecteurDE(DemandeInspecteurDE demandeInspecteurDE) {
        this.demandeInspecteurDES.remove(demandeInspecteurDE);
        demandeInspecteurDE.getInspecteurs().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Inspecteur)) {
            return false;
        }
        return id != null && id.equals(((Inspecteur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Inspecteur{" +
            "id=" + getId() +
            ", nomInspecteur='" + getNomInspecteur() + "'" +
            ", prenomInspecteur='" + getPrenomInspecteur() + "'" +
            ", contactInspecteur='" + getContactInspecteur() + "'" +
            "}";
    }
}
