package de.hsrm.mi.web.projekt.doener.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.hsrm.mi.web.projekt.entities.doener.Doener;
import de.hsrm.mi.web.projekt.entities.doener.mapper.DoenerMapper;
import de.hsrm.mi.web.projekt.entities.doener.services.DoenerErfindungsService;
import de.hsrm.mi.web.projekt.entities.doener.services.DoenerService;
import de.hsrm.mi.web.projekt.entities.doener.services.ZutatenService;
import de.hsrm.mi.web.projekt.entities.zutat.Zutat;
import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/doener")
public class DoenerController {

    private static final Logger logger = LoggerFactory.getLogger(DoenerController.class);
    private static final String FORMULAR_MAP_KEY = "formularMap";
    private DoenerMapper mapper;
    private DoenerFormular formular;
    private Doener neuerDoener;

    @Autowired
    private DoenerService doenerService;
    @Autowired
    private ZutatenService zutatenService;
    @Autowired
    private DoenerErfindungsService doenerErfindungsService;

    // bevorzugter Methodenaufruf
    @Autowired
    public DoenerController(DoenerMapper mapper) {
        this.mapper = mapper;
    }

    @ModelAttribute
    public void initSessionData(HttpSession session) {
        // Initialisierung der Map, falls sie noch nicht existiert
        if (session.getAttribute(FORMULAR_MAP_KEY) == null) {
            session.setAttribute(FORMULAR_MAP_KEY, new HashMap<String, DoenerFormular>());
        }
    }

    @GetMapping("")
    public String doenerListe(Model model) {
        // Abrufen der Doenerliste
        List<Doener> doenerListe = new ArrayList<>(doenerService.findAllDoener());

        // Sicherstellen, dass die Liste nicht null ist
        if (doenerListe == null || doenerListe.isEmpty()) {
            model.addAttribute("info", "Es sind keine Doener vorhanden.");
        } else {
            model.addAttribute("doenerListe", doenerListe);
        }

        return "doener/liste";
    }

    @GetMapping("/{id}")
    public String doenerBearbeiten(@PathVariable("id") long id, Model model, HttpSession session) {
        Doener doener = doenerService.findDoenerById(id).orElse(null);

        if (id == 0 || doener == null) {
            formular = new DoenerFormular();
        } else {
            Doener d = doenerService.findDoenerById(id).orElse(null);

            formular = mapper.doenerToDoenerFormular(d);

            List<String> gewaehlt = d.getZutaten()
                    .stream()
                    .map(Zutat::getEan)
                    .toList();
            formular.setZutatenEan(gewaehlt);

            session.setAttribute("doenerVersion_" + id, doener.getVersion());

        }

        model.addAttribute("formular", formular);
        model.addAttribute("id", id);
        model.addAttribute("alleZutaten", zutatenService.findAllZutaten());
        model.addAttribute("version", formular.getVersion());

        return "doener/bearbeiten";

    }

    @GetMapping("/{id}/delete")
    public String doenerLöschen(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        try {
            doenerService.deleteDoenerById(id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("info", "Löschen fehlgeschlagen: " + e.getMessage());
        }

        return "redirect:/doener";
    }

    @PostMapping("/{id}")
    public String bearbeitenSpeichern(
            @PathVariable("id") long id,
            @ModelAttribute("formular") @Valid DoenerFormular formular,
            BindingResult bindingResult,
            Model model,
            HttpSession session) {

        if (bindingResult.hasErrors()) {
            logger.info("Error has been detected: " + bindingResult.toString());
            return "doener/bearbeiten";
        }

        logger.info("Empfangenes Formular: " + formular.toString());

        Doener doener = mapper.doenerFormularToDoener(formular);
        doener.setId(id);

        // Version aus der Session setzen
        Long gespeicherteVersion = (Long) session.getAttribute("doenerVersion_" + id);
        if (gespeicherteVersion != null) {
            doener.setVersion(gespeicherteVersion);
        }

        try {
            // Zutatenliste aus dem Formular verarbeiten
            List<String> neueZutatenEan = formular.getZutatenEan();
            List<Zutat> zutaten = doener.getZutaten() != null ? new ArrayList<>(doener.getZutaten())
                    : new ArrayList<>();

            if (neueZutatenEan != null) {
                List<Zutat> neueZutaten = neueZutatenEan.stream()
                        .map(ean -> zutatenService.findZutatByEan(ean))
                        .filter(Objects::nonNull)
                        .toList();
                zutaten.addAll(neueZutaten);
            }
            // Zutaten dem Döner hinzufügen
            doener.setZutaten(zutaten);

            doenerService.saveDoener(doener);
            logger.info("Doener gespeichert: " + doener.toString());
            model.addAttribute("ausgewaehlteZutaten", zutaten);
        } catch (OptimisticLockException e) {
            // Optimistic Locking Fehler behandeln
            logger.error("Optimistic Locking Fehler: ", e);
            model.addAttribute("info", "Speichern fehlgeschlagen: Der Döner wurde zwischenzeitlich geändert.");
            return "doener/bearbeiten";
        } catch (Exception e) {
            logger.error("Fehler beim Speichern des Doeners: ", e);
            model.addAttribute("info", e.getMessage());
        }

        return "redirect:/doener";
    }

    @PostMapping("/api/doener/neu")
    public String neuerDoener() {
                doenerErfindungsService.createRandomDoener();
        return "redirect:/doener";
    }

}
