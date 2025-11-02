package de.hsrm.mi.web.projekt.entities.doener.services;

import java.util.List;

import de.hsrm.mi.web.projekt.entities.zutat.Zutat;

public interface ZutatenService {
    List<Zutat> findAllZutaten();
    Zutat findZutatByEan(String ean);
}

