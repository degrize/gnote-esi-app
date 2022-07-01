package com.esi.gnote.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.esi.gnote.domain.AnneeScolaire} entity.
 */
@Schema(description = "AnneeScolaire entity.\n@author Luis-Borges.")
public class AnneeScolaireDTO implements Serializable {

    private Long id;

    /**
     * L' année scolaire\n@author Eleve.
     */
    @NotNull
    @Schema(description = "L' année scolaire\n@author Eleve.", required = true)
    private String periode;

    @NotNull
    private LocalDate dateDebut;

    private LocalDate dateFin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnneeScolaireDTO)) {
            return false;
        }

        AnneeScolaireDTO anneeScolaireDTO = (AnneeScolaireDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, anneeScolaireDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnneeScolaireDTO{" +
            "id=" + getId() +
            ", periode='" + getPeriode() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            "}";
    }
}
