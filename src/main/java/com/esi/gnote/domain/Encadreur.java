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
 * A Encadreur.
 */
@Entity
@Table(name = "encadreur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Encadreur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_enc", nullable = false)
    private String nomEnc;

    @Column(name = "prenoms_enc")
    private String prenomsEnc;

    @Column(name = "email_enc")
    private String emailEnc;

    @OneToMany(mappedBy = "encadreur")
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

    public Encadreur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomEnc() {
        return this.nomEnc;
    }

    public Encadreur nomEnc(String nomEnc) {
        this.setNomEnc(nomEnc);
        return this;
    }

    public void setNomEnc(String nomEnc) {
        this.nomEnc = nomEnc;
    }

    public String getPrenomsEnc() {
        return this.prenomsEnc;
    }

    public Encadreur prenomsEnc(String prenomsEnc) {
        this.setPrenomsEnc(prenomsEnc);
        return this;
    }

    public void setPrenomsEnc(String prenomsEnc) {
        this.prenomsEnc = prenomsEnc;
    }

    public String getEmailEnc() {
        return this.emailEnc;
    }

    public Encadreur emailEnc(String emailEnc) {
        this.setEmailEnc(emailEnc);
        return this;
    }

    public void setEmailEnc(String emailEnc) {
        this.emailEnc = emailEnc;
    }

    public Set<Etudiant> getEtudiants() {
        return this.etudiants;
    }

    public void setEtudiants(Set<Etudiant> etudiants) {
        if (this.etudiants != null) {
            this.etudiants.forEach(i -> i.setEncadreur(null));
        }
        if (etudiants != null) {
            etudiants.forEach(i -> i.setEncadreur(this));
        }
        this.etudiants = etudiants;
    }

    public Encadreur etudiants(Set<Etudiant> etudiants) {
        this.setEtudiants(etudiants);
        return this;
    }

    public Encadreur addEtudiant(Etudiant etudiant) {
        this.etudiants.add(etudiant);
        etudiant.setEncadreur(this);
        return this;
    }

    public Encadreur removeEtudiant(Etudiant etudiant) {
        this.etudiants.remove(etudiant);
        etudiant.setEncadreur(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Encadreur)) {
            return false;
        }
        return id != null && id.equals(((Encadreur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Encadreur{" +
            "id=" + getId() +
            ", nomEnc='" + getNomEnc() + "'" +
            ", prenomsEnc='" + getPrenomsEnc() + "'" +
            ", emailEnc='" + getEmailEnc() + "'" +
            "}";
    }
}
