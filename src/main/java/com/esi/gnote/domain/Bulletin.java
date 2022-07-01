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
 * A Bulletin.
 */
@Entity
@Table(name = "bulletin")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Bulletin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "signature_dg", nullable = false)
    private String signatureDG;

    @NotNull
    @Column(name = "observation", nullable = false)
    private String observation;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "classes", "encadreur", "inspecteurs", "professeurs", "soutenances", "notes", "demandeInspecteurEtudiants" },
        allowSetters = true
    )
    private Etudiant etudiant;

    @ManyToOne
    @JsonIgnoreProperties(value = { "anneeScolaire" }, allowSetters = true)
    private Semestre semestre;

    @ManyToMany
    @JoinTable(
        name = "rel_bulletin__professeur",
        joinColumns = @JoinColumn(name = "bulletin_id"),
        inverseJoinColumns = @JoinColumn(name = "professeur_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "etudiants", "classes", "matieres", "inspecteurs", "bulletins", "juries", "demandeInspecteurDES" },
        allowSetters = true
    )
    private Set<Professeur> professeurs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Bulletin id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSignatureDG() {
        return this.signatureDG;
    }

    public Bulletin signatureDG(String signatureDG) {
        this.setSignatureDG(signatureDG);
        return this;
    }

    public void setSignatureDG(String signatureDG) {
        this.signatureDG = signatureDG;
    }

    public String getObservation() {
        return this.observation;
    }

    public Bulletin observation(String observation) {
        this.setObservation(observation);
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Etudiant getEtudiant() {
        return this.etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public Bulletin etudiant(Etudiant etudiant) {
        this.setEtudiant(etudiant);
        return this;
    }

    public Semestre getSemestre() {
        return this.semestre;
    }

    public void setSemestre(Semestre semestre) {
        this.semestre = semestre;
    }

    public Bulletin semestre(Semestre semestre) {
        this.setSemestre(semestre);
        return this;
    }

    public Set<Professeur> getProfesseurs() {
        return this.professeurs;
    }

    public void setProfesseurs(Set<Professeur> professeurs) {
        this.professeurs = professeurs;
    }

    public Bulletin professeurs(Set<Professeur> professeurs) {
        this.setProfesseurs(professeurs);
        return this;
    }

    public Bulletin addProfesseur(Professeur professeur) {
        this.professeurs.add(professeur);
        professeur.getBulletins().add(this);
        return this;
    }

    public Bulletin removeProfesseur(Professeur professeur) {
        this.professeurs.remove(professeur);
        professeur.getBulletins().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bulletin)) {
            return false;
        }
        return id != null && id.equals(((Bulletin) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bulletin{" +
            "id=" + getId() +
            ", signatureDG='" + getSignatureDG() + "'" +
            ", observation='" + getObservation() + "'" +
            "}";
    }
}
