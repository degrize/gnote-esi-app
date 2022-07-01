package com.esi.gnote.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.esi.gnote.domain.Matiere} entity.
 */
public class MatiereDTO implements Serializable {

    private Long id;

    @NotNull
    private String nomEC;

    @NotNull
    private Integer coeff;

    private ModuleDTO module;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomEC() {
        return nomEC;
    }

    public void setNomEC(String nomEC) {
        this.nomEC = nomEC;
    }

    public Integer getCoeff() {
        return coeff;
    }

    public void setCoeff(Integer coeff) {
        this.coeff = coeff;
    }

    public ModuleDTO getModule() {
        return module;
    }

    public void setModule(ModuleDTO module) {
        this.module = module;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MatiereDTO)) {
            return false;
        }

        MatiereDTO matiereDTO = (MatiereDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, matiereDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MatiereDTO{" +
            "id=" + getId() +
            ", nomEC='" + getNomEC() + "'" +
            ", coeff=" + getCoeff() +
            ", module=" + getModule() +
            "}";
    }
}
