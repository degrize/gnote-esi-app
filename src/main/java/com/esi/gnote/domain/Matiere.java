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
 * A Matiere.
 */
@Entity
@Table(name = "matiere")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Matiere implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_ec", nullable = false)
    private String nomEC;

    @NotNull
    @Column(name = "coeff", nullable = false)
    private Integer coeff;

    @ManyToOne
    @JsonIgnoreProperties(value = { "matieres", "filieres" }, allowSetters = true)
    private Module module;

    @ManyToMany(mappedBy = "matieres")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "etudiants", "classes", "matieres", "inspecteurs", "bulletins", "juries", "demandeInspecteurDES" },
        allowSetters = true
    )
    private Set<Professeur> professeurs = new HashSet<>();

    @ManyToMany(mappedBy = "matieres")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "etudiants", "matieres" }, allowSetters = true)
    private Set<Note> notes = new HashSet<>();

    @ManyToMany(mappedBy = "matieres")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "filiere", "matieres", "professeurs", "etudiants" }, allowSetters = true)
    private Set<Classe> classes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Matiere id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomEC() {
        return this.nomEC;
    }

    public Matiere nomEC(String nomEC) {
        this.setNomEC(nomEC);
        return this;
    }

    public void setNomEC(String nomEC) {
        this.nomEC = nomEC;
    }

    public Integer getCoeff() {
        return this.coeff;
    }

    public Matiere coeff(Integer coeff) {
        this.setCoeff(coeff);
        return this;
    }

    public void setCoeff(Integer coeff) {
        this.coeff = coeff;
    }

    public Module getModule() {
        return this.module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Matiere module(Module module) {
        this.setModule(module);
        return this;
    }

    public Set<Professeur> getProfesseurs() {
        return this.professeurs;
    }

    public void setProfesseurs(Set<Professeur> professeurs) {
        if (this.professeurs != null) {
            this.professeurs.forEach(i -> i.removeMatiere(this));
        }
        if (professeurs != null) {
            professeurs.forEach(i -> i.addMatiere(this));
        }
        this.professeurs = professeurs;
    }

    public Matiere professeurs(Set<Professeur> professeurs) {
        this.setProfesseurs(professeurs);
        return this;
    }

    public Matiere addProfesseur(Professeur professeur) {
        this.professeurs.add(professeur);
        professeur.getMatieres().add(this);
        return this;
    }

    public Matiere removeProfesseur(Professeur professeur) {
        this.professeurs.remove(professeur);
        professeur.getMatieres().remove(this);
        return this;
    }

    public Set<Note> getNotes() {
        return this.notes;
    }

    public void setNotes(Set<Note> notes) {
        if (this.notes != null) {
            this.notes.forEach(i -> i.removeMatiere(this));
        }
        if (notes != null) {
            notes.forEach(i -> i.addMatiere(this));
        }
        this.notes = notes;
    }

    public Matiere notes(Set<Note> notes) {
        this.setNotes(notes);
        return this;
    }

    public Matiere addNote(Note note) {
        this.notes.add(note);
        note.getMatieres().add(this);
        return this;
    }

    public Matiere removeNote(Note note) {
        this.notes.remove(note);
        note.getMatieres().remove(this);
        return this;
    }

    public Set<Classe> getClasses() {
        return this.classes;
    }

    public void setClasses(Set<Classe> classes) {
        if (this.classes != null) {
            this.classes.forEach(i -> i.removeMatiere(this));
        }
        if (classes != null) {
            classes.forEach(i -> i.addMatiere(this));
        }
        this.classes = classes;
    }

    public Matiere classes(Set<Classe> classes) {
        this.setClasses(classes);
        return this;
    }

    public Matiere addClasse(Classe classe) {
        this.classes.add(classe);
        classe.getMatieres().add(this);
        return this;
    }

    public Matiere removeClasse(Classe classe) {
        this.classes.remove(classe);
        classe.getMatieres().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Matiere)) {
            return false;
        }
        return id != null && id.equals(((Matiere) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Matiere{" +
            "id=" + getId() +
            ", nomEC='" + getNomEC() + "'" +
            ", coeff=" + getCoeff() +
            "}";
    }
}
