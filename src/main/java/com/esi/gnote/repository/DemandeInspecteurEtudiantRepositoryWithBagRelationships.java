package com.esi.gnote.repository;

import com.esi.gnote.domain.DemandeInspecteurEtudiant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface DemandeInspecteurEtudiantRepositoryWithBagRelationships {
    Optional<DemandeInspecteurEtudiant> fetchBagRelationships(Optional<DemandeInspecteurEtudiant> demandeInspecteurEtudiant);

    List<DemandeInspecteurEtudiant> fetchBagRelationships(List<DemandeInspecteurEtudiant> demandeInspecteurEtudiants);

    Page<DemandeInspecteurEtudiant> fetchBagRelationships(Page<DemandeInspecteurEtudiant> demandeInspecteurEtudiants);
}
