package com.esi.gnote.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.esi.gnote.domain.Bulletin} entity.
 */
public class BulletinDTO implements Serializable {

    private Long id;

    @NotNull
    private String signatureDG;

    @NotNull
    private String observation;

    private EtudiantDTO etudiant;

    private SemestreDTO semestre;

    private Set<ProfesseurDTO> professeurs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSignatureDG() {
        return signatureDG;
    }

    public void setSignatureDG(String signatureDG) {
        this.signatureDG = signatureDG;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public EtudiantDTO getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(EtudiantDTO etudiant) {
        this.etudiant = etudiant;
    }

    public SemestreDTO getSemestre() {
        return semestre;
    }

    public void setSemestre(SemestreDTO semestre) {
        this.semestre = semestre;
    }

    public Set<ProfesseurDTO> getProfesseurs() {
        return professeurs;
    }

    public void setProfesseurs(Set<ProfesseurDTO> professeurs) {
        this.professeurs = professeurs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BulletinDTO)) {
            return false;
        }

        BulletinDTO bulletinDTO = (BulletinDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bulletinDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BulletinDTO{" +
            "id=" + getId() +
            ", signatureDG='" + getSignatureDG() + "'" +
            ", observation='" + getObservation() + "'" +
            ", etudiant=" + getEtudiant() +
            ", semestre=" + getSemestre() +
            ", professeurs=" + getProfesseurs() +
            "}";
    }
}
