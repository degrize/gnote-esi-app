package com.esi.gnote.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.esi.gnote.domain.DemandeInspecteurDE} entity.
 */
public class DemandeInspecteurDEDTO implements Serializable {

    private Long id;

    @NotNull
    private String message;

    private Set<ProfesseurDTO> professeurs = new HashSet<>();

    private Set<InspecteurDTO> inspecteurs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Set<ProfesseurDTO> getProfesseurs() {
        return professeurs;
    }

    public void setProfesseurs(Set<ProfesseurDTO> professeurs) {
        this.professeurs = professeurs;
    }

    public Set<InspecteurDTO> getInspecteurs() {
        return inspecteurs;
    }

    public void setInspecteurs(Set<InspecteurDTO> inspecteurs) {
        this.inspecteurs = inspecteurs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DemandeInspecteurDEDTO)) {
            return false;
        }

        DemandeInspecteurDEDTO demandeInspecteurDEDTO = (DemandeInspecteurDEDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, demandeInspecteurDEDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DemandeInspecteurDEDTO{" +
            "id=" + getId() +
            ", message='" + getMessage() + "'" +
            ", professeurs=" + getProfesseurs() +
            ", inspecteurs=" + getInspecteurs() +
            "}";
    }
}
