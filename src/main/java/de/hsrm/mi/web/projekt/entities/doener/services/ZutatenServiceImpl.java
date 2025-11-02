package de.hsrm.mi.web.projekt.entities.doener.services;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import de.hsrm.mi.web.projekt.entities.zutat.Zutat;
import de.hsrm.mi.web.projekt.entities.zutat.ZutatRepository;

@Service
public class ZutatenServiceImpl implements ZutatenService {

    private static final Logger logger = Logger.getLogger(ZutatenServiceImpl.class.getName());
    private final ZutatRepository zutatRepository;

    @Autowired
    public ZutatenServiceImpl(ZutatRepository zutatRepository) {
        this.zutatRepository = zutatRepository;
    }

    @Override
    public List<Zutat> findAllZutaten() {
        logger.info("findAllZutat() called");

        // Abrufen der Zutatliste aus dem Repository
        Collection<Zutat> zutatList = zutatRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));

        //logger.log(Level.INFO, "findAllZutat() sorted list: {0}", zutatList);
        return List.copyOf(zutatList);
    }

    @Override
    public Zutat findZutatByEan(String ean) {
        return zutatRepository.findById(ean)
                .orElseThrow(() -> new IllegalArgumentException("Zutat mit EAN " + ean + " nicht gefunden."));
    }

}
