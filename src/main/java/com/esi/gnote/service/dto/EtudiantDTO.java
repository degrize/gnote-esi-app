package com.esi.gnote.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.esi.gnote.domain.Etudiant} entity.
 */
public class EtudiantDTO implements Serializable {

    private Long id;

    @NotNull
    private String matriculeET;

    @NotNull
    private String nomET;

    @NotNull
    private String prenomET;

    @Lob
    private byte[] photo;

    private String photoContentType;
    private String numeroParent;

    private String numeroTuteur;

    private String contactET;

    private Set<ClasseDTO> classes = new HashSet<>();

    private EncadreurDTO encadreur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatriculeET() {
        return matriculeET;
    }

    public void setMatriculeET(String matriculeET) {
        this.matriculeET = matriculeET;
    }

    public String getNomET() {
        return nomET;
    }

    public void setNomET(String nomET) {
        this.nomET = nomET;
    }

    public String getPrenomET() {
        return prenomET;
    }

    public void setPrenomET(String prenomET) {
        this.prenomET = prenomET;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public String getNumeroParent() {
        return numeroParent;
    }

    public void setNumeroParent(String numeroParent) {
        this.numeroParent = numeroParent;
    }

    public String getNumeroTuteur() {
        return numeroTuteur;
    }

    public void setNumeroTuteur(String numeroTuteur) {
        this.numeroTuteur = numeroTuteur;
    }

    public String getContactET() {
        return contactET;
    }

    public void setContactET(String contactET) {
        this.contactET = contactET;
    }

    public Set<ClasseDTO> getClasses() {
        return classes;
    }

    public void setClasses(Set<ClasseDTO> classes) {
        this.classes = classes;
    }

    public EncadreurDTO getEncadreur() {
        return encadreur;
    }

    public void setEncadreur(EncadreurDTO encadreur) {
        this.encadreur = encadreur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EtudiantDTO)) {
            return false;
        }

        EtudiantDTO etudiantDTO = (EtudiantDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, etudiantDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EtudiantDTO{" +
            "id=" + getId() +
            ", matriculeET='" + getMatriculeET() + "'" +
            ", nomET='" + getNomET() + "'" +
            ", prenomET='" + getPrenomET() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", numeroParent='" + getNumeroParent() + "'" +
            ", numeroTuteur='" + getNumeroTuteur() + "'" +
            ", contactET='" + getContactET() + "'" +
            ", classes=" + getClasses() +
            ", encadreur=" + getEncadreur() +
            "}";
    }
}
