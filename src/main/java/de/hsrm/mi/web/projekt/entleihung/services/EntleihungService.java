package de.hsrm.mi.web.projekt.entleihung.services;

import java.util.List;

import de.hsrm.mi.web.projekt.entities.doener.Doener;

public interface EntleihungService {
    void entleiheDoener(long doenerId, String loginName);

    void zurueckgebeDoener(long doenerId, String loginName);

    List<Doener> findeEntleihungenVonBenutzer(String loginName);
}
