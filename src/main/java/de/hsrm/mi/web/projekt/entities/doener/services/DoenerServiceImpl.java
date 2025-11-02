package de.hsrm.mi.web.projekt.entities.doener.services;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import de.hsrm.mi.web.projekt.doener.ui.DoenerException;
import de.hsrm.mi.web.projekt.entities.doener.Doener;
import de.hsrm.mi.web.projekt.entities.doener.DoenerRepository;
import de.hsrm.mi.web.projekt.messaging.FrontendNachrichtEvent;
import jakarta.transaction.Transactional;

@Service
public class DoenerServiceImpl implements DoenerService {
    DoenerRepository doenerRepository;
    @Autowired
    private ApplicationEventPublisher publisher;

    Logger logger = Logger.getLogger(DoenerServiceImpl.class.getName());

    @Autowired
    public DoenerServiceImpl(DoenerRepository doenerRepository) {
        this.doenerRepository = doenerRepository;

    }

    @Override
    @Transactional
    public Doener saveDoener(Doener d) {
        logger.log(Level.INFO, "saveDoener() called with: d = [{0}]", d);
        boolean isUpdate = doenerRepository.existsById(d.getId()); // Check ob Doener bereits existiert (dann Update)
                                                                   // sonst Create
        Doener savedDoener = doenerRepository.save(d);
        logger.log(Level.INFO, "saveDoener() saved: {0}", savedDoener);

        // Event publishen
        FrontendNachrichtEvent.Operation operation = isUpdate ? FrontendNachrichtEvent.Operation.UPDATE
                : FrontendNachrichtEvent.Operation.CREATE;
        publisher.publishEvent(new FrontendNachrichtEvent(
                FrontendNachrichtEvent.EventTyp.DOENER,
                savedDoener.getId(),
                operation));

        // try {
        //     Thread.sleep(1000);
        // } catch (InterruptedException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
 

        return savedDoener;
    }

    @Override
    @Transactional
    public Optional<Doener> findDoenerById(long id) {
        logger.log(Level.INFO, "findDoenerById() called with: id = [{0}]", id);
        Optional<Doener> doener = doenerRepository.findById(id);
        if (doener.isPresent()) {
            logger.log(Level.INFO, "findDoenerById() found: {0}", doener.get());
        } else {
            logger.log(Level.WARNING, "findDoenerById() not found: {0}", id);
        }
        return doener;
    }

    @Override
    public Collection<Doener> findAllDoener() {
        logger.info("findAllDoener() called");

        // Abrufen der Doenerliste aus dem Repository
        Collection<Doener> doenerList = doenerRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        // Null-Check: Falls die Liste null ist, wird eine leere Liste zurückgegeben
        if (doenerList == null) {
            logger.warning("findAllDoener() returned null, returning an empty list instead.");
            return Collections.emptyList();
        }

        logger.log(Level.INFO, "findAllDoener() sorted list: {0}", doenerList);
        return doenerList;
    }

    @Override
    @Transactional
    public void deleteDoenerById(long id) {
        logger.log(Level.INFO, "findDoenerById() called with: id = [{0}]", id);
        Optional<Doener> doener = doenerRepository.findById(id);

        if (doener.isPresent()) {
            doenerRepository.delete(doener.get());
            logger.log(Level.INFO, "deleteDoenerById() deleted Doener: {0}", doener.get());

            // Event publishen
            publisher.publishEvent(
                    new FrontendNachrichtEvent(
                            FrontendNachrichtEvent.EventTyp.DOENER,
                            id,
                            FrontendNachrichtEvent.Operation.DELETE));
        } else {
            logger.log(Level.WARNING, "deleteDoenerById() could not find Doener with id: {0}", id);
            throw new DoenerException("Doener mit id '" + id + "' nicht gefunden.");

        }
    }

}
