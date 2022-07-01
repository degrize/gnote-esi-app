package com.esi.gnote.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.esi.gnote.domain.Planche} entity.
 */
public class PlancheDTO implements Serializable {

    private Long id;

    @NotNull
    private String observation;

    private SemestreDTO semestre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public SemestreDTO getSemestre() {
        return semestre;
    }

    public void setSemestre(SemestreDTO semestre) {
        this.semestre = semestre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlancheDTO)) {
            return false;
        }

        PlancheDTO plancheDTO = (PlancheDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, plancheDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlancheDTO{" +
            "id=" + getId() +
            ", observation='" + getObservation() + "'" +
            ", semestre=" + getSemestre() +
            "}";
    }
}
