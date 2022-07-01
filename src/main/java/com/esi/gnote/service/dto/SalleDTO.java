package com.esi.gnote.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.esi.gnote.domain.Salle} entity.
 */
@Schema(description = "Salle entity.\n@author The Luis-Borges.")
public class SalleDTO implements Serializable {

    private Long id;

    @NotNull
    private String numeroSalle;

    private Integer nbrePlace;

    private String etat;

    private Set<HoraireDTO> horaires = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroSalle() {
        return numeroSalle;
    }

    public void setNumeroSalle(String numeroSalle) {
        this.numeroSalle = numeroSalle;
    }

    public Integer getNbrePlace() {
        return nbrePlace;
    }

    public void setNbrePlace(Integer nbrePlace) {
        this.nbrePlace = nbrePlace;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Set<HoraireDTO> getHoraires() {
        return horaires;
    }

    public void setHoraires(Set<HoraireDTO> horaires) {
        this.horaires = horaires;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SalleDTO)) {
            return false;
        }

        SalleDTO salleDTO = (SalleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, salleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SalleDTO{" +
            "id=" + getId() +
            ", numeroSalle='" + getNumeroSalle() + "'" +
            ", nbrePlace=" + getNbrePlace() +
            ", etat='" + getEtat() + "'" +
            ", horaires=" + getHoraires() +
            "}";
    }
}
