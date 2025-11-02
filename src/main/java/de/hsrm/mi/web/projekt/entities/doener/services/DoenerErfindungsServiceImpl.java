package de.hsrm.mi.web.projekt.entities.doener.services;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import de.hsrm.mi.web.projekt.entities.doener.Doener;
import de.hsrm.mi.web.projekt.entities.doener.DoenerRepository;
import de.hsrm.mi.web.projekt.entities.zutat.Zutat;
import de.hsrm.mi.web.projekt.entities.zutat.ZutatRepository;
import de.hsrm.mi.web.projekt.messaging.FrontendNachrichtEvent;
import jakarta.transaction.Transactional;

@Service
public class DoenerErfindungsServiceImpl implements DoenerErfindungsService {

    private final ZutatRepository zutatRepository;
    private final DoenerRepository doenerRepository;
    private final GenericDoenerNamingService namingService;
    private final Random random = new Random();

    @Autowired
    private ApplicationEventPublisher publisher;

    public DoenerErfindungsServiceImpl(ZutatRepository zutatRepository,
                                       DoenerRepository doenerRepository,
                                       GenericDoenerNamingService namingService) {
        this.zutatRepository = zutatRepository;
        this.doenerRepository = doenerRepository;
        this.namingService = namingService;
    }

    @Override
    @Transactional
    public Doener createRandomDoener() {
        List<Zutat> alleZutaten = zutatRepository.findAll();
        int maxAnzahl = Math.min(5, Math.max(1, alleZutaten.size() / 4));
        Collections.shuffle(alleZutaten);
        List<Zutat> ausgewaehlt = alleZutaten.subList(0, random.nextInt(maxAnzahl) + 1);

        int vegetarizitaet = ausgewaehlt.stream()
        .mapToInt(Zutat::getVegetarizitaet)
        .min()
        .orElse(0); // Falls leer, Standard = unvegetarisch


        int preis = 5 + random.nextInt(25); // 5–29
        int bestand = 1 + random.nextInt(100); // 1–100

        String name = namingService.getName();

        Doener neuerDoener = new Doener(name, ausgewaehlt, vegetarizitaet, preis, bestand);

        Doener savedDoener = doenerRepository.save(neuerDoener);


        FrontendNachrichtEvent.Operation operation = FrontendNachrichtEvent.Operation.CREATE;
        publisher.publishEvent(new FrontendNachrichtEvent(
                FrontendNachrichtEvent.EventTyp.DOENER,
                savedDoener.getId(),
                operation));
            
        return savedDoener;
    }
}
