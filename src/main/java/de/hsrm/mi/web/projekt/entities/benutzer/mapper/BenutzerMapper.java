package de.hsrm.mi.web.projekt.entities.benutzer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import de.hsrm.mi.web.projekt.benutzer.ui.BenutzerFormular;
import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;

@Mapper(componentModel = "spring")
public interface BenutzerMapper {
    // BenutzerFormular aus Benutzer-Entitaet fuellen
    @Mapping(target = "losungWiederh", ignore = true)
    BenutzerFormular benutzerToBenutzerFormular(Benutzer benutzer);

    // Benutzer-Entitaet aus Formularinhalt fuellen
    @Mapping(target = "loginName", ignore = true)
    Benutzer benutzerFormularToBenutzer(BenutzerFormular formular);

    // Update-Methode: Werte aus Formular in bestehendes Benutzer-Objekt kopieren
    // (außer loginName)
    @Mapping(target = "loginName", ignore = true)
    void updateBenutzerFromFormular(@MappingTarget Benutzer benutzer, BenutzerFormular formular);
}
