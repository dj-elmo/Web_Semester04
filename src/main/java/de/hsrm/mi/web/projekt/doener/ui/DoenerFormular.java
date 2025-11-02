package de.hsrm.mi.web.projekt.doener.ui;

import java.util.ArrayList;
import java.util.List;

import de.hsrm.mi.web.projekt.entities.zutat.Zutat;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class DoenerFormular {

    @NotBlank(message = "Bezeichnung darf nicht leer sein")
    @Size(min = 1, max = 40)
    private String bezeichnung = "";

    @PositiveOrZero(message = "Preis darf nicht negativ sein")
    private double preis;

    @NotNull
    private Integer vegetarizitaet = 0;

    @PositiveOrZero(message = "Bestand darf nicht negativ sein")
    private int bestand;

    @ManyToMany
    private List<Zutat> zutaten = new ArrayList<>();

    private List<String> zutatenEan = new ArrayList<>();

    private long version;


    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public double getPreis() {
        return preis;
    }

    public void setPreis(double preis) {
        this.preis = preis;
    }

    public Integer getVegetarizitaet() {
        return vegetarizitaet;
    }

    public void setVegetarizitaet(Integer vegetarizitaet) {
        this.vegetarizitaet = vegetarizitaet;
    }

    public int getBestand() {
        return bestand;
    }

    public void setBestand(int bestand) {
        this.bestand = bestand;
    }

    public List<Zutat> getZutaten() {
        return zutaten;
    }

    public void setZutaten(List<Zutat> zutaten) {
        this.zutaten = zutaten;
    }

    public List<String> getZutatenEan() {
        return zutatenEan;
    }

    public void setZutatenEan(List<String> zutatenEan) {
        this.zutatenEan = zutatenEan;
    }

}