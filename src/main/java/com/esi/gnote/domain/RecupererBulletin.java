package com.esi.gnote.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RecupererBulletin.
 */
@Entity
@Table(name = "recuperer_bulletin")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RecupererBulletin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "signature_eleve", nullable = false)
    private String signatureEleve;

    @Lob
    @Column(name = "bulletin_scanne", nullable = false)
    private byte[] bulletinScanne;

    @NotNull
    @Column(name = "bulletin_scanne_content_type", nullable = false)
    private String bulletinScanneContentType;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "classes", "encadreur", "inspecteurs", "professeurs", "soutenances", "notes", "demandeInspecteurEtudiants" },
        allowSetters = true
    )
    private Etudiant etudiant;

    @ManyToOne
    @JsonIgnoreProperties(value = { "etudiant", "semestre", "professeurs" }, allowSetters = true)
    private Bulletin bulletin;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RecupererBulletin id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSignatureEleve() {
        return this.signatureEleve;
    }

    public RecupererBulletin signatureEleve(String signatureEleve) {
        this.setSignatureEleve(signatureEleve);
        return this;
    }

    public void setSignatureEleve(String signatureEleve) {
        this.signatureEleve = signatureEleve;
    }

    public byte[] getBulletinScanne() {
        return this.bulletinScanne;
    }

    public RecupererBulletin bulletinScanne(byte[] bulletinScanne) {
        this.setBulletinScanne(bulletinScanne);
        return this;
    }

    public void setBulletinScanne(byte[] bulletinScanne) {
        this.bulletinScanne = bulletinScanne;
    }

    public String getBulletinScanneContentType() {
        return this.bulletinScanneContentType;
    }

    public RecupererBulletin bulletinScanneContentType(String bulletinScanneContentType) {
        this.bulletinScanneContentType = bulletinScanneContentType;
        return this;
    }

    public void setBulletinScanneContentType(String bulletinScanneContentType) {
        this.bulletinScanneContentType = bulletinScanneContentType;
    }

    public Etudiant getEtudiant() {
        return this.etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public RecupererBulletin etudiant(Etudiant etudiant) {
        this.setEtudiant(etudiant);
        return this;
    }

    public Bulletin getBulletin() {
        return this.bulletin;
    }

    public void setBulletin(Bulletin bulletin) {
        this.bulletin = bulletin;
    }

    public RecupererBulletin bulletin(Bulletin bulletin) {
        this.setBulletin(bulletin);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecupererBulletin)) {
            return false;
        }
        return id != null && id.equals(((RecupererBulletin) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecupererBulletin{" +
            "id=" + getId() +
            ", signatureEleve='" + getSignatureEleve() + "'" +
            ", bulletinScanne='" + getBulletinScanne() + "'" +
            ", bulletinScanneContentType='" + getBulletinScanneContentType() + "'" +
            "}";
    }
}
