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
 * A Professeur.
 */
@Entity
@Table(name = "professeur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Professeur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_prof", nullable = false)
    private String nomProf;

    @NotNull
    @Column(name = "prenom_prof", nullable = false)
    private String prenomProf;

    @Column(name = "contact_prof")
    private String contactProf;

    @ManyToMany
    @JoinTable(
        name = "rel_professeur__etudiant",
        joinColumns = @JoinColumn(name = "professeur_id"),
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
        name = "rel_professeur__classe",
        joinColumns = @JoinColumn(name = "professeur_id"),
        inverseJoinColumns = @JoinColumn(name = "classe_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "filiere", "matieres", "professeurs", "etudiants" }, allowSetters = true)
    private Set<Classe> classes = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_professeur__matiere",
        joinColumns = @JoinColumn(name = "professeur_id"),
        inverseJoinColumns = @JoinColumn(name = "matiere_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "module", "professeurs", "notes", "classes" }, allowSetters = true)
    private Set<Matiere> matieres = new HashSet<>();

    @ManyToMany(mappedBy = "professeurs")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "professeurs", "etudiants", "demandeInspecteurEtudiants", "demandeInspecteurDES" }, allowSetters = true)
    private Set<Inspecteur> inspecteurs = new HashSet<>();

    @ManyToMany(mappedBy = "professeurs")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "etudiant", "semestre", "professeurs" }, allowSetters = true)
    private Set<Bulletin> bulletins = new HashSet<>();

    @ManyToMany(mappedBy = "professeurs")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "professeurs", "soutenance" }, allowSetters = true)
    private Set<Jury> juries = new HashSet<>();

    @ManyToMany(mappedBy = "professeurs")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "professeurs", "inspecteurs" }, allowSetters = true)
    private Set<DemandeInspecteurDE> demandeInspecteurDES = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Professeur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomProf() {
        return this.nomProf;
    }

    public Professeur nomProf(String nomProf) {
        this.setNomProf(nomProf);
        return this;
    }

    public void setNomProf(String nomProf) {
        this.nomProf = nomProf;
    }

    public String getPrenomProf() {
        return this.prenomProf;
    }

    public Professeur prenomProf(String prenomProf) {
        this.setPrenomProf(prenomProf);
        return this;
    }

    public void setPrenomProf(String prenomProf) {
        this.prenomProf = prenomProf;
    }

    public String getContactProf() {
        return this.contactProf;
    }

    public Professeur contactProf(String contactProf) {
        this.setContactProf(contactProf);
        return this;
    }

    public void setContactProf(String contactProf) {
        this.contactProf = contactProf;
    }

    public Set<Etudiant> getEtudiants() {
        return this.etudiants;
    }

    public void setEtudiants(Set<Etudiant> etudiants) {
        this.etudiants = etudiants;
    }

    public Professeur etudiants(Set<Etudiant> etudiants) {
        this.setEtudiants(etudiants);
        return this;
    }

    public Professeur addEtudiant(Etudiant etudiant) {
        this.etudiants.add(etudiant);
        etudiant.getProfesseurs().add(this);
        return this;
    }

    public Professeur removeEtudiant(Etudiant etudiant) {
        this.etudiants.remove(etudiant);
        etudiant.getProfesseurs().remove(this);
        return this;
    }

    public Set<Classe> getClasses() {
        return this.classes;
    }

    public void setClasses(Set<Classe> classes) {
        this.classes = classes;
    }

    public Professeur classes(Set<Classe> classes) {
        this.setClasses(classes);
        return this;
    }

    public Professeur addClasse(Classe classe) {
        this.classes.add(classe);
        classe.getProfesseurs().add(this);
        return this;
    }

    public Professeur removeClasse(Classe classe) {
        this.classes.remove(classe);
        classe.getProfesseurs().remove(this);
        return this;
    }

    public Set<Matiere> getMatieres() {
        return this.matieres;
    }

    public void setMatieres(Set<Matiere> matieres) {
        this.matieres = matieres;
    }

    public Professeur matieres(Set<Matiere> matieres) {
        this.setMatieres(matieres);
        return this;
    }

    public Professeur addMatiere(Matiere matiere) {
        this.matieres.add(matiere);
        matiere.getProfesseurs().add(this);
        return this;
    }

    public Professeur removeMatiere(Matiere matiere) {
        this.matieres.remove(matiere);
        matiere.getProfesseurs().remove(this);
        return this;
    }

    public Set<Inspecteur> getInspecteurs() {
        return this.inspecteurs;
    }

    public void setInspecteurs(Set<Inspecteur> inspecteurs) {
        if (this.inspecteurs != null) {
            this.inspecteurs.forEach(i -> i.removeProfesseur(this));
        }
        if (inspecteurs != null) {
            inspecteurs.forEach(i -> i.addProfesseur(this));
        }
        this.inspecteurs = inspecteurs;
    }

    public Professeur inspecteurs(Set<Inspecteur> inspecteurs) {
        this.setInspecteurs(inspecteurs);
        return this;
    }

    public Professeur addInspecteur(Inspecteur inspecteur) {
        this.inspecteurs.add(inspecteur);
        inspecteur.getProfesseurs().add(this);
        return this;
    }

    public Professeur removeInspecteur(Inspecteur inspecteur) {
        this.inspecteurs.remove(inspecteur);
        inspecteur.getProfesseurs().remove(this);
        return this;
    }

    public Set<Bulletin> getBulletins() {
        return this.bulletins;
    }

    public void setBulletins(Set<Bulletin> bulletins) {
        if (this.bulletins != null) {
            this.bulletins.forEach(i -> i.removeProfesseur(this));
        }
        if (bulletins != null) {
            bulletins.forEach(i -> i.addProfesseur(this));
        }
        this.bulletins = bulletins;
    }

    public Professeur bulletins(Set<Bulletin> bulletins) {
        this.setBulletins(bulletins);
        return this;
    }

    public Professeur addBulletin(Bulletin bulletin) {
        this.bulletins.add(bulletin);
        bulletin.getProfesseurs().add(this);
        return this;
    }

    public Professeur removeBulletin(Bulletin bulletin) {
        this.bulletins.remove(bulletin);
        bulletin.getProfesseurs().remove(this);
        return this;
    }

    public Set<Jury> getJuries() {
        return this.juries;
    }

    public void setJuries(Set<Jury> juries) {
        if (this.juries != null) {
            this.juries.forEach(i -> i.removeProfesseur(this));
        }
        if (juries != null) {
            juries.forEach(i -> i.addProfesseur(this));
        }
        this.juries = juries;
    }

    public Professeur juries(Set<Jury> juries) {
        this.setJuries(juries);
        return this;
    }

    public Professeur addJury(Jury jury) {
        this.juries.add(jury);
        jury.getProfesseurs().add(this);
        return this;
    }

    public Professeur removeJury(Jury jury) {
        this.juries.remove(jury);
        jury.getProfesseurs().remove(this);
        return this;
    }

    public Set<DemandeInspecteurDE> getDemandeInspecteurDES() {
        return this.demandeInspecteurDES;
    }

    public void setDemandeInspecteurDES(Set<DemandeInspecteurDE> demandeInspecteurDES) {
        if (this.demandeInspecteurDES != null) {
            this.demandeInspecteurDES.forEach(i -> i.removeProfesseur(this));
        }
        if (demandeInspecteurDES != null) {
            demandeInspecteurDES.forEach(i -> i.addProfesseur(this));
        }
        this.demandeInspecteurDES = demandeInspecteurDES;
    }

    public Professeur demandeInspecteurDES(Set<DemandeInspecteurDE> demandeInspecteurDES) {
        this.setDemandeInspecteurDES(demandeInspecteurDES);
        return this;
    }

    public Professeur addDemandeInspecteurDE(DemandeInspecteurDE demandeInspecteurDE) {
        this.demandeInspecteurDES.add(demandeInspecteurDE);
        demandeInspecteurDE.getProfesseurs().add(this);
        return this;
    }

    public Professeur removeDemandeInspecteurDE(DemandeInspecteurDE demandeInspecteurDE) {
        this.demandeInspecteurDES.remove(demandeInspecteurDE);
        demandeInspecteurDE.getProfesseurs().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Professeur)) {
            return false;
        }
        return id != null && id.equals(((Professeur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Professeur{" +
            "id=" + getId() +
            ", nomProf='" + getNomProf() + "'" +
            ", prenomProf='" + getPrenomProf() + "'" +
            ", contactProf='" + getContactProf() + "'" +
            "}";
    }
}
