package de.hsrm.mi.web.projekt.entities.benutzer.services;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import de.hsrm.mi.web.projekt.benutzer.ui.BenutzerException;
import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;
import de.hsrm.mi.web.projekt.entities.benutzer.BenutzerRepository;
import jakarta.transaction.Transactional;

@Service
public class BenutzerServiceImpl implements BenutzerService {
    BenutzerRepository benutzerRepository;
    Logger logger = Logger.getLogger(BenutzerServiceImpl.class.getName());

    @Autowired
    public BenutzerServiceImpl(BenutzerRepository benutzerRepository) {
        this.benutzerRepository = benutzerRepository;

    }

    @Override
    @Transactional
    public Benutzer saveBenutzer(Benutzer b) {
        logger.log(Level.INFO, "saveBenutzer() called with: b = [{0}]", b);
        Benutzer savedBenutzer = benutzerRepository.save(b);
        logger.log(Level.INFO, "saveBenutzer() saved: {0}", savedBenutzer);
        return savedBenutzer;
    }

    @Override
    public Optional<Benutzer> findBenutzerById(String loginName) {
        logger.log(Level.INFO, "findBenutzerById() called with: loginName = [{0}]", loginName);
        Optional<Benutzer> benutzer = benutzerRepository.findById(loginName);
        if (benutzer.isPresent()) {
            logger.log(Level.INFO, "findBenutzerById() found: {0}", benutzer.get());
        } else {
            logger.log(Level.WARNING, "findBenutzerById() not found: {0}", loginName);
        }
        return benutzer;
    }

    // @Override
    // public Collection<Benutzer> findAllBenutzer() {
    // logger.info("findAllBenutzer() called");
    // Collection<Benutzer> benutzerList =
    // benutzerRepository.findAll(Sort.by(Sort.Direction.ASC, "loginName"));
    // //TODO je nach dem wie der code im späteren verlauf (spätestens Ueb 04
    // Aufgabe 6)
    // //reagiert muss hier wahrscheinlich ein null check für benutzerListe
    // logger.log(Level.INFO, "findAllBenutzer() sorted list: {0}", benutzerList);

    // return benutzerList;

    // }

    @Override
    public Collection<Benutzer> findAllBenutzer() {
        logger.info("findAllBenutzer() called");

        // Abrufen der Benutzerliste aus dem Repository
        Collection<Benutzer> benutzerList = benutzerRepository.findAll(Sort.by(Sort.Direction.ASC, "loginName"));

        // Null-Check: Falls die Liste null ist, wird eine leere Liste zurückgegeben
        if (benutzerList == null) {
            logger.warning("findAllBenutzer() returned null, returning an empty list instead.");
            return Collections.emptyList();
        }

        logger.log(Level.INFO, "findAllBenutzer() sorted list: {0}", benutzerList);
        return benutzerList;
    }

    @Override
    @Transactional
    public void deleteBenutzerById(String loginName) {
        logger.log(Level.INFO, "findBenutzerById() called with: loginName = [{0}]", loginName);
        Optional<Benutzer> benutzer = benutzerRepository.findById(loginName);

        if (benutzer.isPresent()) {
            benutzerRepository.delete(benutzer.get());
            logger.log(Level.INFO, "deleteBenutzerById() deleted Benutzer: {0}", benutzer.get());
        } else {
            logger.log(Level.WARNING, "deleteBenutzerById() could not find Benutzer with loginName: {0}", loginName);
            throw new BenutzerException("Benutzer mit loginName '" + loginName + "' nicht gefunden.");

        }
    }

    @Override
    @Transactional
    public Benutzer aktualisiereBenutzerAttribut(String loginName, String feldname, String wert) {
        logger.log(Level.INFO,
                "aktualisiereBenutzerAttribut() called with: loginName = [{0}], feldname = [{1}], wert = [{2}]",
                new Object[] { loginName, feldname, wert });
        Optional<Benutzer> optionalBenutzer = benutzerRepository.findById(loginName);

        if (optionalBenutzer.isPresent()) {
            Benutzer benutzer = optionalBenutzer.get();
            switch (feldname) {
                case "name":
                    benutzer.setName(wert);
                    break;
                case "digitalpostanschrift":
                    benutzer.setDigitalpostanschrift(wert);
                    break;
                default:
                    logger.log(Level.WARNING, "aktualisiereBenutzerAttribut() unrecognized field: {0}", feldname);
                    throw new BenutzerException("Unbekanntes Feld: " + feldname);
            }
            Benutzer updatedBenutzer = benutzerRepository.save(benutzer);
            logger.log(Level.INFO, "aktualisiereBenutzerAttribut() updated Benutzer: {0}", updatedBenutzer);
            return updatedBenutzer;
        } else {
            logger.log(Level.WARNING, "aktualisiereBenutzerAttribut() Benutzer not found with loginName: {0}",
                    loginName);
            throw new BenutzerException("Benutzer mit loginName '" + loginName + "' nicht gefunden.");
        }
    }

}
