package com.esi.gnote.domain;

import com.esi.gnote.domain.enumeration.TypeSoutenance;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Soutenance.
 */
@Entity
@Table(name = "soutenance")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Soutenance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_sout", nullable = false)
    private TypeSoutenance typeSout;

    @NotNull
    @Column(name = "theme_sout", nullable = false)
    private String themeSout;

    @NotNull
    @Column(name = "note_sout", nullable = false)
    private Double noteSout;

    @OneToMany(mappedBy = "soutenance")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "professeurs", "soutenance" }, allowSetters = true)
    private Set<Jury> juries = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "horaires" }, allowSetters = true)
    private Salle salle;

    @ManyToOne
    @JsonIgnoreProperties(value = { "salles" }, allowSetters = true)
    private Horaire horaire;

    @ManyToMany
    @JoinTable(
        name = "rel_soutenance__etudiant",
        joinColumns = @JoinColumn(name = "soutenance_id"),
        inverseJoinColumns = @JoinColumn(name = "etudiant_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "classes", "encadreur", "inspecteurs", "professeurs", "soutenances", "notes", "demandeInspecteurEtudiants" },
        allowSetters = true
    )
    private Set<Etudiant> etudiants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Soutenance id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeSoutenance getTypeSout() {
        return this.typeSout;
    }

    public Soutenance typeSout(TypeSoutenance typeSout) {
        this.setTypeSout(typeSout);
        return this;
    }

    public void setTypeSout(TypeSoutenance typeSout) {
        this.typeSout = typeSout;
    }

    public String getThemeSout() {
        return this.themeSout;
    }

    public Soutenance themeSout(String themeSout) {
        this.setThemeSout(themeSout);
        return this;
    }

    public void setThemeSout(String themeSout) {
        this.themeSout = themeSout;
    }

    public Double getNoteSout() {
        return this.noteSout;
    }

    public Soutenance noteSout(Double noteSout) {
        this.setNoteSout(noteSout);
        return this;
    }

    public void setNoteSout(Double noteSout) {
        this.noteSout = noteSout;
    }

    public Set<Jury> getJuries() {
        return this.juries;
    }

    public void setJuries(Set<Jury> juries) {
        if (this.juries != null) {
            this.juries.forEach(i -> i.setSoutenance(null));
        }
        if (juries != null) {
            juries.forEach(i -> i.setSoutenance(this));
        }
        this.juries = juries;
    }

    public Soutenance juries(Set<Jury> juries) {
        this.setJuries(juries);
        return this;
    }

    public Soutenance addJury(Jury jury) {
        this.juries.add(jury);
        jury.setSoutenance(this);
        return this;
    }

    public Soutenance removeJury(Jury jury) {
        this.juries.remove(jury);
        jury.setSoutenance(null);
        return this;
    }

    public Salle getSalle() {
        return this.salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

    public Soutenance salle(Salle salle) {
        this.setSalle(salle);
        return this;
    }

    public Horaire getHoraire() {
        return this.horaire;
    }

    public void setHoraire(Horaire horaire) {
        this.horaire = horaire;
    }

    public Soutenance horaire(Horaire horaire) {
        this.setHoraire(horaire);
        return this;
    }

    public Set<Etudiant> getEtudiants() {
        return this.etudiants;
    }

    public void setEtudiants(Set<Etudiant> etudiants) {
        this.etudiants = etudiants;
    }

    public Soutenance etudiants(Set<Etudiant> etudiants) {
        this.setEtudiants(etudiants);
        return this;
    }

    public Soutenance addEtudiant(Etudiant etudiant) {
        this.etudiants.add(etudiant);
        etudiant.getSoutenances().add(this);
        return this;
    }

    public Soutenance removeEtudiant(Etudiant etudiant) {
        this.etudiants.remove(etudiant);
        etudiant.getSoutenances().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Soutenance)) {
            return false;
        }
        return id != null && id.equals(((Soutenance) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Soutenance{" +
            "id=" + getId() +
            ", typeSout='" + getTypeSout() + "'" +
            ", themeSout='" + getThemeSout() + "'" +
            ", noteSout=" + getNoteSout() +
            "}";
    }
}
