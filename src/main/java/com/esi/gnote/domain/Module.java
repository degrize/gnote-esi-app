package com.esi.gnote.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Module.
 */
@Entity
@Table(name = "module")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Module implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_ue")
    private String nomUE;

    @OneToMany(mappedBy = "module")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "module", "professeurs", "notes", "classes" }, allowSetters = true)
    private Set<Matiere> matieres = new HashSet<>();

    @ManyToMany(mappedBy = "modules")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "etudiant", "modules", "classes" }, allowSetters = true)
    private Set<Filiere> filieres = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Module id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomUE() {
        return this.nomUE;
    }

    public Module nomUE(String nomUE) {
        this.setNomUE(nomUE);
        return this;
    }

    public void setNomUE(String nomUE) {
        this.nomUE = nomUE;
    }

    public Set<Matiere> getMatieres() {
        return this.matieres;
    }

    public void setMatieres(Set<Matiere> matieres) {
        if (this.matieres != null) {
            this.matieres.forEach(i -> i.setModule(null));
        }
        if (matieres != null) {
            matieres.forEach(i -> i.setModule(this));
        }
        this.matieres = matieres;
    }

    public Module matieres(Set<Matiere> matieres) {
        this.setMatieres(matieres);
        return this;
    }

    public Module addMatiere(Matiere matiere) {
        this.matieres.add(matiere);
        matiere.setModule(this);
        return this;
    }

    public Module removeMatiere(Matiere matiere) {
        this.matieres.remove(matiere);
        matiere.setModule(null);
        return this;
    }

    public Set<Filiere> getFilieres() {
        return this.filieres;
    }

    public void setFilieres(Set<Filiere> filieres) {
        if (this.filieres != null) {
            this.filieres.forEach(i -> i.removeModule(this));
        }
        if (filieres != null) {
            filieres.forEach(i -> i.addModule(this));
        }
        this.filieres = filieres;
    }

    public Module filieres(Set<Filiere> filieres) {
        this.setFilieres(filieres);
        return this;
    }

    public Module addFiliere(Filiere filiere) {
        this.filieres.add(filiere);
        filiere.getModules().add(this);
        return this;
    }

    public Module removeFiliere(Filiere filiere) {
        this.filieres.remove(filiere);
        filiere.getModules().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Module)) {
            return false;
        }
        return id != null && id.equals(((Module) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Module{" +
            "id=" + getId() +
            ", nomUE='" + getNomUE() + "'" +
            "}";
    }
}
