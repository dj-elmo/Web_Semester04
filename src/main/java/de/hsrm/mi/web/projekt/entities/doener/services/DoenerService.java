package de.hsrm.mi.web.projekt.entities.doener.services;

import java.util.Collection;
import java.util.Optional;

import de.hsrm.mi.web.projekt.entities.doener.Doener;

public interface DoenerService {

    Doener saveDoener(Doener doener);

    Optional<Doener> findDoenerById(long id);

    Collection<Doener> findAllDoener();

    void deleteDoenerById(long id);
    
}
