package com.esi.gnote.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Absence.\n@author Luis-Borges.
 */
@Entity
@Table(name = "absence")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Absence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "etat", nullable = false)
    private String etat;

    @Column(name = "heure_debut")
    private String heureDebut;

    @Column(name = "heure_fin")
    private String heureFin;

    @Column(name = "justification_ecrit")
    private String justificationEcrit;

    @Lob
    @Column(name = "justification_numerique")
    private byte[] justificationNumerique;

    @Column(name = "justification_numerique_content_type")
    private String justificationNumeriqueContentType;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "etudiants", "classes", "matieres", "inspecteurs", "bulletins", "juries", "demandeInspecteurDES" },
        allowSetters = true
    )
    private Professeur professeur;

    @ManyToOne
    @JsonIgnoreProperties(value = { "professeurs", "etudiants", "demandeInspecteurEtudiants", "demandeInspecteurDES" }, allowSetters = true)
    private Inspecteur inspecteur;

    @ManyToOne
    @JsonIgnoreProperties(value = { "module", "professeurs", "notes", "classes" }, allowSetters = true)
    private Matiere matiere;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "classes", "encadreur", "inspecteurs", "professeurs", "soutenances", "notes", "demandeInspecteurEtudiants" },
        allowSetters = true
    )
    private Etudiant etudiant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Absence id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEtat() {
        return this.etat;
    }

    public Absence etat(String etat) {
        this.setEtat(etat);
        return this;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getHeureDebut() {
        return this.heureDebut;
    }

    public Absence heureDebut(String heureDebut) {
        this.setHeureDebut(heureDebut);
        return this;
    }

    public void setHeureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
    }

    public String getHeureFin() {
        return this.heureFin;
    }

    public Absence heureFin(String heureFin) {
        this.setHeureFin(heureFin);
        return this;
    }

    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }

    public String getJustificationEcrit() {
        return this.justificationEcrit;
    }

    public Absence justificationEcrit(String justificationEcrit) {
        this.setJustificationEcrit(justificationEcrit);
        return this;
    }

    public void setJustificationEcrit(String justificationEcrit) {
        this.justificationEcrit = justificationEcrit;
    }

    public byte[] getJustificationNumerique() {
        return this.justificationNumerique;
    }

    public Absence justificationNumerique(byte[] justificationNumerique) {
        this.setJustificationNumerique(justificationNumerique);
        return this;
    }

    public void setJustificationNumerique(byte[] justificationNumerique) {
        this.justificationNumerique = justificationNumerique;
    }

    public String getJustificationNumeriqueContentType() {
        return this.justificationNumeriqueContentType;
    }

    public Absence justificationNumeriqueContentType(String justificationNumeriqueContentType) {
        this.justificationNumeriqueContentType = justificationNumeriqueContentType;
        return this;
    }

    public void setJustificationNumeriqueContentType(String justificationNumeriqueContentType) {
        this.justificationNumeriqueContentType = justificationNumeriqueContentType;
    }

    public Professeur getProfesseur() {
        return this.professeur;
    }

    public void setProfesseur(Professeur professeur) {
        this.professeur = professeur;
    }

    public Absence professeur(Professeur professeur) {
        this.setProfesseur(professeur);
        return this;
    }

    public Inspecteur getInspecteur() {
        return this.inspecteur;
    }

    public void setInspecteur(Inspecteur inspecteur) {
        this.inspecteur = inspecteur;
    }

    public Absence inspecteur(Inspecteur inspecteur) {
        this.setInspecteur(inspecteur);
        return this;
    }

    public Matiere getMatiere() {
        return this.matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    public Absence matiere(Matiere matiere) {
        this.setMatiere(matiere);
        return this;
    }

    public Etudiant getEtudiant() {
        return this.etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public Absence etudiant(Etudiant etudiant) {
        this.setEtudiant(etudiant);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Absence)) {
            return false;
        }
        return id != null && id.equals(((Absence) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Absence{" +
            "id=" + getId() +
            ", etat='" + getEtat() + "'" +
            ", heureDebut='" + getHeureDebut() + "'" +
            ", heureFin='" + getHeureFin() + "'" +
            ", justificationEcrit='" + getJustificationEcrit() + "'" +
            ", justificationNumerique='" + getJustificationNumerique() + "'" +
            ", justificationNumeriqueContentType='" + getJustificationNumeriqueContentType() + "'" +
            "}";
    }
}
