package com.esi.gnote.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.esi.gnote.domain.Jury} entity.
 */
@Schema(description = "The Jury entity.")
public class JuryDTO implements Serializable {

    private Long id;

    /**
     * The firstname attribute.
     */
    @NotNull
    @Schema(description = "The firstname attribute.", required = true)
    private String presidentJury;

    private Set<ProfesseurDTO> professeurs = new HashSet<>();

    private SoutenanceDTO soutenance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPresidentJury() {
        return presidentJury;
    }

    public void setPresidentJury(String presidentJury) {
        this.presidentJury = presidentJury;
    }

    public Set<ProfesseurDTO> getProfesseurs() {
        return professeurs;
    }

    public void setProfesseurs(Set<ProfesseurDTO> professeurs) {
        this.professeurs = professeurs;
    }

    public SoutenanceDTO getSoutenance() {
        return soutenance;
    }

    public void setSoutenance(SoutenanceDTO soutenance) {
        this.soutenance = soutenance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JuryDTO)) {
            return false;
        }

        JuryDTO juryDTO = (JuryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, juryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JuryDTO{" +
            "id=" + getId() +
            ", presidentJury='" + getPresidentJury() + "'" +
            ", professeurs=" + getProfesseurs() +
            ", soutenance=" + getSoutenance() +
            "}";
    }
}
