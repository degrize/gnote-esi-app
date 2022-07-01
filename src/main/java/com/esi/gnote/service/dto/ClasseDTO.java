package com.esi.gnote.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.esi.gnote.domain.Classe} entity.
 */
@Schema(description = "not an ignored comment")
public class ClasseDTO implements Serializable {

    private Long id;

    @NotNull
    private String nomClasse;

    private FiliereDTO filiere;

    private Set<MatiereDTO> matieres = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomClasse() {
        return nomClasse;
    }

    public void setNomClasse(String nomClasse) {
        this.nomClasse = nomClasse;
    }

    public FiliereDTO getFiliere() {
        return filiere;
    }

    public void setFiliere(FiliereDTO filiere) {
        this.filiere = filiere;
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
        if (!(o instanceof ClasseDTO)) {
            return false;
        }

        ClasseDTO classeDTO = (ClasseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, classeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClasseDTO{" +
            "id=" + getId() +
            ", nomClasse='" + getNomClasse() + "'" +
            ", filiere=" + getFiliere() +
            ", matieres=" + getMatieres() +
            "}";
    }
}
