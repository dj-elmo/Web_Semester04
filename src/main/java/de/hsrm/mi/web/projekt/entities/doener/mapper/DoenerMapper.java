package de.hsrm.mi.web.projekt.entities.doener.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import de.hsrm.mi.web.projekt.doener.ui.DoenerFormular;
import de.hsrm.mi.web.projekt.doener.ui.ZutatFormular;
import de.hsrm.mi.web.projekt.entities.doener.Doener;
import de.hsrm.mi.web.projekt.entities.doener.api.DoenerDTO;
import de.hsrm.mi.web.projekt.entities.zutat.Zutat;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE) // Unbenutzte Felder werden ignoriert,
                                                                                  // um Warnungen zu vermeiden (z.B.
                                                                                  // bestand und version)
public interface DoenerMapper {
    // DoenerFormular aus Doener-Entitaet fuellen
    DoenerFormular doenerToDoenerFormular(Doener doener);

    // Doener-Entitaet aus Formularinhalt fuellen
    Doener doenerFormularToDoener(DoenerFormular formular);

    List<ZutatFormular> zutatListToZutatFormList(List<Zutat> lstz);

    DoenerDTO doenerToDoenerDTO(Doener doener);

    Doener doenerDTOToDoener(DoenerDTO doenerDto);

    List<DoenerDTO> doenerListToDoenerDTOList(List<Doener> doenerList);

}
