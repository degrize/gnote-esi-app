package com.esi.gnote.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.esi.gnote.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.esi.gnote.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.esi.gnote.domain.User.class.getName());
            createCache(cm, com.esi.gnote.domain.Authority.class.getName());
            createCache(cm, com.esi.gnote.domain.User.class.getName() + ".authorities");
            createCache(cm, com.esi.gnote.domain.AnneeScolaire.class.getName());
            createCache(cm, com.esi.gnote.domain.Planche.class.getName());
            createCache(cm, com.esi.gnote.domain.Semestre.class.getName());
            createCache(cm, com.esi.gnote.domain.Note.class.getName());
            createCache(cm, com.esi.gnote.domain.Note.class.getName() + ".etudiants");
            createCache(cm, com.esi.gnote.domain.Note.class.getName() + ".matieres");
            createCache(cm, com.esi.gnote.domain.Bulletin.class.getName());
            createCache(cm, com.esi.gnote.domain.Bulletin.class.getName() + ".professeurs");
            createCache(cm, com.esi.gnote.domain.Professeur.class.getName());
            createCache(cm, com.esi.gnote.domain.Professeur.class.getName() + ".etudiants");
            createCache(cm, com.esi.gnote.domain.Professeur.class.getName() + ".classes");
            createCache(cm, com.esi.gnote.domain.Professeur.class.getName() + ".matieres");
            createCache(cm, com.esi.gnote.domain.Professeur.class.getName() + ".inspecteurs");
            createCache(cm, com.esi.gnote.domain.Professeur.class.getName() + ".bulletins");
            createCache(cm, com.esi.gnote.domain.Professeur.class.getName() + ".juries");
            createCache(cm, com.esi.gnote.domain.Professeur.class.getName() + ".demandeInspecteurDES");
            createCache(cm, com.esi.gnote.domain.Filiere.class.getName());
            createCache(cm, com.esi.gnote.domain.Filiere.class.getName() + ".modules");
            createCache(cm, com.esi.gnote.domain.Filiere.class.getName() + ".classes");
            createCache(cm, com.esi.gnote.domain.Classe.class.getName());
            createCache(cm, com.esi.gnote.domain.Classe.class.getName() + ".matieres");
            createCache(cm, com.esi.gnote.domain.Classe.class.getName() + ".professeurs");
            createCache(cm, com.esi.gnote.domain.Classe.class.getName() + ".etudiants");
            createCache(cm, com.esi.gnote.domain.Etudiant.class.getName());
            createCache(cm, com.esi.gnote.domain.Etudiant.class.getName() + ".classes");
            createCache(cm, com.esi.gnote.domain.Etudiant.class.getName() + ".inspecteurs");
            createCache(cm, com.esi.gnote.domain.Etudiant.class.getName() + ".professeurs");
            createCache(cm, com.esi.gnote.domain.Etudiant.class.getName() + ".soutenances");
            createCache(cm, com.esi.gnote.domain.Etudiant.class.getName() + ".notes");
            createCache(cm, com.esi.gnote.domain.Etudiant.class.getName() + ".demandeInspecteurEtudiants");
            createCache(cm, com.esi.gnote.domain.Absence.class.getName());
            createCache(cm, com.esi.gnote.domain.Inspecteur.class.getName());
            createCache(cm, com.esi.gnote.domain.Inspecteur.class.getName() + ".professeurs");
            createCache(cm, com.esi.gnote.domain.Inspecteur.class.getName() + ".etudiants");
            createCache(cm, com.esi.gnote.domain.Inspecteur.class.getName() + ".demandeInspecteurEtudiants");
            createCache(cm, com.esi.gnote.domain.Inspecteur.class.getName() + ".demandeInspecteurDES");
            createCache(cm, com.esi.gnote.domain.Matiere.class.getName());
            createCache(cm, com.esi.gnote.domain.Matiere.class.getName() + ".professeurs");
            createCache(cm, com.esi.gnote.domain.Matiere.class.getName() + ".notes");
            createCache(cm, com.esi.gnote.domain.Matiere.class.getName() + ".classes");
            createCache(cm, com.esi.gnote.domain.Module.class.getName());
            createCache(cm, com.esi.gnote.domain.Module.class.getName() + ".matieres");
            createCache(cm, com.esi.gnote.domain.Module.class.getName() + ".filieres");
            createCache(cm, com.esi.gnote.domain.Encadreur.class.getName());
            createCache(cm, com.esi.gnote.domain.Encadreur.class.getName() + ".etudiants");
            createCache(cm, com.esi.gnote.domain.Horaire.class.getName());
            createCache(cm, com.esi.gnote.domain.Horaire.class.getName() + ".salles");
            createCache(cm, com.esi.gnote.domain.Soutenance.class.getName());
            createCache(cm, com.esi.gnote.domain.Soutenance.class.getName() + ".juries");
            createCache(cm, com.esi.gnote.domain.Soutenance.class.getName() + ".etudiants");
            createCache(cm, com.esi.gnote.domain.Salle.class.getName());
            createCache(cm, com.esi.gnote.domain.Salle.class.getName() + ".horaires");
            createCache(cm, com.esi.gnote.domain.Jury.class.getName());
            createCache(cm, com.esi.gnote.domain.Jury.class.getName() + ".professeurs");
            createCache(cm, com.esi.gnote.domain.Cycle.class.getName());
            createCache(cm, com.esi.gnote.domain.DemandeInspecteurEtudiant.class.getName());
            createCache(cm, com.esi.gnote.domain.DemandeInspecteurEtudiant.class.getName() + ".etudiants");
            createCache(cm, com.esi.gnote.domain.DemandeInspecteurEtudiant.class.getName() + ".inspecteurs");
            createCache(cm, com.esi.gnote.domain.DemandeInspecteurDE.class.getName());
            createCache(cm, com.esi.gnote.domain.DemandeInspecteurDE.class.getName() + ".professeurs");
            createCache(cm, com.esi.gnote.domain.DemandeInspecteurDE.class.getName() + ".inspecteurs");
            createCache(cm, com.esi.gnote.domain.RecupererBulletin.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
