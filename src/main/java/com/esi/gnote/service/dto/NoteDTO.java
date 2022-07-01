package com.esi.gnote.service.dto;

import com.esi.gnote.domain.enumeration.TypeNote;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.esi.gnote.domain.Note} entity.
 */
public class NoteDTO implements Serializable {

    private Long id;

    @NotNull
    private Double note;

    @NotNull
    private TypeNote typeNote;

    private Set<EtudiantDTO> etudiants = new HashSet<>();

    private Set<MatiereDTO> matieres = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getNote() {
        return note;
    }

    public void setNote(Double note) {
        this.note = note;
    }

    public TypeNote getTypeNote() {
        return typeNote;
    }

    public void setTypeNote(TypeNote typeNote) {
        this.typeNote = typeNote;
    }

    public Set<EtudiantDTO> getEtudiants() {
        return etudiants;
    }

    public void setEtudiants(Set<EtudiantDTO> etudiants) {
        this.etudiants = etudiants;
    }

    public Set<MatiereDTO> getMatieres() {
        return matieres;
    }

    public void setMatieres(Set<MatiereDTO> matieres) {
        this.matieres = matieres;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NoteDTO)) {
            return false;
        }

        NoteDTO noteDTO = (NoteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, noteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NoteDTO{" +
            "id=" + getId() +
            ", note=" + getNote() +
            ", typeNote='" + getTypeNote() + "'" +
            ", etudiants=" + getEtudiants() +
            ", matieres=" + getMatieres() +
            "}";
    }
}
