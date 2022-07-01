package com.esi.gnote.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.esi.gnote.domain.Semestre} entity.
 */
@Schema(description = "not an ignored comment")
public class SemestreDTO implements Serializable {

    private Long id;

    @NotNull
    private String nomSemestre;

    private AnneeScolaireDTO anneeScolaire;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomSemestre() {
        return nomSemestre;
    }

    public void setNomSemestre(String nomSemestre) {
        this.nomSemestre = nomSemestre;
    }

    public AnneeScolaireDTO getAnneeScolaire() {
        return anneeScolaire;
    }

    public void setAnneeScolaire(AnneeScolaireDTO anneeScolaire) {
        this.anneeScolaire = anneeScolaire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SemestreDTO)) {
            return false;
        }

        SemestreDTO semestreDTO = (SemestreDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, semestreDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SemestreDTO{" +
            "id=" + getId() +
            ", nomSemestre='" + getNomSemestre() + "'" +
            ", anneeScolaire=" + getAnneeScolaire() +
            "}";
    }
}
