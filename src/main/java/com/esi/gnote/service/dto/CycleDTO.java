package com.esi.gnote.service.dto;

import com.esi.gnote.domain.enumeration.TypeCycle;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.esi.gnote.domain.Cycle} entity.
 */
public class CycleDTO implements Serializable {

    private Long id;

    @NotNull
    private TypeCycle nomCycle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeCycle getNomCycle() {
        return nomCycle;
    }

    public void setNomCycle(TypeCycle nomCycle) {
        this.nomCycle = nomCycle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CycleDTO)) {
            return false;
        }

        CycleDTO cycleDTO = (CycleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cycleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CycleDTO{" +
            "id=" + getId() +
            ", nomCycle='" + getNomCycle() + "'" +
            "}";
    }
}
