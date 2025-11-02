package de.hsrm.mi.web.projekt.benutzer.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;
import de.hsrm.mi.web.projekt.entities.benutzer.mapper.BenutzerMapper;
import de.hsrm.mi.web.projekt.entities.benutzer.services.BenutzerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/benutzer")
public class BenutzerController {

    private static final Logger logger = LoggerFactory.getLogger(BenutzerController.class);
    private static final String FORMULAR_MAP_KEY = "formularMap";
    private BenutzerMapper mapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BenutzerService benutzerService;
    // Validator für PutMapping
    @Autowired
    private Validator validator;

    // bevorzugter Methodenaufruf
    @Autowired
    public BenutzerController(BenutzerMapper mapper) {
        this.mapper = mapper;
    }

    @ModelAttribute
    public void initSessionData(HttpSession session) {
        // Initialisierung der Map, falls sie noch nicht existiert
        if (session.getAttribute(FORMULAR_MAP_KEY) == null) {
            session.setAttribute(FORMULAR_MAP_KEY, new HashMap<String, BenutzerFormular>());
        }
    }

    @GetMapping("")
    public String benutzerListe(Model model) {
        // Abrufen der Benutzerliste
        List<Benutzer> benutzerListe = new ArrayList<>(benutzerService.findAllBenutzer());

        // Sicherstellen, dass die Liste nicht null ist
        if (benutzerListe == null || benutzerListe.isEmpty()) {
            model.addAttribute("info", "Es sind keine Benutzer vorhanden.");
        } else {
            model.addAttribute("benutzerListe", benutzerListe);
        }

        return "benutzer/liste";
    }

    @GetMapping("/{loginname}")
    public String benutzerBearbeiten(@PathVariable("loginname") String loginname, Model model, HttpSession session) {
        Benutzer benutzer = benutzerService.findBenutzerById(loginname).orElse(null);

        if (benutzer == null) {
            benutzer = new Benutzer(loginname);
        }
        BenutzerFormular formular = new BenutzerFormular();

        formular = mapper.benutzerToBenutzerFormular(benutzer);

        model.addAttribute("formular", formular);
        model.addAttribute("loginname", loginname);

        return "benutzer/bearbeiten";
    }

    @GetMapping("/{loginname}/delete")
    public String benutzerLöschen(@PathVariable("loginname") String loginname, RedirectAttributes redirectAttributes) {
        try {
            benutzerService.deleteBenutzerById(loginname);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("info", "Löschen fehlgeschlagen: " + e.getMessage());
        }

        return "redirect:/benutzer";
    }

    @PostMapping("/{loginname}")
    public String bearbeitenSpeichern(
            @PathVariable String loginname,
            @ModelAttribute("formular") @Valid BenutzerFormular formular,
            BindingResult bindingResult,
            Model model,
            HttpSession session) {

        // checkt, dass die Wiederholung stimmt
        if (formular.getLosung() != null && !formular.getLosung().isBlank()) {
            if (!formular.getLosung().equals(formular.getLosungWiederh())) {
                bindingResult.rejectValue("losungWiederh", "benutzer.losungwh.validation");
            }
        }

        if (bindingResult.hasErrors()) {
            logger.info("Error has been detected: " + bindingResult.toString());
            return "benutzer/bearbeiten";
        }

        logger.info("Empfangenes Formular: " + formular.toString());

        Optional<Benutzer> gespeicherterNutzerOpt = benutzerService.findBenutzerById(loginname);
        Benutzer benutzer;
        if (gespeicherterNutzerOpt.isPresent()) {
            benutzer = gespeicherterNutzerOpt.get();
            String alteLosung = benutzer.getLosung(); // Vorher merken!
            mapper.updateBenutzerFromFormular(benutzer, formular);

            if (formular.getLosung() == null || formular.getLosung().isBlank()) {
                benutzer.setLosung(alteLosung);
            } else {
                benutzer.setLosung(passwordEncoder.encode(formular.getLosung()));
            }
        } else {
            // Neuer Benutzer
            benutzer = mapper.benutzerFormularToBenutzer(formular);
            benutzer.setLoginName(loginname);
            benutzer.setLosung(passwordEncoder.encode(formular.getLosung()));
        }

        benutzerService.saveBenutzer(benutzer);

        return "benutzer/bearbeiten";
    }

    @GetMapping("/{loginName}/hx/feld/{feldname}")
    public String zeigeBenutzerFeld(
            @PathVariable String loginName,
            @PathVariable String feldname,
            Model model) {

        // Wert aus dem Benutzer-Objekt holen
        Benutzer benutzer = benutzerService.findBenutzerById(loginName).orElse(null);
        String wert = "";
        if (benutzer != null) {
            switch (feldname) {
                case "name" -> wert = benutzer.getName();
                case "digitalpostanschrift", "email" -> wert = benutzer.getDigitalpostanschrift();
                // ggf. weitere Felder ergänzen
                default -> wert = "";
            }
        }

        model.addAttribute("loginName", loginName);
        model.addAttribute("feldname", feldname);
        model.addAttribute("wert", wert);

        return "benutzer/eingabefeld :: bearbeiten";
    }

    @PutMapping("/{loginName}/hx/feld/{feldname}")
    public String bearbeiteBenutzerFeld(
            @PathVariable String loginName,
            @PathVariable String feldname,
            @RequestParam("wert") String wert,
            Model model) {

        try {
            benutzerService.aktualisiereBenutzerAttribut(loginName, feldname, wert);

            model.addAttribute("loginName", loginName);
            model.addAttribute("feldname", feldname);
            model.addAttribute("wert", wert);

            // Nach erfolgreicher Änderung: Anzeige-Fragment zurückgeben
            return "benutzer/eingabefeld :: ausgeben";
        } catch (Exception e) {

            // Bei Fehler: Wert aus der Datenbank holen, also wieder den alten Wert setzen
            Benutzer benutzer = benutzerService.findBenutzerById(loginName).orElse(null);
            String aktuellerWert = "";

            // BenutzerFormular formular = new BenutzerFormular();
            // formular = mapper.benutzerToBenutzerFormular(benutzer);

            if (benutzer != null) {
                switch (feldname) {
                    case "name" -> aktuellerWert = benutzer.getName();
                    case "digitalpostanschrift", "email" -> aktuellerWert = benutzer.getDigitalpostanschrift();
                    default -> aktuellerWert = "";
                }
            }

            model.addAttribute("loginName", loginName);
            model.addAttribute("feldname", feldname);
            model.addAttribute("wert", aktuellerWert);
            model.addAttribute("fehler", e.getMessage());
            return "benutzer/eingabefeld :: bearbeiten";
        }
    }

}
