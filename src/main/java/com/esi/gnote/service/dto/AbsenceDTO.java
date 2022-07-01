package com.esi.gnote.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.esi.gnote.domain.Absence} entity.
 */
@Schema(description = "Absence.\n@author Luis-Borges.")
public class AbsenceDTO implements Serializable {

    private Long id;

    @NotNull
    private String etat;

    private String heureDebut;

    private String heureFin;

    private String justificationEcrit;

    @Lob
    private byte[] justificationNumerique;

    private String justificationNumeriqueContentType;
    private ProfesseurDTO professeur;

    private InspecteurDTO inspecteur;

    private MatiereDTO matiere;

    private EtudiantDTO etudiant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
    }

    public String getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }

    public String getJustificationEcrit() {
        return justificationEcrit;
    }

    public void setJustificationEcrit(String justificationEcrit) {
        this.justificationEcrit = justificationEcrit;
    }

    public byte[] getJustificationNumerique() {
        return justificationNumerique;
    }

    public void setJustificationNumerique(byte[] justificationNumerique) {
        this.justificationNumerique = justificationNumerique;
    }

    public String getJustificationNumeriqueContentType() {
        return justificationNumeriqueContentType;
    }

    public void setJustificationNumeriqueContentType(String justificationNumeriqueContentType) {
        this.justificationNumeriqueContentType = justificationNumeriqueContentType;
    }

    public ProfesseurDTO getProfesseur() {
        return professeur;
    }

    public void setProfesseur(ProfesseurDTO professeur) {
        this.professeur = professeur;
    }

    public InspecteurDTO getInspecteur() {
        return inspecteur;
    }

    public void setInspecteur(InspecteurDTO inspecteur) {
        this.inspecteur = inspecteur;
    }

    public MatiereDTO getMatiere() {
        return matiere;
    }

    public void setMatiere(MatiereDTO matiere) {
        this.matiere = matiere;
    }

    public EtudiantDTO getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(EtudiantDTO etudiant) {
        this.etudiant = etudiant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbsenceDTO)) {
            return false;
        }

        AbsenceDTO absenceDTO = (AbsenceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, absenceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AbsenceDTO{" +
            "id=" + getId() +
            ", etat='" + getEtat() + "'" +
            ", heureDebut='" + getHeureDebut() + "'" +
            ", heureFin='" + getHeureFin() + "'" +
            ", justificationEcrit='" + getJustificationEcrit() + "'" +
            ", justificationNumerique='" + getJustificationNumerique() + "'" +
            ", professeur=" + getProfesseur() +
            ", inspecteur=" + getInspecteur() +
            ", matiere=" + getMatiere() +
            ", etudiant=" + getEtudiant() +
            "}";
    }
}
