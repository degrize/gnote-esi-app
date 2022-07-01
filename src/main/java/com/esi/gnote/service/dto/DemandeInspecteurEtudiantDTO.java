package com.esi.gnote.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.esi.gnote.domain.DemandeInspecteurEtudiant} entity.
 */
public class DemandeInspecteurEtudiantDTO implements Serializable {

    private Long id;

    @NotNull
    private String message;

    private Set<EtudiantDTO> etudiants = new HashSet<>();

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

    public Set<EtudiantDTO> getEtudiants() {
        return etudiants;
    }

    public void setEtudiants(Set<EtudiantDTO> etudiants) {
        this.etudiants = etudiants;
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
        if (!(o instanceof DemandeInspecteurEtudiantDTO)) {
            return false;
        }

        DemandeInspecteurEtudiantDTO demandeInspecteurEtudiantDTO = (DemandeInspecteurEtudiantDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, demandeInspecteurEtudiantDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DemandeInspecteurEtudiantDTO{" +
            "id=" + getId() +
            ", message='" + getMessage() + "'" +
            ", etudiants=" + getEtudiants() +
            ", inspecteurs=" + getInspecteurs() +
            "}";
    }
}
