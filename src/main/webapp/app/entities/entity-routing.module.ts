import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'annee-scolaire',
        data: { pageTitle: 'gnoteEsiApp.anneeScolaire.home.title' },
        loadChildren: () => import('./annee-scolaire/annee-scolaire.module').then(m => m.AnneeScolaireModule),
      },
      {
        path: 'planche',
        data: { pageTitle: 'gnoteEsiApp.planche.home.title' },
        loadChildren: () => import('./planche/planche.module').then(m => m.PlancheModule),
      },
      {
        path: 'semestre',
        data: { pageTitle: 'gnoteEsiApp.semestre.home.title' },
        loadChildren: () => import('./semestre/semestre.module').then(m => m.SemestreModule),
      },
      {
        path: 'note',
        data: { pageTitle: 'gnoteEsiApp.note.home.title' },
        loadChildren: () => import('./note/note.module').then(m => m.NoteModule),
      },
      {
        path: 'bulletin',
        data: { pageTitle: 'gnoteEsiApp.bulletin.home.title' },
        loadChildren: () => import('./bulletin/bulletin.module').then(m => m.BulletinModule),
      },
      {
        path: 'professeur',
        data: { pageTitle: 'gnoteEsiApp.professeur.home.title' },
        loadChildren: () => import('./professeur/professeur.module').then(m => m.ProfesseurModule),
      },
      {
        path: 'filiere',
        data: { pageTitle: 'gnoteEsiApp.filiere.home.title' },
        loadChildren: () => import('./filiere/filiere.module').then(m => m.FiliereModule),
      },
      {
        path: 'classe',
        data: { pageTitle: 'gnoteEsiApp.classe.home.title' },
        loadChildren: () => import('./classe/classe.module').then(m => m.ClasseModule),
      },
      {
        path: 'etudiant',
        data: { pageTitle: 'gnoteEsiApp.etudiant.home.title' },
        loadChildren: () => import('./etudiant/etudiant.module').then(m => m.EtudiantModule),
      },
      {
        path: 'absence',
        data: { pageTitle: 'gnoteEsiApp.absence.home.title' },
        loadChildren: () => import('./absence/absence.module').then(m => m.AbsenceModule),
      },
      {
        path: 'inspecteur',
        data: { pageTitle: 'gnoteEsiApp.inspecteur.home.title' },
        loadChildren: () => import('./inspecteur/inspecteur.module').then(m => m.InspecteurModule),
      },
      {
        path: 'matiere',
        data: { pageTitle: 'gnoteEsiApp.matiere.home.title' },
        loadChildren: () => import('./matiere/matiere.module').then(m => m.MatiereModule),
      },
      {
        path: 'module',
        data: { pageTitle: 'gnoteEsiApp.module.home.title' },
        loadChildren: () => import('./module/module.module').then(m => m.ModuleModule),
      },
      {
        path: 'encadreur',
        data: { pageTitle: 'gnoteEsiApp.encadreur.home.title' },
        loadChildren: () => import('./encadreur/encadreur.module').then(m => m.EncadreurModule),
      },
      {
        path: 'horaire',
        data: { pageTitle: 'gnoteEsiApp.horaire.home.title' },
        loadChildren: () => import('./horaire/horaire.module').then(m => m.HoraireModule),
      },
      {
        path: 'soutenance',
        data: { pageTitle: 'gnoteEsiApp.soutenance.home.title' },
        loadChildren: () => import('./soutenance/soutenance.module').then(m => m.SoutenanceModule),
      },
      {
        path: 'salle',
        data: { pageTitle: 'gnoteEsiApp.salle.home.title' },
        loadChildren: () => import('./salle/salle.module').then(m => m.SalleModule),
      },
      {
        path: 'jury',
        data: { pageTitle: 'gnoteEsiApp.jury.home.title' },
        loadChildren: () => import('./jury/jury.module').then(m => m.JuryModule),
      },
      {
        path: 'cycle',
        data: { pageTitle: 'gnoteEsiApp.cycle.home.title' },
        loadChildren: () => import('./cycle/cycle.module').then(m => m.CycleModule),
      },
      {
        path: 'demande-inspecteur-etudiant',
        data: { pageTitle: 'gnoteEsiApp.demandeInspecteurEtudiant.home.title' },
        loadChildren: () =>
          import('./demande-inspecteur-etudiant/demande-inspecteur-etudiant.module').then(m => m.DemandeInspecteurEtudiantModule),
      },
      {
        path: 'demande-inspecteur-de',
        data: { pageTitle: 'gnoteEsiApp.demandeInspecteurDE.home.title' },
        loadChildren: () => import('./demande-inspecteur-de/demande-inspecteur-de.module').then(m => m.DemandeInspecteurDEModule),
      },
      {
        path: 'recuperer-bulletin',
        data: { pageTitle: 'gnoteEsiApp.recupererBulletin.home.title' },
        loadChildren: () => import('./recuperer-bulletin/recuperer-bulletin.module').then(m => m.RecupererBulletinModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
