package de.hsrm.mi.web.projekt.entities.doener.services;

import java.util.Random;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GenericDoenerNamingServiceImpl implements GenericDoenerNamingService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final Random random = new Random();

    @Override
    public String getName() {
        int id = 1 + random.nextInt(1000);
        String url = "https://pokeapi.co/api/v2/pokemon/" + id;

        try {
            ResponseEntity<PokemonResponse> response =
                restTemplate.getForEntity(url, PokemonResponse.class);
            String rawName = response.getBody().name();
            String capitalized = rawName.substring(0, 1).toUpperCase() + rawName.substring(1);
            return capitalized + "dön";
        } catch (Exception e) {
            return "FehlerDön-" + id;
        }
    }

    record PokemonResponse(String name) {}

}