package de.hsrm.mi.web.projekt.entities.doener.api;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.hsrm.mi.web.projekt.entities.doener.Doener;
import de.hsrm.mi.web.projekt.entities.doener.services.DoenerService;


@RestController
@RequestMapping("/api/doener")
public class DoenerRestController {
    
    @Autowired
    private DoenerService doenerService;

    @GetMapping("/{id}")
    public Doener getDoener(@PathVariable long id) {
        return doenerService.findDoenerById(id)
                .orElseThrow(() -> new DoenerNotFoundException(id));
    }

    @GetMapping("")
    public List<Doener> getAll() {
        return doenerService.findAllDoener()
                .stream()
                .sorted(Comparator.comparing(Doener::getBezeichnung))
                .toList();
    }
    
    
}
