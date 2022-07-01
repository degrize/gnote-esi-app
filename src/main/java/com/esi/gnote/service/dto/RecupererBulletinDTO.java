package com.esi.gnote.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.esi.gnote.domain.RecupererBulletin} entity.
 */
public class RecupererBulletinDTO implements Serializable {

    private Long id;

    @NotNull
    private String signatureEleve;

    @Lob
    private byte[] bulletinScanne;

    private String bulletinScanneContentType;
    private EtudiantDTO etudiant;

    private BulletinDTO bulletin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSignatureEleve() {
        return signatureEleve;
    }

    public void setSignatureEleve(String signatureEleve) {
        this.signatureEleve = signatureEleve;
    }

    public byte[] getBulletinScanne() {
        return bulletinScanne;
    }

    public void setBulletinScanne(byte[] bulletinScanne) {
        this.bulletinScanne = bulletinScanne;
    }

    public String getBulletinScanneContentType() {
        return bulletinScanneContentType;
    }

    public void setBulletinScanneContentType(String bulletinScanneContentType) {
        this.bulletinScanneContentType = bulletinScanneContentType;
    }

    public EtudiantDTO getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(EtudiantDTO etudiant) {
        this.etudiant = etudiant;
    }

    public BulletinDTO getBulletin() {
        return bulletin;
    }

    public void setBulletin(BulletinDTO bulletin) {
        this.bulletin = bulletin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecupererBulletinDTO)) {
            return false;
        }

        RecupererBulletinDTO recupererBulletinDTO = (RecupererBulletinDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, recupererBulletinDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecupererBulletinDTO{" +
            "id=" + getId() +
            ", signatureEleve='" + getSignatureEleve() + "'" +
            ", bulletinScanne='" + getBulletinScanne() + "'" +
            ", etudiant=" + getEtudiant() +
            ", bulletin=" + getBulletin() +
            "}";
    }
}
