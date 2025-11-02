package de.hsrm.mi.web.projekt.entleihung.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.hsrm.mi.web.projekt.entities.doener.api.DoenerDTO;
import de.hsrm.mi.web.projekt.entities.doener.api.ZutatDTO;
import de.hsrm.mi.web.projekt.entleihung.services.EntleihungService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/entleihung")
public class EntleihungRestController {
    @Autowired
    private EntleihungService entleihungService;

    @PostMapping("")
    public void createEntleihung(@RequestBody EntleihungCreateDTO dto) {
        entleihungService.entleiheDoener(dto.doenerId(), dto.loginName());
    }

    @DeleteMapping("/{loginName}/{doenerId}")
    public void deleteEntleihung(@PathVariable String loginName, @PathVariable long doenerId) {
        entleihungService.zurueckgebeDoener(doenerId, loginName);
    }

    @GetMapping("/{loginName}")
    public List<DoenerDTO> getEntleihungenByUser(@PathVariable String loginName) {
        return entleihungService.findeEntleihungenVonBenutzer(loginName)
                .stream()
                .map(doener -> new DoenerDTO(
                        doener.getId(),
                        doener.getBezeichnung(),
                        doener.getPreis(),
                        doener.getVegetarizitaet(),
                        doener.getZutaten().stream()
                                .map(z -> new ZutatDTO(z.getEan(), z.getName(), z.getVegetarizitaet())).toList(),
                        doener.getVerfuegbar()))
                .toList();
    }
}
