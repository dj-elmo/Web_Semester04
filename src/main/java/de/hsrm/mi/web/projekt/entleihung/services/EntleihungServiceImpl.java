package de.hsrm.mi.web.projekt.entleihung.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;
import de.hsrm.mi.web.projekt.entities.benutzer.services.BenutzerService;
import de.hsrm.mi.web.projekt.entities.doener.Doener;
import de.hsrm.mi.web.projekt.entities.doener.services.DoenerService;
import de.hsrm.mi.web.projekt.entleihung.exception.EntleihException;
import de.hsrm.mi.web.projekt.messaging.FrontendNachrichtEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EntleihungServiceImpl implements EntleihungService {
    private static final Logger logger = LoggerFactory.getLogger(EntleihungServiceImpl.class);

    @Autowired
    private DoenerService doenerService;
    @Autowired
    private BenutzerService benutzerService;
    @Autowired
    private ApplicationEventPublisher publisher;

    private static final int INCREMENT = 1;

    // Implementierung der Methoden aus dem EntleihungService-Interface
    @Override
    public void entleiheDoener(long doenerId, String loginName) {
        logger.info("Versuche Döner {} an Benutzer {} zu verleihen.", doenerId, loginName);
        Doener d = doenerService.findDoenerById(doenerId)
                .orElseThrow(() -> {
                    logger.warn("Döner mit ID {} nicht gefunden.", doenerId);
                    return new EntleihException("Doener mit ID " + doenerId + " nicht gefunden");
                });
        Benutzer b = benutzerService.findBenutzerById(loginName)
                .orElseThrow(() -> {
                    logger.warn("Benutzer mit Login {} nicht gefunden.", loginName);
                    return new EntleihException("Benutzer mit Login " + loginName + " nicht gefunden");
                });

        if (d.getVerfuegbar() > 0) {
            d.setEntliehen(d.getEntliehen() + INCREMENT);
            b.getEntlieheneDoener().add(d);

            logger.info("Döner {} erfolgreich an Benutzer {} verliehen.", doenerId, loginName);

            publisher.publishEvent(new FrontendNachrichtEvent(
                    FrontendNachrichtEvent.EventTyp.DOENER,
                    d.getId(),
                    FrontendNachrichtEvent.Operation.BOOKED));
        } else {
            logger.warn("Döner {} ist nicht verfügbar für Benutzer {}.", doenerId, loginName);
            throw new EntleihException(loginName + " kann den Döner mit ID " + doenerId
                    + " nicht entleihen, da er nicht verfügbar ist.");
        }
    }

    @Override
    public void zurueckgebeDoener(long doenerId, String loginName) {
        logger.info("Versuche Döner {} von Benutzer {} zurückzugeben.", doenerId, loginName);
        Doener d = doenerService.findDoenerById(doenerId)
                .orElseThrow(() -> {
                    logger.warn("Döner mit ID {} nicht gefunden.", doenerId);
                    return new EntleihException("Doener mit ID " + doenerId + " nicht gefunden");
                });
        Benutzer b = benutzerService.findBenutzerById(loginName)
                .orElseThrow(() -> {
                    logger.warn("Benutzer mit Login {} nicht gefunden.", loginName);
                    return new EntleihException("Benutzer mit Login " + loginName + " nicht gefunden");
                });
        if (d.getEntliehen() > 0 && b.getEntlieheneDoener().contains(d)) {
            d.setEntliehen(d.getEntliehen() - INCREMENT);
            b.getEntlieheneDoener().remove(d);

            logger.info("Döner {} wurde von Benutzer {} zurückgegeben.", doenerId, loginName);

            publisher.publishEvent(new FrontendNachrichtEvent(
                    FrontendNachrichtEvent.EventTyp.DOENER,
                    d.getId(),
                    FrontendNachrichtEvent.Operation.RETURNED));
        } else {
            logger.warn("Benutzer {} kann Döner {} nicht zurückgeben, da er ihn nicht entliehen hat.", loginName,
                    doenerId);
            throw new EntleihException(loginName + " kann den Döner mit ID " + doenerId
                    + " nicht zurückgeben, da er nicht entliehen wurde.");
        }
    }

    @Override
    public List<Doener> findeEntleihungenVonBenutzer(String loginName) {
        logger.info("Suche Entleihungen für Benutzer {}.", loginName);
        Benutzer b = benutzerService.findBenutzerById(loginName)
                .orElseThrow(() -> {
                    logger.warn("Benutzer mit Login {} nicht gefunden.", loginName);
                    return new EntleihException("Benutzer mit Login " + loginName + " nicht gefunden");
                });
        if (b.getEntlieheneDoener() == null) {
            logger.warn("Keine Entleihungen für Benutzer {} gefunden.", loginName);
            throw new EntleihException("Keine Entleihungen für Benutzer " + loginName + " gefunden.");
        }
        logger.info("Gefundene Entleihungen für Benutzer {}: {}", loginName, b.getEntlieheneDoener());
        return b.getEntlieheneDoener();
    }

}
