package com.esi.gnote.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.esi.gnote.domain.Encadreur} entity.
 */
public class EncadreurDTO implements Serializable {

    private Long id;

    @NotNull
    private String nomEnc;

    private String prenomsEnc;

    private String emailEnc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomEnc() {
        return nomEnc;
    }

    public void setNomEnc(String nomEnc) {
        this.nomEnc = nomEnc;
    }

    public String getPrenomsEnc() {
        return prenomsEnc;
    }

    public void setPrenomsEnc(String prenomsEnc) {
        this.prenomsEnc = prenomsEnc;
    }

    public String getEmailEnc() {
        return emailEnc;
    }

    public void setEmailEnc(String emailEnc) {
        this.emailEnc = emailEnc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EncadreurDTO)) {
            return false;
        }

        EncadreurDTO encadreurDTO = (EncadreurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, encadreurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EncadreurDTO{" +
            "id=" + getId() +
            ", nomEnc='" + getNomEnc() + "'" +
            ", prenomsEnc='" + getPrenomsEnc() + "'" +
            ", emailEnc='" + getEmailEnc() + "'" +
            "}";
    }
}
