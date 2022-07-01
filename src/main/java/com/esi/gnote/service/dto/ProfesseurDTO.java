package com.esi.gnote.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.esi.gnote.domain.Professeur} entity.
 */
public class ProfesseurDTO implements Serializable {

    private Long id;

    @NotNull
    private String nomProf;

    @NotNull
    private String prenomProf;

    private String contactProf;

    private Set<EtudiantDTO> etudiants = new HashSet<>();

    private Set<ClasseDTO> classes = new HashSet<>();

    private Set<MatiereDTO> matieres = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomProf() {
        return nomProf;
    }

    public void setNomProf(String nomProf) {
        this.nomProf = nomProf;
    }

    public String getPrenomProf() {
        return prenomProf;
    }

    public void setPrenomProf(String prenomProf) {
        this.prenomProf = prenomProf;
    }

    public String getContactProf() {
        return contactProf;
    }

    public void setContactProf(String contactProf) {
        this.contactProf = contactProf;
    }

    public Set<EtudiantDTO> getEtudiants() {
        return etudiants;
    }

    public void setEtudiants(Set<EtudiantDTO> etudiants) {
        this.etudiants = etudiants;
    }

    public Set<ClasseDTO> getClasses() {
        return classes;
    }

    public void setClasses(Set<ClasseDTO> classes) {
        this.classes = classes;
    }

    public Set<MatiereDTO> getMatieres() {
        return matieres;
    }

    public void setMatieres(Set<MatiereDTO> matieres) {
        this.matieres = matieres;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfesseurDTO)) {
            return false;
        }

        ProfesseurDTO professeurDTO = (ProfesseurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, professeurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfesseurDTO{" +
            "id=" + getId() +
            ", nomProf='" + getNomProf() + "'" +
            ", prenomProf='" + getPrenomProf() + "'" +
            ", contactProf='" + getContactProf() + "'" +
            ", etudiants=" + getEtudiants() +
            ", classes=" + getClasses() +
            ", matieres=" + getMatieres() +
            "}";
    }
}
