package com.esi.gnote.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.esi.gnote.domain.Horaire} entity.
 */
@Schema(description = "not an ignored comment")
public class HoraireDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate dateSout;

    private LocalDate dateEffet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateSout() {
        return dateSout;
    }

    public void setDateSout(LocalDate dateSout) {
        this.dateSout = dateSout;
    }

    public LocalDate getDateEffet() {
        return dateEffet;
    }

    public void setDateEffet(LocalDate dateEffet) {
        this.dateEffet = dateEffet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HoraireDTO)) {
            return false;
        }

        HoraireDTO horaireDTO = (HoraireDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, horaireDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HoraireDTO{" +
            "id=" + getId() +
            ", dateSout='" + getDateSout() + "'" +
            ", dateEffet='" + getDateEffet() + "'" +
            "}";
    }
}
