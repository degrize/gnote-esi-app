package com.esi.gnote.service.dto;

import com.esi.gnote.domain.enumeration.TypeSoutenance;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.esi.gnote.domain.Soutenance} entity.
 */
public class SoutenanceDTO implements Serializable {

    private Long id;

    @NotNull
    private TypeSoutenance typeSout;

    @NotNull
    private String themeSout;

    @NotNull
    private Double noteSout;

    private SalleDTO salle;

    private HoraireDTO horaire;

    private Set<EtudiantDTO> etudiants = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeSoutenance getTypeSout() {
        return typeSout;
    }

    public void setTypeSout(TypeSoutenance typeSout) {
        this.typeSout = typeSout;
    }

    public String getThemeSout() {
        return themeSout;
    }

    public void setThemeSout(String themeSout) {
        this.themeSout = themeSout;
    }

    public Double getNoteSout() {
        return noteSout;
    }

    public void setNoteSout(Double noteSout) {
        this.noteSout = noteSout;
    }

    public SalleDTO getSalle() {
        return salle;
    }

    public void setSalle(SalleDTO salle) {
        this.salle = salle;
    }

    public HoraireDTO getHoraire() {
        return horaire;
    }

    public void setHoraire(HoraireDTO horaire) {
        this.horaire = horaire;
    }

    public Set<EtudiantDTO> getEtudiants() {
        return etudiants;
    }

    public void setEtudiants(Set<EtudiantDTO> etudiants) {
        this.etudiants = etudiants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SoutenanceDTO)) {
            return false;
        }

        SoutenanceDTO soutenanceDTO = (SoutenanceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, soutenanceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SoutenanceDTO{" +
            "id=" + getId() +
            ", typeSout='" + getTypeSout() + "'" +
            ", themeSout='" + getThemeSout() + "'" +
            ", noteSout=" + getNoteSout() +
            ", salle=" + getSalle() +
            ", horaire=" + getHoraire() +
            ", etudiants=" + getEtudiants() +
            "}";
    }
}
