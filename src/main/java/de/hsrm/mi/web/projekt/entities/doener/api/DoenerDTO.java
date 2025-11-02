package de.hsrm.mi.web.projekt.entities.doener.api;

import java.util.List;

public record DoenerDTO(
    long id,
    String bezeichnung,
    double preis,
    int vegetarizitaet,
    List<ZutatDTO> zutaten,
    int verfuegbar
) {}
